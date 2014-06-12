package com.learningmodule.association.conceptdrug.predictionmodule;

import java.util.Collections;
import java.util.Comparator;
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
	public static DrugModel[] predict(String query) {
		LinkedList<Cell> results = new LinkedList<Cell>();

		query.replace('\'', ' ');
		query.replace('\"', ' ');
		
		// get the list of concepts IDs from the search query
		LinkedList<Integer> conceptIds = ConceptWordSearchDatabase.search(query);
		PredictionMatrix matrix = ConceptDrugLearning.getMatrix();
		Iterator<Integer> concepts = conceptIds.iterator();
		while (concepts.hasNext()) {

			// get the list of drugsIDs for each conceptId from prediction
			// matrix
			LinkedList<Cell> temp = matrix.getDrugs(concepts.next());
			if (temp != null) {
				Iterator<Cell> drugs = temp.iterator();
				while (drugs.hasNext()) {
					Cell drug = drugs.next();
					Iterator<Cell> resultItr = results.iterator();
					boolean found = false;
					while (resultItr.hasNext()) {
						Cell tmp = resultItr.next();
						if (tmp.equals(drug)) {
							tmp.setConfidence(drug.getConfidence() + tmp.getConfidence()
									+ (drug.getConfidence() * tmp.getConfidence()));
							found = true;
							break;
						}
					}
					if (!found) {
						// add the drugID in the linked list of drugIDs
						results.add(drug);
					}
					// System.out.println(drug);
				}
			}
		}
		Collections.sort(results, new Comparator<Cell>() {

			@Override
			public int compare(Cell o1, Cell o2) {
				if (o1.getConfidence() < o2.getConfidence())
					return -1;
				else if (o1.getConfidence() > o2.getConfidence())
					return 1;
				else
					return 0;
			}
		});

		Iterator<Cell> itr = results.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next().toString());
		}

		// get the linked list of drugs from result drugIDs
		return DrugTableOperation.search(results);
	}

	public static void getAllConceptsInMatrix() {
		PredictionMatrix matrix = ConceptDrugLearning.getMatrix();
		LinkedList<Integer> conceptIds = matrix.getNonEmptyConcepts();
		String str = "( ";
		Iterator<Integer> itr = conceptIds.iterator();
		while (itr.hasNext()) {
			str = str + itr.next() + ", ";
		}
		str = str + ")";
		System.out.println(str);
	}

	public static void main(String[] args) {
		DrugModel[] temp = predict("BLOOD loss");
		for (int i = 0; i < temp.length; i++) {
			System.out.println(temp[i]);
		}

		// getAllConceptsInMatrix();
	}
}