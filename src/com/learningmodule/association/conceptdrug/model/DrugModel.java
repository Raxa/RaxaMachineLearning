package com.learningmodule.association.conceptdrug.model;

/*
 * class that represent a drug object which will be sent as result of search query
 */
public class DrugModel {
	private String name;
	private String generic;
	private String uuid;
	private int drugCommercialId;
	private int drugId;

	public DrugModel(String name, String generic, String uuid, int drugCommercialId, int drugId) {
		super();
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

	public int getDrugId() {
		return drugId;
	}

	public void setDrugId(int drugId) {
		this.drugId = drugId;
	}
	
	@Override
	public String toString() {
		return "DrugModel [name=" + name + ", generic=" + generic + ", uuid=" + uuid
				+ ", drugCommercialId=" + drugCommercialId + ", drugId=" + drugId + "]";
	}
}
