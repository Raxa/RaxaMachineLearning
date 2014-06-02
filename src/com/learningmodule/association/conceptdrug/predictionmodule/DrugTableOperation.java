package com.learningmodule.association.conceptdrug.predictionmodule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

import com.database.PropertiesReader;
import com.learningmodule.association.conceptdrug.model.DrugModel;

/*
 * class to fetch the drug data from database
 */

public class DrugTableOperation {
	private static Connection connection = null;

	// method to get the list of Drugs details from database of given drugIDs
	public static LinkedList<DrugModel> search(LinkedList<Integer> drugIds) {
		if (isConnected() && !drugIds.isEmpty()) {
			try {
				Statement stat = connection.createStatement();
				// execute the SQL query
				ResultSet rs = stat.executeQuery("SELECT drug.drug_id, "
						+ "drug.name, drug.generic_name, drug.uuid, drug.drug_commercial_id "
						+ "from openmrs.drug where drug.drug_id in ( " + getWords(drugIds) + ");");

				// create a linked list of Drugs
				LinkedList<DrugModel> data = new LinkedList<DrugModel>();
				while (rs.next()) {
					data.add(new DrugModel(rs.getString(2), rs.getString(3), rs.getString(4), rs
							.getInt(5), rs.getInt(1)));
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.println("connection lost! OR list of Drug IDs empty");
		}
		return null;
	}

	private static String getWords(LinkedList<Integer> drugIds) {
		Iterator<Integer> id = drugIds.iterator();
		String str = " '" + id.next() + "'";
		while (id.hasNext()) {
			str = str + ", '" + id.next() + "'";
		}
		return str;
	}

	public static boolean isConnected() {
		if (connection != null) {
			try {
				return !connection.isClosed();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public static void makeConnection() {

		try {
			// get the connection with the database
			PropertiesReader.load();
			connection = DriverManager.getConnection(PropertiesReader.getUrl(),
					PropertiesReader.getUser(), PropertiesReader.getPassword());
			System.out.println("MySQL JDBC Driver Registered!");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		LinkedList<Integer> drugIds = new LinkedList<Integer>();
		drugIds.add(3000463);
		makeConnection();
		LinkedList<DrugModel> tmp = search(drugIds);
		closeConnection();
		Iterator<DrugModel> itr = tmp.iterator();
		while(itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
}
