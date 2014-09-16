package com.umls.models;

public class MRDOCModel {
	private String dockey;
	private String value;
	private String type;
	private String expl;

	public MRDOCModel(String dockey, String value, String type, String expl) {
		this.dockey = dockey;
		this.value = value;
		this.type = type;
		this.expl = expl;
	}

	public String getDockey() {
		return dockey;
	}

	public void setDockey(String dockey) {
		this.dockey = dockey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExpl() {
		return expl;
	}

	public void setExpl(String expl) {
		this.expl = expl;
	}

	@Override
	public String toString() {
		return "MRDOCModel [dockey=" + dockey + ", value=" + value + ", type=" + type + ", expl="
				+ expl + "]";
	}

}
