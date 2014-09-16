package com.umls.models;

/*
 * Class to model co-occurrence relationships
 */

public class CoOccurrenceModel {
	
	// concept Id
	private String CUI;
	
	// conceptId of co-occurring concept
	private String relatedCUI;

	public CoOccurrenceModel(String cUI, String relatedCUI) {
		super();
		CUI = cUI;
		this.relatedCUI = relatedCUI;
	}

	public String getCUI() {
		return CUI;
	}

	public void setCUI(String cUI) {
		CUI = cUI;
	}

	public String getRelatedCUI() {
		return relatedCUI;
	}

	public void setRelatedCUI(String relatedCUI) {
		this.relatedCUI = relatedCUI;
	}

	@Override
	public String toString() {
		return "CoOccurrenceModel [CUI=" + CUI + ", relatedCUI=" + relatedCUI + "]";
	}
}
