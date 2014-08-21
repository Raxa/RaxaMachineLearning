package com.learningmodule.association.conceptdrug.predictionmodule;

import java.util.LinkedList;

import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.model.ConceptNameModel;
import com.learningmodule.association.conceptdrug.model.ConceptDrugPredictionResult;

/*
 * class that fetch the concept name from database interface and add names to tag in results
 */

public class ConceptNameDatabaseOperation {

	// method to get the list of tags for each drug result
	public static LinkedList<ConceptDrugPredictionResult> getTags(
			LinkedList<ConceptDrugPredictionResult> results,
			ConceptDrugDatabaseInterface learningDataInterface) {

		// get the list of conceptId and conceptNames from database Interface
		LinkedList<ConceptNameModel> concepts = learningDataInterface
				.getConceptIdNameByConceptIds(getIds(results));

		// loop adds the concepts names to tags in results
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
	 * conceptId separated by comma
	 */
	private static LinkedList<String> getIds(LinkedList<ConceptDrugPredictionResult> results) {
		LinkedList<String> ids = new LinkedList<String>();
		for (ConceptDrugPredictionResult result : results) {
			for (String id : result.getConceptIds()) {
				ids.add(id);
			}
		}
		return ids;
	}
}
