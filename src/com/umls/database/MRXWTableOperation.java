package com.umls.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.umls.models.ConceptWordModel;

/*
 * Class that implements the method for Operation on table MRXW_ENG(table with English word and CUI's)
 */

public class MRXWTableOperation {
	
	/*
	 * method to get the list of CUI's for given query
	 */
	public static LinkedList<String> searchCUI(String query) {
		if (UmlsDatabaseConnector.getConnection() != null) {
			try {
				Statement stat = UmlsDatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT MRXW_ENG.CUI FROM umls.MRXW_ENG WHERE"
						+ " MRXW_ENG.WD In (" + getWords(query) + ") group by MRXW_ENG.CUI;");

				// create a linked list of objects with EncounterId, drugId,
				// ConceptId(observation)
				LinkedList<String> data = new LinkedList<String>();
				while (rs.next()) {
					data.add(rs.getString(1));
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
		} else
			System.out.println("connection lost!");
		return null;
	}

	public static LinkedList<ConceptWordModel> getWordsByLimit(int start, int size) {
		if (UmlsDatabaseConnector.getConnection() != null) {
			System.out.println("You made it, take control your database now!");
			try {
				Statement stat = UmlsDatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT MRXW_ENG.CUI, MRXW_ENG.WD FROM "
						+ "umls.MRXW_ENG LIMIT " + start + " ," + size + ";");

				// create a linked list of objects with EncounterId, drugId,
				// ConceptId(observation)
				LinkedList<ConceptWordModel> data = new LinkedList<ConceptWordModel>();
				while (rs.next()) {
					data.add(new ConceptWordModel(rs.getString(1), rs.getString(2)));
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
		} else
			System.out.println("connection lost!");
		return null;
	}

	public static String getWords(String query) {
		String[] temp = query.split(" ");
		String str = "'" + temp[0];
		for (int i = 1; i < temp.length; i++) {
			str = str + "', '" + temp[i];
		}
		return str + "'";
	}

	public static void main(String[] args) {
		LinkedList<String> r = searchCUI("asthma");
		for (String s : r) {
			System.out.println(s);
		}
	}
}
