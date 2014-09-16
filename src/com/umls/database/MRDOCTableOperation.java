package com.umls.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.umls.models.MRDOCModel;

public class MRDOCTableOperation {
	private static LinkedList<MRDOCModel> relations;

	public static void getDocByDockeyRelRelA() {
		if (UmlsDatabaseConnector.getConnection() != null) {
			try {
				Statement stat = UmlsDatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT * FROM umls.MRDOC WHERE"
						+ " MRDOC.DOCKEY IN ('REL','RELA')");

				LinkedList<MRDOCModel> data = new LinkedList<MRDOCModel>();
				while (rs.next()) {
					data.add(new MRDOCModel(rs.getString(1), rs.getString(3), rs.getString(3), rs
							.getString(4)));
				}
				relations = data;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else
			System.out.println("connection lost!");
	}
	
	public static LinkedList<MRDOCModel> getRelationLabels() {
		if(relations == null) {
			getDocByDockeyRelRelA();
		}
		return relations;
	}
	public static void main(String[] args) {
		getDocByDockeyRelRelA();
		for(MRDOCModel item: relations) {
			System.out.print(item);
		}
	}
}
