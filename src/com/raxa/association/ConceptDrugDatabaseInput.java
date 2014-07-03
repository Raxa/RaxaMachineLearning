package com.raxa.association;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.database.DatabaseConnector;
import com.learningmodule.association.conceptdrug.AbstractDrugModel;
import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.model.ConceptNameModel;
import com.learningmodule.association.conceptdrug.model.ConceptWordModel;
import com.learningmodule.association.conceptdrug.model.DrugModel;
import com.learningmodule.association.conceptdrug.model.EncounterIdConceptDrug;

public class ConceptDrugDatabaseInput implements ConceptDrugDatabaseInterface {

	private String matrixFileName = "files/results";
	private static Logger log = Logger.getLogger(ConceptDrugDatabaseInput.class);
	@Override
	public String getMatrixFileName() {
		return matrixFileName;
	}

	@Override
	public LinkedList<EncounterIdConceptDrug> getData() {
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
				log.error(e1);
			}
		} else
			System.out.println("connection lost!");
		return null;
	}

	@Override
	public LinkedList<ConceptNameModel> getConceptIdNameByConceptIds(LinkedList<Integer> conceptIds) {
		LinkedList<ConceptNameModel> results = new LinkedList<ConceptNameModel>();
		if (DatabaseConnector.getConnection() != null) {
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query to get the list of concepts names of
				// all concepts in drug results
				ResultSet rs = stat.executeQuery("SELECT concept_name.concept_id,"
						+ " concept_name.name from openmrs.concept_name"
						+ " where concept_name.concept_id in ( " + getIdsString(conceptIds)
						+ ") and locale = 'en';");

				// for ever row of concept name and concept Id
				while (rs.next()) {
					results.add(new ConceptNameModel(rs.getInt(1), rs.getString(2)));
				}
				return results;
			} catch (SQLException e1) {
				e1.printStackTrace();
				log.error(e1);
			}
		} else {
			System.out.println("connection lost!");
		}
		return null;

	}

	@Override
	public LinkedList<AbstractDrugModel> getDrugInfoByDrugIds(LinkedList<Integer> conceptIds) {
		LinkedList<AbstractDrugModel> results = new LinkedList<AbstractDrugModel>();
		if (DatabaseConnector.getConnection() != null && !conceptIds.isEmpty()) {
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query to get drug related info for given drug
				// Ids
				ResultSet rs = stat.executeQuery("SELECT drug.drug_id, "
						+ "drug.name, drug.generic_name, drug.uuid, drug.drug_commercial_id "
						+ "from openmrs.drug where drug.drug_id in ( " + getIdsString(conceptIds)
						+ ");");
				// for every drugId from results of query
				while (rs.next()) {

					// set the drug info in the drug result
					results.add(new DrugModel(rs.getString(2), rs.getString(3), rs.getString(4), rs
							.getInt(5), rs.getInt(1)));

				}

			} catch (SQLException e1) {
				e1.printStackTrace();
				log.error(e1);
			}
		} else {
			System.out.println("connection lost! OR list of Drug IDs empty = " + results.isEmpty());
		}
		return results;
	}

	@Override
	public LinkedList<ConceptWordModel> getConceptWords() {
		if (DatabaseConnector.getConnection() != null) {

			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query get all the row from concept_word table
				ResultSet rs = stat
						.executeQuery("SELECT concept_word.word, concept_word.concept_id"
								+ " FROM openmrs.concept_word;");

				// create a linked list of ConceptWordModel object
				LinkedList<ConceptWordModel> data = new LinkedList<ConceptWordModel>();
				while (rs.next()) {
					data.add(new ConceptWordModel(rs.getInt(2), rs.getString(1)));
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();
				log.error(e1);
			}
		} else
			System.out.println("connection lost!");
		return null;
	}

	private static String getIdsString(LinkedList<Integer> ids) {
		String str = "";
		for (Integer id : ids) {
			str = str + id + ", ";

		}
		str = str + "0 ";
		return str;
	}

}
