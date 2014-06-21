package com.learningmodule.association.conceptdrug.model;

import java.util.HashSet;
import java.util.LinkedList;

/*
 * Class that represent the Object that will be sent as a drug 
 * result for concept drug association algorithm.
 */

public class PredictionResults {
	// contains drug info
	private DrugModel drug;

	// confidence related to the drug and query
	private double confidence;

	// concepts names for the concepts, drug related to
	private LinkedList<String> tags;

	// conceptsId of concepts drug realted to
	private HashSet<Integer> conceptIds;

	public PredictionResults(int drug, double confidence) {
		this.drug = new DrugModel(drug);
		this.confidence = confidence;
		this.conceptIds = new HashSet<Integer>();
		this.tags = new LinkedList<String>();
	}

	public DrugModel getDrug() {
		return drug;
	}

	public void setDrug(DrugModel drug) {
		this.drug = drug;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public LinkedList<String> getTags() {
		return tags;
	}

	/*
	 * method to add a tag for drug result
	 */
	public void addTags(String tag) {
		this.tags.add(tag);
	}

	public HashSet<Integer> getConceptIds() {
		return conceptIds;
	}

	/*
	 * method to add concept Ids related to the drug result
	 */
	public void addConceptId(Integer conceptId) {
		this.conceptIds.add(conceptId);
	}

	public boolean hasConcept(Integer conceptId) {
		return conceptIds.contains(conceptId);
	}

	@Override
	public String toString() {
		return "PredictionResults [drug=" + drug.toString() + ", confidence=" + confidence + "]";
	}
}