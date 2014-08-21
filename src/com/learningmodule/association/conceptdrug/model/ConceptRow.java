package com.learningmodule.association.conceptdrug.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * Class that represent the row of Prediction metrix
 */

public class ConceptRow implements Serializable {

	private static final long serialVersionUID = 1L;

	// linkedlist of cells contains drugId and confidence
	private LinkedList<Cell> cellList = new LinkedList<Cell>();
	
	// conceptId
	private String concept;

	public ConceptRow(String concept) {
		this.concept = concept;
	}

	public String getConcept() {
		return concept;
	}

	public void addCell(String drug, double confidence) {
		Cell cell = getCellWithId(drug);
		if(cell != null) {
			double conf = cell.getConfidence();
			cell.setConfidence(conf + confidence - (conf*confidence));
		}
		else cellList.add(new Cell(drug, confidence));
	}
	
	public Cell getCellWithId(String drug) {
		for(Cell cell: cellList) {
			
			if(cell.getDrug().equals(drug)) {
				return cell;
			}
		}
		return null;
	}
	
	public LinkedList<Cell> getDrugs() {
		LinkedList<Cell> drugs = new LinkedList<Cell>();
		for(Cell drug: cellList) {
			drugs.add(new Cell(drug.getDrug(), drug.getConfidence()));
		}
		return drugs;
	}

	@Override
	public String toString() {
		String result = "";
		result = concept + " ==> ";
		Iterator<Cell> itr = cellList.iterator();
		while (itr.hasNext()) {
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
		private String drug;
		private double confidence;

		public Cell(String drug, double confidence) {
			this.drug = drug;
			this.confidence = confidence;
		}

		public String getDrug() {
			return drug;
		}

		public double getConfidence() {
			return confidence;
		}
		
		public void setConfidence(double confidence) {
			this.confidence = confidence;
		}

		@Override
		public String toString() {
			return "Cell [drug=" + drug + ", confidence=" + confidence + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (drug.equals(other.drug))
				return false;
			return true;
		}

	}
}