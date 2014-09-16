package com.learningmodule.association.conceptdrug.model;

public class ConceptNameModel {

	private String conceptId;
	private String conceptName;

	public ConceptNameModel(String conceptId, String conceptName) {
		super();
		this.conceptId = conceptId;
		this.conceptName = conceptName;
	}

	public String getConceptId() {
		return conceptId;
	}

	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
}
