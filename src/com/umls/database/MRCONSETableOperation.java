package com.umls.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.umls.models.ConceptModel;

/*
 * Class that implement methods to perform operations on table MRCONSE of UMls
 */

public class MRCONSETableOperation {

	/*
	 * method to get the concept Info for given list of CUI's(concept Id's)
	 */
	public static LinkedList<ConceptModel> getConceptByCUI(LinkedList<String> CUIs) {
		if (UmlsDatabaseConnector.getConnection() != null) {
			try {
				Statement stat = UmlsDatabaseConnector.getConnection().createStatement();
				// execute the SQL query to get concept information for given
				// list of concepts
				ResultSet rs = stat.executeQuery("SELECT MRCONSO.CUI, MRCONSO.STR,"
						+ " MRCONSO.SAB, MRCONSO.TTY, MRCONSO.CODE"
						+ " FROM umls.MRCONSO WHERE MRCONSO.CUI IN (" + getINString(CUIs)
						+ " ) and MRCONSO.ISPREF = 'Y' and MRCONSO.TS = 'P' AND LAT ="
						+ " 'ENG' group by MRCONSO.STR;");

				LinkedList<ConceptModel> data = new LinkedList<ConceptModel>();

				while (rs.next()) {
					data.add(new ConceptModel(rs.getString(1), rs.getString(2), rs.getString(3), rs
							.getString(4), rs.getString(5)));
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
	 * Method to get the list of concepts that are drugs with source as RXNorm
	 * from the given list of cui's
	 */
	public static LinkedList<ConceptModel> getDrugConceptsByCUI(LinkedList<String> cuis) {
		LinkedList<ConceptModel> concepts = getConceptByCUI(cuis);
		LinkedList<ConceptModel> drugs = new LinkedList<ConceptModel>();
		for (ConceptModel concept : concepts) {
			if (concept.getSource().equals("RXNORM") && concept.getTTY().equals("SCD")) {
				drugs.add(concept);
			}
		}
		return drugs;
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
		LinkedList<String> cuis = MRXWTableOperation.searchCUI("asthma");
		// LinkedList<String> cuis = new LinkedList<String>();
		// cuis.add("C0000039");
		LinkedList<ConceptModel> list = getConceptByCUI(cuis);
		System.out.println(list.isEmpty());
		for (ConceptModel item : list) {
			System.out.println(item);
		}
	}
}
