package com.umls.search;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import com.learningmodule.association.conceptdrug.PredictionResult;
import com.learningmodule.association.conceptdrug.model.DrugModel;
import com.machine.learning.LearningModulesPool;
import com.machine.learning.request.SearchAttribute;
import com.umls.database.MRCONSETableOperation;
import com.umls.database.MRDEFTableOperation;
import com.umls.database.MRRELTableOperation;
import com.umls.database.MRSTYTableOperation;
import com.umls.database.MRXWTableOperation;
import com.umls.models.ConceptDefination;
import com.umls.models.ConceptModel;
import com.umls.models.DrugGeneric;
import com.umls.models.UmlsPredictionResult;
import com.umls.models.RelationModel;
import com.umls.models.SemanticType;
import com.umls.models.UmlsResult;

/*
 * Class that implement method to search for query in umls and then 
 * improve the results based on machine learning algorithm results
 */

public class DiseaseToDrugPrediction {

	/*
	 * method to search for a query
	 */
	public static UmlsResult predict(String query, SearchAttribute[] features) {

		// get the list for CUI's for this search query
		LinkedList<String> cuis = MRXWTableOperation.searchCUI(query);
		System.out.println("d1");

		// if list is not empty
		if (!cuis.isEmpty()) {

			// get filter the cui's with semantic type of
			// disease/syndrom/symptom
			LinkedList<SemanticType> types = MRSTYTableOperation
					.getDiseaseSyndromSymptomByCUI(cuis);
			System.out.println("d2");
			cuis = getCuis(types);

			// get the Concept name and other info from MRCONSE table for the
			// disease CUI's.
			LinkedList<ConceptModel> diseases = MRCONSETableOperation.getConceptByCUI(cuis);

			// get the list of concepts related to the diseases CUI's
			LinkedList<RelationModel> relatedConceptsList = MRRELTableOperation
					.getRelatedConceptsByCUI(cuis);
			System.out.println("d3");
			LinkedList<String> relatedcuis = getRelatedCuis(relatedConceptsList);
			System.out.println(relatedcuis.size());

			// filter out the Concepts which are drugs from list of related
			// CUI's
			LinkedList<SemanticType> drugtypes = MRSTYTableOperation.getDrugsByCUI(relatedcuis);
			System.out.println("d4");
			LinkedList<String> drugCuis = getCuis(drugtypes);

			// get the drug concept Info for drug list
			LinkedList<ConceptModel> drugs = MRCONSETableOperation.getDrugConceptsByCUI(drugCuis);
			// showDrugRelations(drugs, relatedConceptsList);
			System.out.println("d5");

			// add weights to drugs depending upon drug is contra-indicated or
			// it may treat the disease
			LinkedList<UmlsPredictionResult> results = addWeights(relatedConceptsList, drugs);

			// add weights to list of disease found for given search query
			LinkedList<UmlsPredictionResult> weightedDiseases = sortResults(getWeightedDiseaseList(
					diseases, query));

			// get the list of generics out of the list of drugs
			LinkedList<DrugGeneric> generics = getListOfDrugGeneric(results);

			// add weights to list of drug generics depending on disease it may
			// treat
			generics = addMayTreats(weightedDiseases, generics, relatedConceptsList);

			// Use machine learning results to change weights list of generics
			generics = getMLPredictions(generics, query, features);

			// sort the list of drug generics
			generics = sortDrugs(generics);

			// limit on top 30 drug generics
			generics = limitNoOfDrugs(30, generics);

			// remove the suggested diseases with weight less then 0.5
			weightedDiseases = limitNoOfDisease(0.5, weightedDiseases);

			// set the umls result for this query
			UmlsResult result = new UmlsResult(weightedDiseases, generics);

			// get the definition for this search query(disease)
			result.setDisDef(getDef(weightedDiseases, query));
			return result;
		} else {
			return new UmlsResult(null, null);
		}
	}

	/*
	 * method to remove the suggestions with confidence less the threshold
	 */
	private static LinkedList<UmlsPredictionResult> limitNoOfDisease(double thresholdConf,
			LinkedList<UmlsPredictionResult> diseases) {
		LinkedList<UmlsPredictionResult> results = new LinkedList<UmlsPredictionResult>();
		for (UmlsPredictionResult disease : diseases) {
			if (disease.getConfidence() >= thresholdConf) {
				results.add(disease);
			} else {
				break;
			}
		}
		return results;
	}

	/*
	 * method to limit the number of drugs for a given size
	 */
	private static LinkedList<DrugGeneric> limitNoOfDrugs(int size, LinkedList<DrugGeneric> generics) {
		int i = 0;
		LinkedList<DrugGeneric> results = new LinkedList<DrugGeneric>();
		for (DrugGeneric gen : generics) {
			if (i < size) {
				results.add(gen);
			} else {
				break;
			}
			i++;
		}
		return results;
	}

