package com.learningmodule.association.conceptdrug.model;

import com.learningmodule.association.conceptdrug.AbstractDrugModel;

/*
 * class that represent a drug object which will be sent as result of search query
 */
public class DrugModel implements AbstractDrugModel {

	// name of drug
	private String name;

	// generic od drug
	private String generic;

	// uuid of drug
	private String uuid;

	// commercial Id of drug
	private int drugCommercialId;

	// drug Id
	private int drugId;

	public DrugModel(int drugId) {
		this.drugId = drugId;
	}

	public DrugModel(String name, String generic, String uuid, int drugCommercialId, int drugId) {
		this.name = name;
		this.generic = generic;
		this.uuid = uuid;
		this.drugCommercialId = drugCommercialId;
		this.drugId = drugId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGeneric() {
		return generic;
	}

	public void setGeneric(String generic) {
		this.generic = generic;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getDrugCommercialId() {
		return drugCommercialId;
	}

	public void setDrugCommercialId(int drugCommercialId) {
		this.drugCommercialId = drugCommercialId;
	}

	@Override
	public int getDrugId() {
		return drugId;
	}

	@Override
	public void setDrugId(int drugId) {
		this.drugId = drugId;
	}

	@Override
	public String toString() {
		return "DrugModel [name=" + name + ", generic=" + generic + ", uuid=" + uuid
				+ ", drugCommercialId=" + drugCommercialId + ", drugId=" + drugId + "]";
	}
}
