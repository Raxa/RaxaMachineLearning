package com.pacemaker.association;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.database.DatabaseConnector;
import com.learningmodule.association.conceptdrug.AbstractDrugModel;
import com.learningmodule.association.conceptdrug.ConceptDrugAssociationModule;
import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.model.ConceptNameModel;
import com.learningmodule.association.conceptdrug.model.ConceptWordModel;
import com.learningmodule.association.conceptdrug.model.DrugModel;
import com.learningmodule.association.conceptdrug.model.EncounterIdConceptDrug;
import com.learningmodule.association.conceptdrug.multifeature.EncounterIdConceptFeaturesDrugModel;
import com.learningmodule.association.conceptdrug.multifeature.Feature;
import com.learningmodule.association.conceptdrug.multifeature.FeatureValue;
import com.machine.learning.request.SearchAttribute;

/*
 * class that implements the interface for Pace Maker Database
 */

public class PaceMakeConceptDrugDatabaseInput implements ConceptDrugDatabaseInterface {

	// matrix file name for the results generated for this database
	private String matrixFileName = "paceResults";
	private static Logger log = Logger.getLogger(PaceMakeConceptDrugDatabaseInput.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface
	 * #getMatrixFileName()
	 * 
	 * method sends the matrix file name
	 */
	@Override
	public String getMatrixFileName() {
		return matrixFileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface
	 * #getData()
	 */
	@Override
	public LinkedList<EncounterIdConceptDrug> getData() {
		// get the connection with database
		if (DatabaseConnector.getConnection() != null) {
			System.out.println("You made it, take control your database now!");
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query to get the medical records for learning
				// Concept(ICD9Code of disease/syndrom), drug association rules
				ResultSet rs = stat.executeQuery("SELECT diagnosis.id, medication.ndc_code"
						+ ", diagnosis.ICD9Code FROM kaggle_training_data.diagnosis,"
						+ " kaggle_training_data.medication WHERE diagnosis.guid ="
						+ " medication.diagnosis_guid;");

				// create the list of EncounterId(diagnosis Ids) Concept(ICD9Code) Drug(ndc code of drug)
				LinkedList<EncounterIdConceptDrug> data = new LinkedList<EncounterIdConceptDrug>();
				while (rs.next()) {
					data.add(new EncounterIdConceptDrug(rs.getInt(1), rs.getString(2), rs
							.getString(3)));
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

	/*
	 * (non-Javadoc)
	 * @see com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface#getDataByConceptIds(java.util.LinkedList)
	 */
	@Override
	public LinkedList<EncounterIdConceptFeaturesDrugModel> getDataByConceptIds(LinkedList<String> ids) {

		// get the connection with database
		if (DatabaseConnector.getConnection() != null) {
			System.out.println("You made it, take control your database now!");
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query to get the medical records for learning
				// Concept(ICD9Code of disease/syndrom), drug association rules
				ResultSet rs = stat
						.executeQuery("SELECT diagnosis.id, medication.ndc_code"
								+ ", diagnosis.ICD9Code, patient.age, patient.state FROM kaggle_training_data.diagnosis,"
								+ " kaggle_training_data.medication, kaggle_training_data.patient"
								+ " WHERE diagnosis.ICD9Code in (" + getIdsString(ids)
								+ ") && diagnosis.guid = medication.diagnosis_guid"
								+ " && medication.patient_guid = patient.guid;");

				try {
					// create Age feature with type integer
					Feature ageFeature = new Feature("age", Feature.INTEGERTYPE,
							new Feature.DistanceCalculator() {

								@Override
								public double getDistance(Object i1, Object i2) throws Exception {
									int age1 = (int) i1;
									int age2 = (int) i2;
									int d = age2 - age1;
									if (d < 0)
										d = d * (-1);
									if (d > 15) {
										return 0.0;
									} else {
										return 1.0 - (((double) d) / 15.0);
									}
								}
							});
					
					// create State feature with type string
					Feature stateFeature = new Feature("state", Feature.STRINGTYPE);
					LinkedList<EncounterIdConceptFeaturesDrugModel> data = new LinkedList<EncounterIdConceptFeaturesDrugModel>();

					while (rs.next()) {
						
						// set the age feature value and state feature value of record
						FeatureValue[] values = new FeatureValue[2];
						values[0] = new FeatureValue(ageFeature, getAge(rs.getString(4)));
						values[1] = new FeatureValue(stateFeature, rs.getString(5));
						
						// add the medical record with encounter id, conceptId, drugId, and feature values 
						data.add(new EncounterIdConceptFeaturesDrugModel(rs.getInt(1), rs
								.getString(3), rs.getString(2), values));
					}
					return data;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else
			System.out.println("connection lost!");
		return null;
	}

	private static Integer getAge(String date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = sd.parse(date);
			Date now = new Date();
			return new Integer(now.getYear() - d.getYear());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Integer(-1);
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface#getConceptIdNameByConceptIds(java.util.LinkedList)
	 */
	@Override
	public LinkedList<ConceptNameModel> getConceptIdNameByConceptIds(LinkedList<String> conceptIds) {
		LinkedList<ConceptNameModel> results = new LinkedList<ConceptNameModel>();
		if (DatabaseConnector.getConnection() != null) {
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query to get the list of concepts names of
				// all concepts in drug results
				ResultSet rs = stat.executeQuery("SELECT diagnosis.ICD9Code, "
						+ "diagnosis.Discription FROM kaggle_training_data.diagnosis where"
						+ " ICD9Code In ( " + getIdsString(conceptIds)
						+ ") group by diagnosis.ICD9Code;");

				// for ever row of concept name and concept Id
				while (rs.next()) {
					results.add(new ConceptNameModel(rs.getString(1), rs.getString(2)));
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
	public LinkedList<AbstractDrugModel> getDrugInfoByDrugIds(LinkedList<String> conceptIds) {
		LinkedList<AbstractDrugModel> results = new LinkedList<AbstractDrugModel>();
		if (DatabaseConnector.getConnection() != null && !conceptIds.isEmpty()) {
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query to get drug related info for given drug
				// Ids(ndc code)
				ResultSet rs = stat.executeQuery("SELECT medication.ndc_code, "
						+ "medication.medication_name FROM kaggle_training_data.medication"
						+ " WHERE medication.ndc_code in ( " + getIdsString(conceptIds)
						+ ") group by medication.ndc_code;");
				// for every drugId from results of query
				while (rs.next()) {
					// set the drug info in the drug result
					results.add(new DrugModel(rs.getString(2), null, null, rs.getString(1), rs
							.getString(1)));

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
				// execute the SQL query get all the diagnosis names and there ICD9COde
				ResultSet rs = stat
						.executeQuery("	SELECT diagnosis.ICD9Code, diagnosis.Discription"
								+ " FROM kaggle_training_data.diagnosis group by diagnosis.ICD9Code;");

				// create a linked list of ConceptWordModel object
				LinkedList<ConceptWordModel> data = new LinkedList<ConceptWordModel>();
				while (rs.next()) {
					// remove all the special characters
					String temp = rs.getString(2).replaceAll("[^a-zA-Z0-9\\s]+", " ");
					
					// split the concept name to get concept words
					String[] words = temp.split("\\s+");
					for (int i = 0; i < words.length; i++) {
						data.add(new ConceptWordModel(rs.getString(1), words[i]));
					}
				}
				return data;
			} catch (SQLException e1) {
				e1.printStackTrace();
				log.error(e1);
			}
		} else {
			System.out.println("connection lost!");
		}
		return null;
	}

	private static String getIdsString(LinkedList<String> ids) {
		String str = "'";
		for (String id : ids) {
			str = str + id + "', '";

		}
		str = str + "0' ";
		return str;
	}

	public static void main(String[] args) {
		PaceMakeConceptDrugDatabaseInput obj = new PaceMakeConceptDrugDatabaseInput();
		obj.getConceptWords();
		ConceptDrugAssociationModule module = new ConceptDrugAssociationModule(
				new PaceMakeConceptDrugDatabaseInput());

		// module.predict("");
		module.predict("asthma",new SearchAttribute[0]);
		// module.learn();
		// module.predict("asthma");
	}
}
