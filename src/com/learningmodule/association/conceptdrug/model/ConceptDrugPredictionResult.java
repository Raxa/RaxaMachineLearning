package com.learningmodule.association.conceptdrug.model;

import java.util.HashSet;
import java.util.LinkedList;

import com.learningmodule.association.conceptdrug.AbstractDrugModel;
import com.learningmodule.association.conceptdrug.PredictionResult;

/*
 * Class that represent the Object that will be sent as a drug 
 * result for concept drug association algorithm.
 */

public class ConceptDrugPredictionResult implements PredictionResult {
	// contains drug info
	private AbstractDrugModel drug;

	// confidence related to the drug and query
	private double confidence;

	// concepts names for the concepts, drug related to
	private LinkedList<String> tags;

	// conceptsId of concepts drug realted to
	private HashSet<String> conceptIds;

	public ConceptDrugPredictionResult(String drug, double confidence) {
		this.drug = new DrugModel(drug);
		this.confidence = confidence;
		this.conceptIds = new HashSet<String>();
		this.tags = new LinkedList<String>();
	}

	public AbstractDrugModel getDrug() {
		return drug;
	}
	
	@Override
	public Object getValue() {
		return drug;
	}

	public void setDrug(AbstractDrugModel drug) {
		this.drug = drug;
	}

	@Override
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

	public HashSet<String> getConceptIds() {
		return conceptIds;
	}

	/*
	 * method to add concept Ids related to the drug result
	 */
	public void addConceptId(String conceptId) {
		this.conceptIds.add(conceptId);
	}

	public boolean hasConcept(String conceptId) {
		return conceptIds.contains(conceptId);
	}

	@Override
	public String toString() {
		return "PredictionResults [drug=" + drug.toString() + ", confidence=" + confidence + "]";
	}
}
