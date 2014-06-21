package com.learningmodule.association.conceptdrug.predictionmodule;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import com.learningmodule.association.conceptdrug.learning.ConceptDrugLearning;
import com.learningmodule.association.conceptdrug.model.ConceptRow.Cell;
import com.learningmodule.association.conceptdrug.model.PredictionMatrix;
import com.learningmodule.association.conceptdrug.model.PredictionResults;
import com.raxa.string.search.LevenshteinResults;
import com.raxa.string.search.LevenshteinSearch;

/*
 * Class that predicts the drugs for given query
 */

public class PredictionMethod {
	public static LinkedList<PredictionResults> predict(String query) {
		LinkedList<PredictionResults> results = new LinkedList<PredictionResults>();

		query.replace('\'', ' ');
		query.replace('\"', ' ');

		// get the list of concepts IDs using Levenshtein Search given the
		// query.
		LinkedList<LevenshteinResults> searchResults = LevenshteinSearch.search(query, 2);

		// get the prediction matrix
		PredictionMatrix matrix = ConceptDrugLearning.getMatrix();

		// for each conceptId in each Levenshtein word Match
		for (LevenshteinResults result : searchResults) {
			for (Integer concept : result.getConcepts()) {

				// get the list of drugsIDs for each conceptId from prediction
				// matrix
				LinkedList<Cell> drugs = matrix.getDrugs(concept);
				if (drugs != null) {
					for (Cell drug : drugs) {
						// set the confidence for this drug multiplied by it
						// weight from approximate string search
						drug.setConfidence(drug.getConfidence() * result.getWeight());
						boolean found = false;

						// if drug already present in the list set the new
						// confidence
						for (PredictionResults tmp : results) {
							if (tmp.getDrug().getDrugId() == (drug.getDrug())) {
								tmp.setConfidence(drug.getConfidence() + tmp.getConfidence()
										- (drug.getConfidence() * tmp.getConfidence()));

								// add the conceptId in list of conceptId
								// related to drug
								tmp.addConceptId(concept);
								found = true;
								break;
							}
						}
						// if drug is not already present in results
						if (!found) {
							// add the drugID in the linked list of drugIDs
							PredictionResults tmp = new PredictionResults(drug.getDrug(),
									drug.getConfidence());
							// add the conceptId in list of conceptId related to
							// drug
							tmp.addConceptId(concept);
							results.add(tmp);
						}
						// System.out.println(drug);
					}
				}
			}
		}

		// sort the results in decreasing order of the confidence
		Collections.sort(results, new Comparator<PredictionResults>() {

			@Override
			public int compare(PredictionResults o1, PredictionResults o2) {
				if (o1.getConfidence() > o2.getConfidence())
					return -1;
				else if (o1.getConfidence() < o2.getConfidence())
					return 1;
				else
					return 0;
			}
		});

		// get the tags for all drug results
		results = ConceptNameDatabaseOperation.getTags(results);
		/* for (PredictionResults itr : results) {
			System.out.println(itr.toString());
		} */

		// get the linked list of drugs from result drugIDs
		return DrugTableOperation.search(results);
	}

	/*
	 * method to print value of all conceptId's with non empty drugs in prediction matrix
	 * (For Debuging purpose)
	 */
	public static void getAllConceptsInMatrix() {
		PredictionMatrix matrix = ConceptDrugLearning.getMatrix();
		LinkedList<Integer> conceptIds = matrix.getNonEmptyConcepts();
		String str = "( ";
		for (Integer itr : conceptIds) {
			str = str + itr + ", ";
		}
		str = str + ")";
		System.out.println(str);
	}

	public static void main(String[] args) {
		LinkedList<PredictionResults> temp = predict("anemia blood loss");
		for (PredictionResults row : temp) {
			System.out.println(row.toString());
		}
		// getAllConceptsInMatrix();
	}
}