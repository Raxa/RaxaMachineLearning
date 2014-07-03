package com.learningmodule.association.conceptdrug;

/*
 * Interface that a Object containg drugInfo should implement
 */

public interface AbstractDrugModel {
	
	/*
	 * set the Drug Id for this drug
	 */
	public void setDrugId(int id);

	/*
	 * get the drug Id for this drug
	 */
	public int getDrugId();
}
