package com.learningmodule.association.conceptdrug.predictionmodule;

import java.util.Iterator;
import java.util.LinkedList;

import com.learningmodule.association.conceptdrug.learning.ConceptDrugLearning;
import com.learningmodule.association.conceptdrug.model.ConceptRow.Cell;
import com.learningmodule.association.conceptdrug.model.DrugModel;
import com.learningmodule.association.conceptdrug.model.PredictionMatrix;

/*
 * Class that predicts the drugs for given obs(concepts)
 */

public class PredictionMethod {
	public static LinkedList<DrugModel> predict(String query) {
		LinkedList<Integer> results = new LinkedList<Integer>();
		ConceptWordSearchDatabase.makeConnection();
		
		// get the list of concepts IDs from the search query
		LinkedList<Integer> conceptIds = ConceptWordSearchDatabase.search(query);
		ConceptWordSearchDatabase.closeConnection();
		PredictionMatrix matrix = ConceptDrugLearning.getMatrix();
		Iterator<Integer> concepts = conceptIds.iterator();
		while (concepts.hasNext()) {
			
			// get the list of drugsIDs for each conceptId from prediction matrix
			LinkedList<Cell> temp = matrix.getDrugs(concepts.next());
			if (temp != null) {
				Iterator<Cell> drugs = temp.iterator();
				while (drugs.hasNext()) {
					int drug = drugs.next().getDrug();
					
					// add the drugID in the linked list of drugIDs
					results.add(drug);
					//System.out.println(drug);
				}
			}
		}
		DrugTableOperation.makeConnection();
		
		// get the linked list of drugs from result drugIDs
		return DrugTableOperation.search(results);
	}

	public static void getAllConceptsInMatrix() {
		PredictionMatrix matrix = ConceptDrugLearning.getMatrix();
		LinkedList<Integer> conceptIds = matrix.getNonEmptyConcepts();
		String str = "( ";
		Iterator<Integer> itr = conceptIds.iterator();
		while(itr.hasNext()) {
			str = str + itr.next() + ", ";
		}
		str = str + ")";
		System.out.println(str);
	}
	
	public static void main(String[] args) {
		LinkedList<DrugModel> temp = predict("BLOOD LOSS");
		Iterator<DrugModel> itr = temp.iterator();
		while(itr.hasNext()) {
			System.out.println(itr.next());
		}
		//getAllConceptsInMatrix();
	}
}