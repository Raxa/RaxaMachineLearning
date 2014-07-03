package com.learningmodule.association.conceptdrug.model;

public class ConceptNameModel {

	private Integer conceptId;
	private String conceptName;

	public ConceptNameModel(Integer conceptId, String conceptName) {
		super();
		this.conceptId = conceptId;
		this.conceptName = conceptName;
	}

	public Integer getConceptId() {
		return conceptId;
	}

	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
}
