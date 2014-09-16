package com.umls.models;

/*
 * Class that models the Concept Definition
 */

public class ConceptDefination {
	
	// cui of concept
	private String CUI;
	
	// source of definition
	private String source;
	
	// defination
	private String defination;
	
	// prefered name of concept
	private String name;

	public ConceptDefination(String cUI, String source, String defination, String name) {
		super();
		CUI = cUI;
		this.source = source;
		this.defination = defination;
		this.name = name;
	}

	public String getCUI() {
		return CUI;
	}

	public void setCUI(String cUI) {
		CUI = cUI;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDefination() {
		return defination;
	}

	public void setDefination(String defination) {
		this.defination = defination;
	}

	@Override
	public String toString() {
		return "ConceptDefination [CUI=" + CUI + ", source=" + source + ", defination="
				+ defination + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
