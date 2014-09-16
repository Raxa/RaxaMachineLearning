package com.umls.models;

/*
 * class that models the Semantic types
 */

public class SemanticType {

	String CUI;
	
	// Semantic type unique Identifier
	String TUI;
	
	// semantic type tree number
	String STN;
	
	// name of semantic type
	String STY;

	public SemanticType(String cUI, String tUI, String sTN, String sTY) {
		super();
		CUI = cUI;
		TUI = tUI;
		STN = sTN;
		STY = sTY;
	}

	public String getCUI() {
		return CUI;
	}

	public void setCUI(String cUI) {
		CUI = cUI;
	}

	public String getTUI() {
		return TUI;
	}

	public void setTUI(String tUI) {
		TUI = tUI;
	}

	public String getSTN() {
		return STN;
	}

	public void setSTN(String sTN) {
		STN = sTN;
	}

	public String getSTY() {
		return STY;
	}

	public void setSTY(String sTY) {
		STY = sTY;
	}

	@Override
	public String toString() {
		return "SymenticType [CUI=" + CUI + ", TUI=" + TUI + ", STN=" + STN + ", STY=" + STY + "]";
	}

}
