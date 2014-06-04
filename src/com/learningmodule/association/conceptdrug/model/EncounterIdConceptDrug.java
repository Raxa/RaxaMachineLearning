package com.learningmodule.association.conceptdrug.model;

/*
 * class that represent a Encounter with obs(concept id) and Drug(drug id)
 */
public class EncounterIdConceptDrug {
	private int id;
	private int drug, conceptId;
	
	public EncounterIdConceptDrug(int id, int drug, int conceptId) {
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
	
	public int getDrug() {
		return drug;
	}
	public void setDrug(int drug) {
		this.drug = drug;
	}
	public int getConceptId() {
		return conceptId;
	}
	public void setConceptId(int conceptId) {
		this.conceptId = conceptId;
	}
}
