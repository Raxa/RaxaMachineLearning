package com.learningmodule.association.conceptdrug.model;

/*
 * Model for ConceptId and Concept word pair.
 */

public class ConceptWordModel {
	private Integer conceptId;
	private String conceptWord;

	public ConceptWordModel(Integer conceptId, String conceptWord) {
		super();
		this.conceptId = conceptId;
		this.conceptWord = conceptWord;
	}

	public Integer getConceptId() {
		return conceptId;
	}

	public String getConceptWord() {
		return conceptWord;
	}
}