	/*
	 * Method to get the definition for the search query
	 */
	private static ConceptDefination getDef(LinkedList<UmlsPredictionResult> diseases, String query) {
		ConceptDefination temp = null;
		int i = 0;

		// for 1st 30 disease found for search query
		for (UmlsPredictionResult disease : diseases) {
			if (i > 30)
				break;
			i++;

			// if distance of search query from disease name is less the 1 or
			// search query is a short form
			if (LivenstineDistance.getDistance(disease.getValue().getName(), query) <= 1
					|| queryIsShortForm(disease.getValue().getName(), query)) {

				// get the definition for this CUI
				temp = MRDEFTableOperation.getDefByCUI(disease.getValue().getCUI(), disease
						.getValue().getName());
				if (temp != null) {
					return temp;
				}
			}
		}
		return null;
	}

	/*
	 * method to check is the given search query is a shortForm. Short Form
	 * should have all upper case letters
	 */
	private static boolean queryIsShortForm(String name, String query) {

		// all characters should be upper case.
		if (query.toUpperCase().equals(query)) {
			name = name.toLowerCase();
			String[] temp = name.split("\\s+");
			query.replaceAll(".", "");
			String shortForm = "";
			for (int i = 0; i < temp.length; i++) {
				shortForm = shortForm + temp[i].charAt(0);
			}
			if (LivenstineDistance.getDistance(query, shortForm) < 2) {
				return true;
			}
		}
		return false;
	}

	/*
	 * method that adds the disease that a generic mayTreat given the relations
	 * between drug and disease in umls
	 */
	private static LinkedList<DrugGeneric> addMayTreats(LinkedList<UmlsPredictionResult> diseases,
			LinkedList<DrugGeneric> generics, LinkedList<RelationModel> relations) {

		for (DrugGeneric generic : generics) {
			for (UmlsPredictionResult disease : diseases) {
				boolean found = false;
				for (UmlsPredictionResult drug : generic.getDrugs()) {
					for (RelationModel rel : relations) {
						if (rel.getCUI().equals(disease.getValue().getCUI())
								&& rel.getRelatedCUI().equals(drug.getValue().getCUI())) {
							generic.addMayreat(disease.getValue());
							generic.setConfidence(max(generic.getConfidence(),
									disease.getConfidence() * drug.getConfidence()));
							found = true;
							// System.out.print("yes");
							break;
						}
					}
					if (found)
						break;
				}
			}
		}
		return generics;
	}

	private static double max(double x, double y) {
		if (x > y)
			return x;
		else
			return y;
	}

	private static void showDrugRelations(LinkedList<ConceptModel> drugCuis,
			LinkedList<RelationModel> rel) {
		for (ConceptModel cui : drugCuis) {
			for (RelationModel re : rel) {
				if (re.getRelatedCUI().equals(cui.getCUI())) {
					System.out.println(re + "|" + cui);
				}
			}
		}
	}

	/*
	 * method to get the list of cui's from relations
	 */
	private static LinkedList<String> getRelatedCuis(LinkedList<RelationModel> list) {
		LinkedList<String> cuis = new LinkedList<String>();
		for (RelationModel row : list) {
			cuis.add(row.getRelatedCUI());
		}
		return cuis;
	}

	/*
	 * method to sort the drug generic results
	 */
	private static LinkedList<DrugGeneric> sortDrugs(LinkedList<DrugGeneric> list) {

		Collections.sort(list, new Comparator<DrugGeneric>() {

			@Override
			public int compare(DrugGeneric o1, DrugGeneric o2) {
				if (o1.getConfidence() > o2.getConfidence())
					return -1;
				else if (o1.getConfidence() < o2.getConfidence())
					return 1;
				else
					return 0;
			}
		});
		return list;
	}

	/*
	 * method to sort the UmlsPredictionResult(disease/drugs)
	 */
	private static LinkedList<UmlsPredictionResult> sortResults(
			LinkedList<UmlsPredictionResult> list) {

		Collections.sort(list, new Comparator<UmlsPredictionResult>() {

			@Override
			public int compare(UmlsPredictionResult o1, UmlsPredictionResult o2) {
				if (o1.getConfidence() > o2.getConfidence())
					return -1;
				else if (o1.getConfidence() < o2.getConfidence())
					return 1;
				else
					return 0;
			}
		});
		LinkedList<UmlsPredictionResult> results = new LinkedList<UmlsPredictionResult>();
		for (UmlsPredictionResult item : list) {
			boolean found = false;
			for (UmlsPredictionResult res : results) {
				if (item.getValue().getCUI().equals(res.getValue().getCUI())) {
					found = true;
					break;
				}
			}
			if (!found) {
				results.add(item);
			}
		}
		return results;
	}

