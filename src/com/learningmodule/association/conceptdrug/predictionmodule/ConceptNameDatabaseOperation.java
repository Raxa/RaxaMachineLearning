package com.learningmodule.association.conceptdrug.predictionmodule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.database.DatabaseConnector;
import com.learningmodule.association.conceptdrug.model.PredictionResults;

/*
 * class that implements methods operating on table concept_name in OpenMRS
 */

public class ConceptNameDatabaseOperation {

	// method to get the list of tags for each drug result
	public static LinkedList<PredictionResults> getTags(LinkedList<PredictionResults> results) {
		// check if database is connected
		if (DatabaseConnector.getConnection() != null) {
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query to get the list of concepts names of
				// all concepts in drug results
				ResultSet rs = stat.executeQuery("SELECT concept_name.concept_id,"
						+ " concept_name.name from openmrs.concept_name"
						+ " where concept_name.concept_id in ( " + getIdsString(results)
						+ ") and locale = 'en';");

				// for ever row of concept name and concept Id
				while (rs.next()) {

					// for every drug result check if conceptId is present in
					// list of concepts related to it
					for (PredictionResults result : results) {
						if (result.hasConcept(rs.getInt(1))) {
							// add the concept name is concept Id is related to
							// drug
							result.addTags(rs.getString(2));
						}
					}
				}
				return results;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.println("connection lost!");
		}
		return null;
	}

	/*
	 * method to get the string that has to be put in SQL query which has all
	 * conceptId saparted by comma
	 */
	private static String getIdsString(LinkedList<PredictionResults> results) {
		String str = "";
		for (PredictionResults result : results) {
			for (Integer id : result.getConceptIds()) {
				str = str + id + ", ";
			}
		}
		str = str + "0 ";
		return str;
	}
}
