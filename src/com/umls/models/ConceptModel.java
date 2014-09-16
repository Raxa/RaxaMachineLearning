package com.umls.models;

/*
 * Class that Model the Concept
 */

public class ConceptModel {
	
	// concept Id
	private String CUI;
	
	// concept Name
	private String name;
	
	// semantic type of concepts
	private SemanticType semanticType;
	
	// source of concept
	private String source;
	
	// Term Type in Source
	private String TTY;

	// code for concept in source
	private String code;

	public ConceptModel(String cUI, String name, String source,
			String tTY, String code) {
		CUI = cUI;
		this.name = name;
		this.source = source;
		TTY = tTY;
		this.code = code;
	}

	public String getCUI() {
		return CUI;
	}

	public void setCUI(String cUI) {
		CUI = cUI;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SemanticType getSemanticTypes() {
		return semanticType;
	}

	public void setSemanticTypes(SemanticType semanticTypes) {
		this.semanticType = semanticTypes;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTTY() {
		return TTY;
	}

	public void setTTY(String tTY) {
		TTY = tTY;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ConceptModel [CUI=" + CUI + ", name=" + name + ", symenticTypes=" + semanticType
				+ ", source=" + source + ", TTY=" + TTY + ", code=" + code + "]";
	}

}
