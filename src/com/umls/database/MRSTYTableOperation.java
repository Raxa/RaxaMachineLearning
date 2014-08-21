package com.umls.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.umls.models.SemanticType;

/*
 * Class that implements the method to do operations on MRSTY(table that contain semantics type of concepts)
 */

public class MRSTYTableOperation {
	
	/*
	 * Method to get the semantic type of CUI's in the given list
	 */
	public static LinkedList<SemanticType> getSemanticTypeByCUI(LinkedList<String> CUIs) {
		if (UmlsDatabaseConnector.getConnection() != null) {
			try {
				Statement stat = UmlsDatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT MRSTY.CUI, MRSTY.TUI,"
						+ " MRSTY.STN, MRSTY.STY  FROM umls.MRSTY WHERE MRSTY.CUI IN ("
						+ getINString(CUIs) + " );");

				LinkedList<SemanticType> data = new LinkedList<SemanticType>();
				while (rs.next()) {
					data.add(new SemanticType(rs.getString(1), rs.getString(2), rs.getString(3), rs
							.getString(4)));
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
		} else
			System.out.println("connection lost!");
		return null;
	}

	/*
	 * method to get Semantics Type on those concepts which are drugs
	 */
	public static LinkedList<SemanticType> getDrugsByCUI(LinkedList<String> CUIs) {

		return filterByTUI("T200", getSemanticTypeByCUI(CUIs));

	}

	/*
	 * method to get Semantics Type on those concepts which are Disease or syndrome or symptom
	 */
	public static LinkedList<SemanticType> getDiseaseSyndromSymptomByCUI(LinkedList<String> CUIs) {
		return filterByTUI("T047 T184", getSemanticTypeByCUI(CUIs));
	}

	/*
	 * method to filter the list of semantic types by given TUI or space delimited list of TUI's
	 */
	public static LinkedList<SemanticType> filterByTUI(String TUI, LinkedList<SemanticType> types) {
		LinkedList<SemanticType> results = new LinkedList<SemanticType>();
		
		// split the String TUI's to get array of TUI's that has to filtered
		String[] tuis = TUI.split(" ");
		
		// for each row of concept with semantics types
		for (SemanticType type : types) {
			for (int i = 0; i < tuis.length; i++) {
				// add concept into results if TUI matched with any of given TUI's
				if (type.getTUI().equalsIgnoreCase(tuis[i])) {
					results.add(type);
				}
			}
		}
		return results;
	}

	/*
	 * method to get semantics types with given CUI's and TUI's
	 */
	public static LinkedList<SemanticType> getSemanticTypeByCuiTui(LinkedList<String> CUIs,
			String TUI) {
		if (UmlsDatabaseConnector.getConnection() != null) {
			try {
				Statement stat = UmlsDatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT MRSTY.CUI, MRSTY.TUI,"
						+ " MRSTY.STN, MRSTY.STY  FROM umls.MRSTY WHERE MRSTY.CUI IN ("
						+ getINString(CUIs) + " ) AND MRSTY.TUI = '" + TUI + "';");

				LinkedList<SemanticType> data = new LinkedList<SemanticType>();
				while (rs.next()) {
					data.add(new SemanticType(rs.getString(1), rs.getString(2), rs.getString(3), rs
							.getString(4)));
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
		} else
			System.out.println("connection lost!");
		return null;
	}

	public static String getINString(LinkedList<String> CUIs) {
		String str = "'";
		for (String CUI : CUIs) {
			str = str + CUI + "', '";
		}
		str = str + "0'";
		return str;
	}

	public static void main(String[] args) {
		// LinkedList<String> cuis = MRXNWTableOperation.searchCUI("asthma");
		LinkedList<String> cuis = new LinkedList<String>();
		cuis.add("C0000039");
		LinkedList<SemanticType> list = getSemanticTypeByCUI(cuis);
		System.out.println(list.isEmpty());
		for (SemanticType item : list) {
			System.out.println(item);
		}
	}
}
