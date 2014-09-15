package com.learningmodule.association.conceptdrug.multifeature;

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

	/*
	 * constructor with database Interface as arguments;
	 */
	public PredictionMethod(ConceptDrugDatabaseInterface databaseInterface) {
		this.databaseInterface = databaseInterface;
		dictionary = new LevenshteinSearch(databaseInterface);
	}

	/*
	 * Constructor with min support, min confidence and database interface
	 */
	public PredictionMethod(int minSupport, double minConfidence, ConceptDrugDatabaseInterface databaseInterface) {
		this.databaseInterface = databaseInterface;
		dictionary = new LevenshteinSearch(databaseInterface);
		this.minSupport = minSupport;
		this.minConfidence = minConfidence;
	}

	/*
	 * Prediction Results for a given query and search attributes
	 */
	public LinkedList<PredictionResult> predict(String query, SearchAttribute[] features) {

		//System.out.println(Arrays.toString(features));

		LinkedList<LevenshteinResults> list = dictionary.search(query, 0);

		// create a list of concepts ids from dictonary results
		LinkedList<String> ids = new LinkedList<String>();
		for (LevenshteinResults res : list) {
			for (String concept : res.getConcepts()) {
				ids.add(concept);
			}
		}

		// get the medical records with the list of concept ids
		LinkedList<EncounterIdConceptFeaturesDrugModel> data = databaseInterface.getDataByConceptIds(ids);
		
		// create set of unique drugs and concepts from medical records
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

		// for every pair of concept and drug in set of conceptIds and drugs
		for (String concept : concepts) {
			for (String drug : drugs) {
				
				// get the confidence level for this concept drug pair
				double conf = getConf(data, concept, features, drug);
				// System.out.println(conf + ", " + concept + ", " + drug);
				
				// if confidence is greater then min confidence level add the drug in results
				if (conf > minConfidence) {
					
					// increase the weight of drug if its already present in list of results
					conf = conf * getWeight(list, concept);
					boolean found = false;
					for (ConceptDrugPredictionResult temp : results) {
						if (temp.getDrug().getDrugId().equals(drug)) {
							found = true;
							
							// increase the weight using w = w1 + w2 - w1*w2
							temp.setConfidence(conf + temp.getConfidence()
									- (conf * temp.getConfidence()));
							temp.addConceptId(concept);
							break;
						}
					}
					// add the drug in list of results case its not already present there
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

	/*
	 * method to get the weight of a conceptId from dictionary results
	 */
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
	
	/*
	 * method to get the confidence of drug for given conceptId and searchAttributes 
	 */
	public double getConf(LinkedList<EncounterIdConceptFeaturesDrugModel> data, String conceptId,
			SearchAttribute[] features, String drugId) {
		
		// support of itemset<conceptId, attributes, drugId>
		double drugS = getSupport(data, conceptId, features, drugId);
		
		// support of itemset<conceptId, attributes>
		double conceptS = getSupport(data, conceptId, features, null);
		double conf = 0.0;
		
		// if support if itemset<conceptId, attribute, drugId> greater then minimum support
		if (drugS >= (double) minSupport) {
			
			//confidence  = support itemset<conceptId, attributes, drugId> / support itemset<conceptid, attributes>
			conf = drugS / conceptS;
		}
		for (int i = 0; i < features.length; i++) {
			
			// remove attribute at ith index to get subset of search attribute
			SearchAttribute[] subSetFeatures = getSubSet(i, features);
			
			// get confidence of this subset of attribute and set it as confidence if its greater then previous
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

	/*
	 * method that remove attributes at ith index from a set of attribute and reture subset
	 */
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

	/*
	 * method to get the support itemset<conceptId, attributes, drugId>
	 */
	public static double getSupport(LinkedList<EncounterIdConceptFeaturesDrugModel> data,
			String conceptId, SearchAttribute[] features, String drugId) {
		double sup = 0.0;
		int preId = 0;
		int curId;
		
		// for every medical records
		for (EncounterIdConceptFeaturesDrugModel item : data) {
			// current encounterId
			curId = item.getId();
			
			// if current encounter id is not equal to previous encounterId and 
			// drugId is either null or equal to the drugID in medical records
			if (curId != preId && item.getConceptId().equals(conceptId)
					&& (drugId == null || drugId.equals(item.getDrugId()))) {
				
				// Initialize the value of support for this record, 0.0 < support <= 1.0
				double temp = 1.0;
				
				// for every search attribute and feature in record
				for (SearchAttribute sa : features) {
					for (FeatureValue df : item.getFeatures()) {
						
						// attribute is compatible with the feature
						if (attributeIsFeature(sa, df.getFeature())) {
							// System.out.println(sa.getValue() instanceof
							// Integer);
							try {
								// calculate the distance of attribute value with feature value
								temp = temp
										* df.getDistance(new FeatureValue(df.getFeature(), sa
												.getValue()));
								
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
				
				// add support for this record to total support for itemset
				sup = sup + temp;
				// System.out.println(sup + ", " + curId);
			}
		}
		// System.out.println(sup);
		return sup;
	}

	/*
	 * Method to check search attribute is a Feature and has compatible types
	 */
	public static boolean attributeIsFeature(SearchAttribute at, Feature f) {
		
		// if feature name and attribute name are same
		if (at.getName().equals(f.getFeatureName())) {
			try {
				// System.out.println(f.getFeatureType());
				
				// check if the type of attribute value is compatible with feature types
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
