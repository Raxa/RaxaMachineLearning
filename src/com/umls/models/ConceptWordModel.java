package com.umls.models;

/*
 * Class that model the Concept Word
 */

public class ConceptWordModel {
	
	// ConceptId(CUI)
	private String concept;
	
	// word for concept
	private String word;

	public ConceptWordModel(String concept, String word) {
		super();
		this.concept = concept;
		this.word = word;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
}
