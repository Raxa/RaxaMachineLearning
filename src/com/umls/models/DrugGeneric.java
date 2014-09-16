package com.umls.models;

import java.util.LinkedList;

/*
 * Class that model the drug generic prediction results
 */

public class DrugGeneric {
	
	// generic of the drug
	private String generic;
	
	// list of UmlsPredictionResult which contains concepts that are drugs with same generic name
	private LinkedList<UmlsPredictionResult> drugs;
	
	// list of concepts that are disease which can be treated by this drug
	private LinkedList<ConceptModel> mayTreat;
	
	// confidence level for this result
	private double confidence;

	public DrugGeneric(String generic) {
		this.generic = generic;
		drugs = new LinkedList<UmlsPredictionResult>();
		mayTreat = new LinkedList<ConceptModel>();
	}

	public String getGeneric() {
		return generic;
	}

	public void setGeneric(String generic) {
		this.generic = generic;
	}

	public void addMayreat(ConceptModel disease) {
		mayTreat.add(disease);
	}
	
	public LinkedList<ConceptModel> mayTreat() {
		return mayTreat;
	}
	
	public LinkedList<UmlsPredictionResult> getDrugs() {
		return drugs;
	}

	public void addDrug(UmlsPredictionResult drug) {
		drugs.add(drug);
	}

	private static double max(double x, double y) {
		if (x >= y)
			return x;
		else
			return y;

	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	@Override
	public String toString() {
		String s = "DrugGeneric [generic=" + generic + ", confidence="
				+ confidence + ", drugs={";
		for(UmlsPredictionResult drug: drugs) {
			s = s + drug + ", ";
		}
		s = s + "}]";
		return s; 
	}
}
