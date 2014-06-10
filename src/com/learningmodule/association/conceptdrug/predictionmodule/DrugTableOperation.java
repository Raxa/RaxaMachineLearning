package com.learningmodule.association.conceptdrug.predictionmodule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

import com.database.DatabaseConnector;
import com.learningmodule.association.conceptdrug.model.DrugModel;

/*
 * class to fetch the drug data from database
 */

public class DrugTableOperation {

	// method to get the list of Drugs details from database of given drugIDs
	public static LinkedList<DrugModel> search(LinkedList<Integer> drugIds) {
		LinkedList<DrugModel> data = new LinkedList<DrugModel>();
		if (DatabaseConnector.getConnection() != null && !drugIds.isEmpty()) {
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT drug.drug_id, "
						+ "drug.name, drug.generic_name, drug.uuid, drug.drug_commercial_id "
						+ "from openmrs.drug where drug.drug_id in ( " + getWords(drugIds) + ");");

				// create a linked list of Drugs

				while (rs.next()) {
					data.add(new DrugModel(rs.getString(2), rs.getString(3), rs.getString(4), rs
							.getInt(5), rs.getInt(1)));
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.println("connection lost! OR list of Drug IDs empty = " + drugIds.isEmpty());
		}
		return data;
	}

	private static String getWords(LinkedList<Integer> drugIds) {
		Iterator<Integer> id = drugIds.iterator();
		String str = " '" + id.next() + "'";
		while (id.hasNext()) {
			str = str + ", '" + id.next() + "'";
		}
		return str;
	}

	public static void main(String[] args) {
		LinkedList<Integer> drugIds = new LinkedList<Integer>();
		drugIds.add(3000463);
		LinkedList<DrugModel> tmp = search(drugIds);
		Iterator<DrugModel> itr = tmp.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
}
