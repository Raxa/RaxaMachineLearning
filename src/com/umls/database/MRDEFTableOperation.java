package com.umls.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.umls.models.ConceptDefination;

/*
 * Class that implements method to do operations on MRDEF(table of concept definitions) in umls
 */

public class MRDEFTableOperation {
	
	/*
	 * method to get the shortest definition for given CUI
	 */
	public static ConceptDefination getDefByCUI(String cui, String name) {
		if (UmlsDatabaseConnector.getConnection() != null) {
			try {
				Statement stat = UmlsDatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT MRDEF.CUI, MRDEF.SAB, MRDEF.DEF"
						+ " FROM umls.MRDEF where MRDEF.CUI = '" + cui + "';");

				ConceptDefination data = null; 
				while (rs.next()) {
					if(data == null || data.getDefination().length() > rs.getString(3).length()) {
						data = new ConceptDefination(rs.getString(1), rs.getString(2),
								rs.getString(3), name);
					}
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
		} else
			System.out.println("connection lost!");
		return null;
	}
	public static void main(String[] args) {
		System.out.println(getDefByCUI("C0004096", "name"));
	}
}
