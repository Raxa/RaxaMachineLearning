package com.learningmodule.association.conceptdrug.predictionmodule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.database.DatabaseConnector;
import com.learningmodule.association.conceptdrug.model.DrugModel;
import com.learningmodule.association.conceptdrug.model.PredictionResults;

/*
 * class to fetch the drug data from database
 */

public class DrugTableOperation {

	// method to get the list of Drugs details from database of given drugIDs
	public static LinkedList<PredictionResults> search(LinkedList<PredictionResults> drugs) {

		if (DatabaseConnector.getConnection() != null && !drugs.isEmpty()) {
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query to get drug related info for given drug
				// Ids
				ResultSet rs = stat.executeQuery("SELECT drug.drug_id, "
						+ "drug.name, drug.generic_name, drug.uuid, drug.drug_commercial_id "
						+ "from openmrs.drug where drug.drug_id in ( " + getWords(drugs) + ");");
				// for every drugId from results of query
				while (rs.next()) {
					
					// search for drugId in drug results
					for (PredictionResults drug : drugs) {
						if (drug.getDrug().getDrugId() == rs.getInt(1)) {
							
							// set the drug info in the drug result
							drug.setDrug(new DrugModel(rs.getString(2), rs.getString(3), rs
									.getString(4), rs.getInt(5), rs.getInt(1)));
						}
					}
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.println("connection lost! OR list of Drug IDs empty = " + drugs.isEmpty());
		}
		return drugs;
	}

	// create the string of drugIds separated by comma that will put in sql query 
	private static String getWords(LinkedList<PredictionResults> drugs) {

		String str = "";
		for (PredictionResults drug : drugs) {
			str = str + drug.getDrug().getDrugId() + ", ";
		}
		str = str + "0";
		return str;
	}

	public static void main(String[] args) {
		LinkedList<PredictionResults> rows = new LinkedList<>();
		rows.add(new PredictionResults(3000463, 0.8));
		rows.add(new PredictionResults(3000472, 0.7));
		rows = search(rows);
		for (PredictionResults row : rows) {
			System.out.println(row.toString());
		}
	}
}
