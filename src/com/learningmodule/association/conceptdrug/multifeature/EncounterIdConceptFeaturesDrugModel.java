package com.learningmodule.association.conceptdrug.multifeature;

import java.util.Arrays;

/*
 * Class that models medical records
 */

public class EncounterIdConceptFeaturesDrugModel {
	
	// encounter id of medical record
	private int id;
	
	// conceptId(disease/diagnosis)
	private String conceptId;
	
	// drug prescribed
	private String drugId;
	
	// other feature related to record like age, location of patient etc.
	private FeatureValue[] featuresValues;
	
	// number of feature;
	private int noOfFeatures;

	public EncounterIdConceptFeaturesDrugModel(int id, String conceptId, String drugId,
			FeatureValue[] featuresValues) {
		super();
		this.id = id;
		this.conceptId = conceptId;
		this.drugId = drugId;
		this.featuresValues = featuresValues;
		noOfFeatures = featuresValues.length;
	}

	public EncounterIdConceptFeaturesDrugModel(int id, String conceptId, String drugId,
			int noOfFeatures) {
		super();
		this.id = id;
		this.conceptId = conceptId;
		this.drugId = drugId;
		this.noOfFeatures = noOfFeatures;
		featuresValues = new FeatureValue[noOfFeatures];
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConceptId() {
		return conceptId;
	}

	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}

	public String getDrugId() {
		return drugId;
	}

	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}

	public FeatureValue[] getFeatures() {
		return featuresValues;
	}

	public void setFeatures(FeatureValue[] featuresValues) {
		this.featuresValues = featuresValues;
	}

	public int getNoOfFeatures() {
		return noOfFeatures;
	}

	@Override
	public String toString() {
		return "EncounterIdConceptFeaturesDrugModel [id=" + id + ", conceptId=" + conceptId
				+ ", drugId=" + drugId + ", featuresValues=" + Arrays.toString(featuresValues)
				+ ", noOfFeatures=" + noOfFeatures + "]";
	}
}
