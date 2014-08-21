package com.learningmodule.association.conceptdrug.multifeature;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.PredictionResult;
import com.learningmodule.association.conceptdrug.model.ConceptDrugPredictionResult;
import com.learningmodule.association.conceptdrug.predictionmodule.ConceptNameDatabaseOperation;
import com.learningmodule.association.conceptdrug.predictionmodule.DrugTableOperation;
import com.learningmodule.association.conceptdrug.search.LevenshteinResults;
import com.learningmodule.association.conceptdrug.search.LevenshteinSearch;
import com.machine.learning.request.Request;
import com.machine.learning.request.SearchAttribute;
import com.pacemaker.association.PaceMakeConceptDrugDatabaseInput;

public class PredictionMethod {

	private ConceptDrugDatabaseInterface databaseInterface;
	private LevenshteinSearch dictionary;

	// min support for association rules
	private int minSupport = 1;

	// minConfidence for association rules
	private double minConfidence = 0.3;

	public PredictionMethod(ConceptDrugDatabaseInterface databaseInterface) {
		this.databaseInterface = databaseInterface;
		dictionary = new LevenshteinSearch(databaseInterface);
	}

	public PredictionMethod(int minSupport, double minConfidence, ConceptDrugDatabaseInterface databaseInterface) {
		this.databaseInterface = databaseInterface;
		dictionary = new LevenshteinSearch(databaseInterface);
		this.minSupport = minSupport;
		this.minConfidence = minConfidence;
	}

	public LinkedList<PredictionResult> predict(String query, SearchAttribute[] features) {

		System.out.println(Arrays.toString(features));

		LinkedList<LevenshteinResults> list = dictionary.search(query, 0);

		LinkedList<String> ids = new LinkedList<String>();
		for (LevenshteinResults res : list) {
			for (String concept : res.getConcepts()) {
				ids.add(concept);
			}
		}

		LinkedList<EncounterIdConceptFeaturesDrugModel> data = databaseInterface.getDataByConceptIds(ids);
		HashSet<String> drugs = new HashSet<String>();
		HashSet<String> concepts = new HashSet<String>();
		for (EncounterIdConceptFeaturesDrugModel item : data) {
			if (!drugs.contains(item.getDrugId())) {
				drugs.add(item.getDrugId());
				// System.out.println(item.getDrugId());
			}
			if (!concepts.contains(item.getConceptId())) {
				concepts.add(item.getConceptId());
				// System.out.println(item.getConceptId());
			}
		}

		LinkedList<ConceptDrugPredictionResult> results = new LinkedList<ConceptDrugPredictionResult>();

		for (String concept : concepts) {
			for (String drug : drugs) {
				double conf = getConf(data, concept, features, drug);
				// System.out.println(conf + ", " + concept + ", " + drug);
				if (conf > minConfidence) {
					conf = conf * getWeight(list, concept);
					boolean found = false;
					for (ConceptDrugPredictionResult temp : results) {
						if (temp.getDrug().getDrugId().equals(drug)) {
							found = true;
							temp.setConfidence(conf + temp.getConfidence()
									- (conf * temp.getConfidence()));
							temp.addConceptId(concept);
							break;
						}
					}
					if (!found) {
						ConceptDrugPredictionResult tmp = new ConceptDrugPredictionResult(drug,
								conf);
						tmp.addConceptId(concept);
						results.add(tmp);
					}
				}
			}
		}
		// sort the results in decreasing order of the confidence
		Collections.sort(results, new Comparator<ConceptDrugPredictionResult>() {

			@Override
			public int compare(ConceptDrugPredictionResult o1, ConceptDrugPredictionResult o2) {
				if (o1.getConfidence() > o2.getConfidence())
					return -1;
				else if (o1.getConfidence() < o2.getConfidence())
					return 1;
				else
					return 0;
			}
		});

		// get the tags for all drug results
		results = ConceptNameDatabaseOperation.getTags(results, databaseInterface);

		// get the linked list of drugs from result drugIDs
		return DrugTableOperation.addDrugInfo(results, databaseInterface);

	}

	public static double getWeight(LinkedList<LevenshteinResults> list, String conceptId) {
		double weight = 0.0;
		for (LevenshteinResults item : list) {
			for (String concept : item.getConcepts()) {
				if (concept.equals(conceptId)) {
					weight = max(weight, item.getWeight());
				}
			}
		}
		return weight;
	}

