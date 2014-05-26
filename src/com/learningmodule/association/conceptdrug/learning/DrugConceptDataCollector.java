package com.learningmodule.association.conceptdrug.learning;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.learningmodule.association.conceptdrug.model.EncounterIdConceptDrug;

public class DrugConceptDataCollector {
	private static Connection connection = null;

	public static void makeConnection() {
		System.out.println("MySQL JDBC Driver Registered!");
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://raxa-test.cvgs1e008nbr.ap-southeast-1.rds.amazonaws.com", "root",
					"dlkw05j21vkd2b");

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
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

	public static LinkedList<EncounterIdConceptDrug> getData() {
		if (isConnected()){
			System.out.println("You made it, take control your database now!");
			try {
				Statement stat = connection.createStatement();
				ResultSet rs = stat
						.executeQuery("SELECT obs.encounter_id, drug_order.drug_inventory_id as drug, obs.concept_id "
								+ "from openmrs.orders, openmrs.drug_order, openmrs.obs "
								+ "where orders.order_type_id =2 && "
								+ "orders.encounter_id = obs.encounter_id && "
								+ "orders.order_id = drug_order.order_id;");
				LinkedList<EncounterIdConceptDrug> data = new LinkedList<EncounterIdConceptDrug>();
				while (rs.next()) {
					data.add(new EncounterIdConceptDrug(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else System.out.println("connection lost!");
		return null;
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		if (!isConnected())
			makeConnection();
		System.out.println(getData().size());
		closeConnection();
	}
}
