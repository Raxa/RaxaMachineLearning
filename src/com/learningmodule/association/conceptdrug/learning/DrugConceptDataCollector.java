package com.learningmodule.association.conceptdrug.learning;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.database.DatabaseConnector;
import com.learningmodule.association.conceptdrug.model.EncounterIdConceptDrug;

/*
 * Class that is used to connect to database and load the data from OpenMRS database
 */

public class DrugConceptDataCollector {

	/*
	 * method that gets the data from database
	 */
	public static LinkedList<EncounterIdConceptDrug> getData() {
		if (DatabaseConnector.getConnection() != null) {
			System.out.println("You made it, take control your database now!");
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query
				ResultSet rs = stat
						.executeQuery("SELECT obs.encounter_id, drug_order.drug_inventory_id as drug, obs.concept_id "
								+ "from openmrs.orders, openmrs.drug_order, openmrs.obs "
								+ "where orders.order_type_id =2 && "
								+ "orders.encounter_id = obs.encounter_id && "
								+ "orders.order_id = drug_order.order_id;");

				// create a linked list of objects with EncounterId, drugId,
				// ConceptId(observation)
				LinkedList<EncounterIdConceptDrug> data = new LinkedList<EncounterIdConceptDrug>();
				while (rs.next()) {
					data.add(new EncounterIdConceptDrug(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else
			System.out.println("connection lost!");
		return null;
	}

	public static void main(String args[]) {
		System.out.println(getData().size());
		DatabaseConnector.closeConnection();
	}
}