	public double getConf(LinkedList<EncounterIdConceptFeaturesDrugModel> data, String conceptId,
			SearchAttribute[] features, String drugId) {
		double drugS = getSupport(data, conceptId, features, drugId);
		double conceptS = getSupport(data, conceptId, features, null);
		double conf = 0.0;
		if (drugS >= (double) minSupport) {
			conf = drugS / conceptS;
		}
		for (int i = 0; i < features.length; i++) {
			SearchAttribute[] subSetFeatures = getSubSet(i, features);
			conf = max(conf, 0.9 * getConf(data, conceptId, subSetFeatures, drugId));
		}

		return conf;
	}

	public static double max(double x, double y) {
		if (x > y)
			return x;
		else
			return y;
	}

	public static SearchAttribute[] getSubSet(int index, SearchAttribute[] ats) {
		SearchAttribute[] subset = new SearchAttribute[ats.length - 1];
		for (int i = 0; i < index; i++) {
			subset[i] = ats[i];
		}
		for (int i = index + 1; i < ats.length; i++) {
			subset[i - 1] = ats[i];
		}
		return subset;
	}

	public static double getSupport(LinkedList<EncounterIdConceptFeaturesDrugModel> data,
			String conceptId, SearchAttribute[] features, String drugId) {
		double sup = 0.0;
		int preId = 0;
		int curId;
		for (EncounterIdConceptFeaturesDrugModel item : data) {
			curId = item.getId();
			if (curId != preId && item.getConceptId().equals(conceptId)
					&& (drugId == null || drugId.equals(item.getDrugId()))) {
				double temp = 1.0;
				for (SearchAttribute sa : features) {
					for (FeatureValue df : item.getFeatures()) {
						if (attributeIsFeature(sa, df.getFeature())) {
							// System.out.println(sa.getValue() instanceof
							// Integer);
							try {
								temp = temp
										* df.getDistance(new FeatureValue(df.getFeature(), sa
												.getValue()));
								// System.out.println("yo!" + temp + "," +
								// df.getValue());
								preId = curId;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				if (features.length == 0 || drugId == null) {
					preId = curId;
				}
				sup = sup + temp;
				// System.out.println(sup + ", " + curId);
			}
		}
		// System.out.println(sup);
		return sup;
	}

	public static boolean attributeIsFeature(SearchAttribute at, Feature f) {
		if (at.getName().equals(f.getFeatureName())) {
			try {
				// System.out.println(f.getFeatureType());
				switch (f.getFeatureType()) {
				case Feature.INTEGERTYPE:
					if (at.getValue() instanceof String) {
						Integer i = Integer.parseInt((String) at.getValue());
						at.setValue(i);
					} else {
						Integer i = (Integer) at.getValue();
					}
					return true;
				case Feature.STRINGTYPE:
					String st = (String) at.getValue();
					at.setValue(st);
					return true;
				default:
					return false;
				}
			} catch (Exception e) {
				// System.out.println(at.getValue());
				// e.printStackTrace();
			}
		}
		return false;
	}

	public static void main(String[] args) {
		PredictionMethod method = new PredictionMethod(new PaceMakeConceptDrugDatabaseInput());

		/*
		 * LinkedList<String> ids = new LinkedList<String>(); ids.add("493");
		 * LinkedList<EncounterIdConceptFeaturesDrugModel> temp =
		 * LearningMethod.getData(ids); for (EncounterIdConceptFeaturesDrugModel
		 * item : temp) { System.out.println(item); }
		 * System.out.println(method.getSupport(temp, "493", ats, "1232810"));
		 */
		Gson gson = new Gson();
		String msg = "{searchRequest: { query: 'asthma', features:[{name: 'state', value: 'NY'}, {name: 'age', value: '40'}] }}";
		Request req = gson.fromJson(msg, Request.class);

		LinkedList<PredictionResult> results = method.predict(req.getSearchRequest().getQuery(),
				req.getSearchRequest().getFeatures());
		for (PredictionResult result : results) {
			System.out.println(result.getValue() + ", " + result.getConfidence());
		}
	}

	public int getMinSupport() {
		return minSupport;
	}

	public void setMinSupport(int minSupport) {
		this.minSupport = minSupport;
	}

	public double getMinConfidence() {
		return minConfidence;
	}

	public void setMinConfidence(double minConfidence) {
		this.minConfidence = minConfidence;
	}
}
