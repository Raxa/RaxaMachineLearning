package com.learningmodule.association.conceptdrug.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * Class that represent the row of Prediction metrix
 */

public class ConceptRow implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// each row contain conceptId and a linkedlist of cells
	private LinkedList<Cell> cellList = new LinkedList<Cell>();
	private int concept;
	
	public ConceptRow(int concept) {
		this.concept = concept;
	}
	
	public int getConcept() {
		return concept;
	}
	
	public void addCell(int drug, double confidence) {
		cellList.add(new Cell(drug, confidence));
	}
	
	public LinkedList<Cell> getDrugs() {
		return cellList;
	}
	
	@Override
	public String toString() {
		String result = "";
		result = concept + " ==> ";
		Iterator<Cell> itr = cellList.iterator();
		while(itr.hasNext()) {
			Cell temp = itr.next();
			result = result + "(" + temp.getDrug() + ", " + temp.getConfidence() + " )";
		}
		return result;
	}
	
	/*
	 * class that represent a cell of predection matrix
	 */
	public class Cell implements Serializable {
		private static final long serialVersionUID = 1L;
		
		// each cell contains drug Id and confidence level
		private int drug;
		private double confidence;
		
		public Cell(int drug, double confidence) {
			this.drug = drug;
			this.confidence = confidence;
		}
		
		public int getDrug() {
			return drug;
		}
		
		public double getConfidence() {
			return confidence;
		}
	}
}