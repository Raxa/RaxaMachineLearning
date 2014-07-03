package com.learningmodule.association.conceptdrug.predictionmodule;

import java.util.LinkedList;

import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.model.ConceptNameModel;
import com.learningmodule.association.conceptdrug.model.ConceptDrugPredictionResult;

/*
 * class that implements methods operating on table concept_name in OpenMRS
 */

public class ConceptNameDatabaseOperation {

	// method to get the list of tags for each drug result
	public static LinkedList<ConceptDrugPredictionResult> getTags(
			LinkedList<ConceptDrugPredictionResult> results,
			ConceptDrugDatabaseInterface learningInterface) {
		LinkedList<ConceptNameModel> concepts = learningInterface
				.getConceptIdNameByConceptIds(getIds(results));

		for (ConceptNameModel concept : concepts) {
			for (ConceptDrugPredictionResult result : results) {
				if (result.hasConcept(concept.getConceptId())) {
					// add the concept name is concept Id is related to drug
					result.addTags(concept.getConceptName());
				}
			}
		}
		return results;
	}

	/*
	 * method to get the string that has to be put in SQL query which has all
	 * conceptId saparted by comma
	 */
	private static LinkedList<Integer> getIds(LinkedList<ConceptDrugPredictionResult> results) {
		LinkedList<Integer> ids = new LinkedList<Integer>();
		for (ConceptDrugPredictionResult result : results) {
			for (Integer id : result.getConceptIds()) {
				ids.add(id);
			}
		}
		return ids;
	}
}
