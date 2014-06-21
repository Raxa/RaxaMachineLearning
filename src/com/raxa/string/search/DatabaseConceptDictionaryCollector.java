package com.raxa.string.search;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.database.DatabaseConnector;

/*
 * Class that implements the method to get the all the concept words and respective conceptId's from database
 */

public class DatabaseConceptDictionaryCollector {
	public static LinkedList<ConceptWordModel> getConceptWords() {
		if (DatabaseConnector.getConnection() != null) {

			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query get all the row from concept_word table
				ResultSet rs = stat.executeQuery("SELECT concept_word.word, concept_word.concept_id"
						+ " FROM openmrs.concept_word;");

				// create a linked list of ConceptWordModel object
				LinkedList<ConceptWordModel> data = new LinkedList<ConceptWordModel>();
				while (rs.next()) {
					data.add(new ConceptWordModel( rs.getInt(2), rs.getString(1)));
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else
			System.out.println("connection lost!");
		return null;
	}
	
	public static void main(String args[]) {
		System.out.println(getConceptWords().size());
		DatabaseConnector.closeConnection();
	}
}
