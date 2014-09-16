package com.umls.models;

/*
 * Class that model the predictions results from umls.
 */

public class UmlsPredictionResult {
	
	// confidence level for this result
	private double confidence;
	
	// Drug(which is a umls concept) result
	private ConceptModel value;

	public UmlsPredictionResult(double confidence, ConceptModel value) {
		this.confidence = confidence;
		this.value = value;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public ConceptModel getValue() {
		return value;
	}

	public void setValue(ConceptModel value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "UmlsPredictionResult [confidence=" + confidence + ", value=" + value + "]";
	}

}
