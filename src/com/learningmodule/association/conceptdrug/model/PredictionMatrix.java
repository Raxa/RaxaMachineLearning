package com.learningmodule.association.conceptdrug.model;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import com.learningmodule.association.conceptdrug.model.ConceptRow.Cell;

/*
 * Class that represent he resultant matrix that will be used to predict the drug for a concept
 */
public class PredictionMatrix implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Prediction matrix is a hastable with key as concept_id and value as
	// ConceptRow
	Hashtable<String, ConceptRow> rows;

	public PredictionMatrix(int noOfConcepts) {
		rows = new Hashtable<String, ConceptRow>();
	}

	/*
	 * method to get the list of drugs that are associated with a drugId
	 */
	public LinkedList<Cell> getDrugs(String conceptId) {
		// if conceptId is present in the hash table then get the list of drugs
		// related to it
		if (rows.containsKey(conceptId)) {
			return rows.get(conceptId).getDrugs();
		}
		return null;
	}

	/*
	 * method to get all the concepts which are related to some drug
	 */
	public LinkedList<String> getNonEmptyConcepts() {
		Enumeration<String> temp = rows.keys();
		LinkedList<String> result = new LinkedList<String>();
		while (temp.hasMoreElements()) {
			String key = temp.nextElement();
			if (!getDrugs(key).isEmpty()) {
				result.add(key);
			}
		}
		return result;
	}

	/*
	 * method to add a new cell to Prediction matrix
	 */
	public void addCell(String concept, String drug, double conf) {
		// if concept is already present in hast table put a new key value pair
		// with key as this concept
		if (!rows.containsKey(concept)) {
			rows.put(concept, new ConceptRow(concept));
		}
		// add the drug in conceptRow
		rows.get(concept).addCell(drug, conf);
	}

	@Override
	public String toString() {
		String result = "";
		int count = 0;
		Enumeration<String> keys = rows.keys();
		while (keys.hasMoreElements()) {
			result = result + rows.get(keys.nextElement()).toString() + "\n";
			count++;
		}
		return result + count;
	}

}
