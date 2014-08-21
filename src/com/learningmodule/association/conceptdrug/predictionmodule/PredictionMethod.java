package com.learningmodule.association.conceptdrug.predictionmodule;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.PredictionResult;
import com.learningmodule.association.conceptdrug.model.ConceptRow.Cell;
import com.learningmodule.association.conceptdrug.model.PredictionMatrix;
import com.learningmodule.association.conceptdrug.model.ConceptDrugPredictionResult;
import com.learningmodule.association.conceptdrug.search.LevenshteinResults;
import com.learningmodule.association.conceptdrug.search.LevenshteinSearch;

/*
 * Class that predicts the drugs for given query
 */

public class PredictionMethod {

	private PredictionMatrix matrix;
	private ConceptDrugDatabaseInterface learningInterface;
	private LevenshteinSearch dictonary;
	private Logger log = Logger.getLogger(PredictionMethod.class);
	
	public PredictionMethod(PredictionMatrix matrix,
			ConceptDrugDatabaseInterface learningInterface) {
		this.matrix = matrix;
		this.learningInterface = learningInterface;
		dictonary = new LevenshteinSearch(learningInterface);
	}

	public void setPredictionMatrix(PredictionMatrix matrix) {
		this.matrix = matrix;
	}

	public LinkedList<PredictionResult> predict(String query) {
		LinkedList<ConceptDrugPredictionResult> results = new LinkedList<ConceptDrugPredictionResult>();

		query.replace("\'", "\\\'");
		query.replace("\"", "\\\"");

		// get the list of concepts IDs using Levenshtein Search given the
		// query.
		LinkedList<LevenshteinResults> searchResults = dictonary.search(query, 2);
		log.info("dictonary search size:" + searchResults.size());
		// for each conceptId in each Levenshtein word Match
		for (LevenshteinResults result : searchResults) {
			for (String concept : result.getConcepts()) {

				// get the list of drugsIDs for each conceptId from prediction
				// matrix
				LinkedList<Cell> drugs = matrix.getDrugs(concept);
				if (drugs != null) {
					for (Cell drug : drugs) {
						// set the confidence for this drug multiplied by it
						// weight from approximate string search
						//System.out.println(drug + "," + drug.getConfidence());
						//log.info(drug.toString());
						
						drug.setConfidence(drug.getConfidence() * result.getWeight());
						boolean found = false;

						// if drug already present in the list set the new
						// confidence
						for (ConceptDrugPredictionResult tmp : results) {
							if (tmp.getDrug().getDrugId().equals(drug.getDrug())) {
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
							ConceptDrugPredictionResult tmp = new ConceptDrugPredictionResult(
									drug.getDrug(), drug.getConfidence());
							// add the conceptId in list of conceptId related to
							// drug
							tmp.addConceptId(concept);
							results.add(tmp);
						}
						
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
		results = ConceptNameDatabaseOperation.getTags(results, learningInterface);

		// get the linked list of drugs from result drugIDs
		return DrugTableOperation.addDrugInfo(results, learningInterface);
	}

	/*
	 * method to print value of all conceptId's with non empty drugs in
	 * prediction matrix (For Debuging purpose)
	 */
	public void getAllConceptsInMatrix() {
		LinkedList<String> conceptIds = matrix.getNonEmptyConcepts();
		String str = "( ";
		for (String itr : conceptIds) {
			str = str + itr + ", ";
		}
		str = str + ")";
		System.out.println(str);
		log.debug("allConcepts" + str);
	}

	/*
	 * public static void main(String[] args) { LinkedList<PredictionResults>
	 * temp = predict("anemia blood loss"); for (PredictionResults row : temp) {
	 * System.out.println(row.toString()); } // getAllConceptsInMatrix(); }
	 */
}