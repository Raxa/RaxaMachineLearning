package com.umls.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.umls.models.RelationModel;

/*
 * Class the implements method to do operations on MRREL(table that contains concept relations) in umls
 */

public class MRRELTableOperation {
	
	/*
	 * method to get the concept related to the given list of cui's
	 */
	public static LinkedList<RelationModel> getRelatedConceptsByCUI(LinkedList<String> CUIs) {
		if (UmlsDatabaseConnector.getConnection() != null) {
			try {
				Statement stat = UmlsDatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT MRREL.CUI1, MRREL.CUI2, MRREL.REL"
						+ ", MRREL.RELA FROM umls.MRREL WHERE MRREL.CUI1" + " IN ( "
						+ getINString(CUIs) + ") AND MRREL.CUI2 != MRREL.CUI1");

				LinkedList<RelationModel> data = new LinkedList<RelationModel>();
				while (rs.next()) {
					data.add(new RelationModel(rs.getString(1), rs.getString(2), rs.getString(3),
							rs.getString(4)));
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
		LinkedList<String> cuis = new LinkedList<String>();
		cuis.add("C0000039");
		LinkedList<RelationModel> list = getRelatedConceptsByCUI(cuis);
		System.out.println(list.isEmpty());
		for (RelationModel item : list) {
			System.out.println(item);
		}
	}
}
