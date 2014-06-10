package com.learningmodule.association.conceptdrug.predictionmodule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

import com.database.DatabaseConnector;

/*
 * class to get list concept IDs for given word from the database
 */

public class ConceptWordSearchDatabase {

	// method to get the list of concept IDs for given search query
	public static LinkedList<Integer> search(String query) {
		if (DatabaseConnector.getConnection() != null) {
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT concept_word.concept_id "
						+ "FROM openmrs.concept_word WHERE concept_word.word IN ( "
						+ getWords(query) + ") GROUP BY concept_word.concept_id "
						+ "ORDER BY count(concept_word.concept_word_id) desc,"
						+ " sum(concept_word.weight) DESC;");

				// create a list of ConceptIDs
				LinkedList<Integer> data = new LinkedList<Integer>();
				while (rs.next()) {
					data.add(rs.getInt(1));
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.println("connection lost!");
		}
		return null;
	}

	private static String getWords(String query) {
		String[] words = query.split(" ");
		String str = "'" + words[0] + "'";
		for (int i = 1; i < words.length; i++) {
			str = str + ", '" + words[i] + "'";
		}
		return str;
	}

	public static void main(String[] args) {
		Iterator<Integer> it = search("ASTHMA NOS").iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
