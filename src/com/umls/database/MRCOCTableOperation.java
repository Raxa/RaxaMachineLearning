package com.umls.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.umls.models.CoOccurrenceModel;

/*
 * class to get the list of concepts Co-Occurring with given List of CUI's
 */

public class MRCOCTableOperation {

	public static LinkedList<CoOccurrenceModel> getCoOccurrneceByCUI(LinkedList<String> CUIs) {
		if (UmlsDatabaseConnector.getConnection() != null) {
			try {
				Statement stat = UmlsDatabaseConnector.getConnection().createStatement();
				// SQL query to get the concepts cooccuring with given list of cuis
				ResultSet rs = stat.executeQuery("SELECT  MRCOC.CUI1 AS CUI , MRCOC.CUI2 AS RCUI "
						+ "FROM umls.MRCOC WHERE MRCOC.CUI1 In (" + getINString(CUIs)
						+ " ) UNION SELECT  MRCOC.CUI2 AS CUI ,  MRCOC.CUI1 AS RCUI " 
						+ "FROM  umls.MRCOC WHERE MRCOC.CUI2 IN (" + getINString(CUIs) 
						+ " )");

				LinkedList<CoOccurrenceModel> data = new LinkedList<CoOccurrenceModel>();
				while (rs.next()) {
					data.add(new CoOccurrenceModel(rs.getString(1), rs.getString(2)));
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
		LinkedList<CoOccurrenceModel> list = getCoOccurrneceByCUI(cuis);
		System.out.println(list.isEmpty());
		for (CoOccurrenceModel item : list) {
			System.out.println(item);
		}
	}
}
