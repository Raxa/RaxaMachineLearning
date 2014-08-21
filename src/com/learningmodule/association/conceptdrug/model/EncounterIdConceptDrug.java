package com.learningmodule.association.conceptdrug.model;

/*
 * class that represent a Encounter with obs(concept id) and Drug(drug id)
 */
public class EncounterIdConceptDrug {
	private int id;
	private String drug, conceptId;
	
	public EncounterIdConceptDrug(int id, String drug, String conceptId) {
		super();
		this.id = id;
		this.drug = drug;
		this.conceptId = conceptId;
	}
	
	public int getEncounterId() {
		return id;
	}
	public void setEncounterId(int id) {
		this.id = id;
	}
	
	public String getDrug() {
		return drug;
	}
	public void setDrug(String drug) {
		this.drug = drug;
	}
	public String getConceptId() {
		return conceptId;
	}
	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}
}
