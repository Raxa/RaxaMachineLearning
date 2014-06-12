package com.learningmodule.association.conceptdrug.predictionmodule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

import com.database.DatabaseConnector;
import com.learningmodule.association.conceptdrug.model.ConceptRow;
import com.learningmodule.association.conceptdrug.model.ConceptRow.Cell;
import com.learningmodule.association.conceptdrug.model.DrugModel;

/*
 * class to fetch the drug data from database
 */

public class DrugTableOperation {

	// method to get the list of Drugs details from database of given drugIDs
	public static DrugModel[] search(LinkedList<Cell> drugs) {

		DrugModel data[] = new DrugModel[drugs.size()];
		if (DatabaseConnector.getConnection() != null && !drugs.isEmpty()) {
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT drug.drug_id, "
						+ "drug.name, drug.generic_name, drug.uuid, drug.drug_commercial_id "
						+ "from openmrs.drug where drug.drug_id in ( " + getWords(drugs) + ");");

				int idx = 0;
				// create a linked list of Drugs
				while (rs.next()) {
					idx = findIndexOfDrugIdInSortedList(drugs, rs.getInt(1));
					if (idx != -1) {
						data[idx] = new DrugModel(rs.getString(2), rs.getString(3),
								rs.getString(4), rs.getInt(5), rs.getInt(1));
					}
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.println("connection lost! OR list of Drug IDs empty = " + drugs.isEmpty());
		}
		return data;
	}

	private static String getWords(LinkedList<Cell> drugs) {
		Iterator<Cell> id = drugs.iterator();
		String str = " '" + id.next().getDrug() + "'";
		while (id.hasNext()) {
			str = str + ", '" + id.next().getDrug() + "'";
		}
		return str;
	}

	private static int findIndexOfDrugIdInSortedList(LinkedList<Cell> drugs, int drugId) {
		int count = 0;
		Iterator<Cell> itr = drugs.iterator();
		while (itr.hasNext()) {
			if (itr.next().getDrug() == drugId) {
				return count;
			}
			count++;
		}
		return -1;
	}

	public static void main(String[] args) {
		ConceptRow row = new ConceptRow(1);
		row.addCell(3000463, 0.8);
		row.addCell(3000472, 0.7);
		DrugModel[] tmp = search(row.getDrugs());

		for (int i = 0; i < tmp.length; i++) {
			System.out.println(tmp[i]);
		}
	}
}
