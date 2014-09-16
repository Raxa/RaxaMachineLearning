package com.umls.models;

import java.util.LinkedList;

/*
 * class that models Results for a search query
 */

public class UmlsResult {
	
	// other suggestions that are diseases
	private LinkedList<UmlsPredictionResult> disease;
	
	// drug predictions
	private LinkedList<DrugGeneric> drugs;
	
	// definition of disease in search query
	private ConceptDefination disDef;

	public UmlsResult(LinkedList<UmlsPredictionResult> disease, LinkedList<DrugGeneric> drugs) {
		this.disease = disease;
		this.drugs = drugs;
	}

	public LinkedList<UmlsPredictionResult> getDisease() {
		return disease;
	}

	public void setDisease(LinkedList<UmlsPredictionResult> disease) {
		this.disease = disease;
	}

	public LinkedList<DrugGeneric> getDrugs() {
		return drugs;
	}

	public void setDrugs(LinkedList<DrugGeneric> drugs) {
		this.drugs = drugs;
	}

	public ConceptDefination getDisDef() {
		return disDef;
	}

	public void setDisDef(ConceptDefination disDef) {
		this.disDef = disDef;
	}
}
