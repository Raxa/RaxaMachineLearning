package com.learningmodule.association.conceptdrug.model;

/*
 * Model for ConceptId and Concept word pair.
 */

public class ConceptWordModel {
	private String conceptId;
	private String conceptWord;

	public ConceptWordModel(String conceptId, String conceptWord) {
		super();
		this.conceptId = conceptId;
		this.conceptWord = conceptWord;
	}

	public String getConceptId() {
		return conceptId;
	}

	public String getConceptWord() {
		return conceptWord;
	}
}