	/*
	 * method that add the weights for drugs based on whether they are contra
	 * indicated or may treat relation ship attribute
	 */
	private static LinkedList<UmlsPredictionResult> addWeights(LinkedList<RelationModel> rels,
			LinkedList<ConceptModel> drugs) {

		LinkedList<UmlsPredictionResult> results = new LinkedList<UmlsPredictionResult>();
		for (ConceptModel drug : drugs) {
			results.add(new UmlsPredictionResult(1.0, drug));
		}
		for (RelationModel rel : rels) {

			// set the weight to 0.0 if the drug is contraindicated for this
			// disease
			if (rel.getRelationAttribute() != null
					&& rel.getRelationAttribute().equalsIgnoreCase("contraindicated_drug")) {
				for (UmlsPredictionResult result : results) {
					if (result.getValue().getCUI().equals(rel.getRelatedCUI())) {
						result.setConfidence(0.0);
					}
				}
			}
		}
		return results;
	}

	/*
	 * method to get list of generic and group the drug with those generics
	 */
	private static LinkedList<DrugGeneric> getListOfDrugGeneric(
			LinkedList<UmlsPredictionResult> list) {
		LinkedList<DrugGeneric> results = new LinkedList<DrugGeneric>();
		for (UmlsPredictionResult item : list) {
			if (item.getConfidence() > 0.0) {
				String generic = getGeneric(item.getValue().getName());
				boolean found = false;
				for (DrugGeneric result : results) {
					if (result.getGeneric().equals(generic)) {
						result.addDrug(item);
						found = true;
						break;
					}
				}
				if (!found) {
					DrugGeneric temp = new DrugGeneric(generic);
					temp.addDrug(item);
					results.add(temp);

				}
			}
		}
		return results;
	}

	/*
	 * get the Generic Name from the Ingredient dosage form of the drug
	 */
	private static String getGeneric(String name1) {
		String temp1[] = name1.split("\\s+");

		String generic1 = "";
		int i = 0;
		if (isNumeric(temp1[0])) {
			i = 2;
		}
		while (i < temp1.length) {
			if (isNumeric(temp1[i])) {
				break;
			}
			generic1 = generic1 + temp1[i] + " ";
			i++;
		}

		return generic1;
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/*
	 * method to add semantics types to concept Model
	 */
	public static LinkedList<ConceptModel> addSemanticTypes(LinkedList<ConceptModel> concepts,
			LinkedList<SemanticType> types) {
		for (SemanticType type : types) {
			for (ConceptModel concept : concepts) {
				if (concept.getCUI().equals(type.getCUI())) {
					concept.setSemanticTypes(type);
				}
			}
		}
		return concepts;
	}

	/*
	 * get list of CUI's from the list of semantic type
	 */
	public static LinkedList<String> getCuis(LinkedList<SemanticType> types) {
		LinkedList<String> cuis = new LinkedList<String>();
		for (SemanticType type : types) {
			cuis.add(type.getCUI());
		}
		return cuis;
	}

	/*
	 * add the weights for suggestion diseases using the Livenstine Distance
	 */
	public static LinkedList<UmlsPredictionResult> getWeightedDiseaseList(
			LinkedList<ConceptModel> diseases, String query) {
		LinkedList<UmlsPredictionResult> result = new LinkedList<UmlsPredictionResult>();
		for (ConceptModel disease : diseases) {
			result.add(new UmlsPredictionResult(LivenstineDistance.getWordMatchDistance(
					disease.getName(), query), disease));
		}
		return result;
	}

	/*
	 * method to get the machine learning predictions and increase the weight of
	 * drug generics found by machine learning
	 */
	public static LinkedList<DrugGeneric> getMLPredictions(LinkedList<DrugGeneric> generics,
			String query, SearchAttribute[] features) {
		LinkedList<PredictionResult> list = LearningModulesPool.predict(query, features);
		for (PredictionResult item : list) {
			DrugModel drug = (DrugModel) item.getValue();
			// System.out.print(drug.getName() + "," + drug.getGeneric());
			for (DrugGeneric gen : generics) {
				if (belongsToGeneric(drug, gen.getGeneric())) {
					gen.setConfidence(gen.getConfidence() + item.getConfidence());
					// System.out.print( ", " + gen.getGeneric());
				}
			}
			// System.out.println();
		}
		return generics;
	}

	public static boolean belongsToGeneric(DrugModel drug, String generic) {
		String gen;
		if (drug.getGeneric() != null) {
			gen = drug.getGeneric();
		} else {
			gen = drug.getName();
		}
		gen = gen.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]+", "\\s");

		generic = generic.toLowerCase().replace("[^a-zA-Z0-9\\s]+", "\\s");

		String[] temp1 = gen.split("\\s+");
		String[] temp2 = generic.split("\\s+");
		boolean found = true;
		for (int i = 0; i < 1; i++) {
			boolean temp = false;
			for (int j = 0; j < temp1.length; j++) {
				temp = (temp1[j].equals(temp2[i]) || temp);
			}
			found = (found && temp);
		}
		return found;
	}

	public static void main(String[] args) {

		UmlsResult result = predict("asthma", new SearchAttribute[0]);

		int i = 0;
		for (DrugGeneric drug : result.getDrugs()) {
			i++;
			System.out.println(drug);
			if (i > 20)
				break;
		}
		System.out.print(result.getDisDef());
	}
}