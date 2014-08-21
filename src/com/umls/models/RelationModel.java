package com.umls.models;

/*
 * class that implements the relation model
 */

public class RelationModel {
	
	// CUI
	private String CUI;
	
	// CUI of concept related
	private String relatedCUI;
	
	// relation label
	private String relationLable;
	
	// relation attribute
	private String relationAttribute;

	public RelationModel(String cUI, String relatedCUI, String relationLable,
			String relationAttribute) {
		CUI = cUI;
		this.relatedCUI = relatedCUI;
		this.relationLable = relationLable;
		this.relationAttribute = relationAttribute;
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

	public String getRelationLable() {
		return relationLable;
	}

	public void setRelationLable(String relationLable) {
		this.relationLable = relationLable;
	}

	public String getRelationAttribute() {
		return relationAttribute;
	}

	public void setRelationAttribute(String relationAttribute) {
		this.relationAttribute = relationAttribute;
	}

	@Override
	public String toString() {
		return "MRRELTableOperation [CUI=" + CUI + ", relatedCUI=" + relatedCUI
				+ ", relationLable=" + relationLable + ", relationAttribute=" + relationAttribute
				+ "]";
	}
}
