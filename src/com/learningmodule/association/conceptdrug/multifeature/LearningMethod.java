package com.learningmodule.association.conceptdrug.multifeature;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.database.DatabaseConnector;

public class LearningMethod {
	
	private static int noOfFeature = 1;
	
	public static LinkedList<EncounterIdConceptFeaturesDrugModel> getData(LinkedList<String> ids) {
		// get the connection with database
		if (DatabaseConnector.getConnection() != null) {
			System.out.println("You made it, take control your database now!");
			try {
				Statement stat = DatabaseConnector.getConnection().createStatement();
				// execute the SQL query to get the medical records for learning
				// Concept(ICD9Code of disease/syndrom), drug association rules
				ResultSet rs = stat.executeQuery("SELECT diagnosis.id, medication.ndc_code"
						+ ", diagnosis.ICD9Code, patient.age, patient.state FROM kaggle_training_data.diagnosis,"
						+ " kaggle_training_data.medication, kaggle_training_data.patient"
						+ " WHERE diagnosis.ICD9Code in (" + getIdsString(ids)
						+ ") && diagnosis.guid = medication.diagnosis_guid"
						+ " && medication.patient_guid = patient.guid;");

				try {
					Feature ageFeature = new Feature("age", Feature.INTEGERTYPE,
							new Feature.DistanceCalculator() {

								@Override
								public double getDistance(Object i1, Object i2) throws Exception {
									int age1 = (int) i1;
									int age2 = (int) i2;
									int d = age2 - age1;
									if (d < 0)
										d = d * (-1);
									if (d > 1) {
										return 0.0;
									} else {
										return 1.0;// - (((double) d) / 15.0);
									}
								}
							});
					Feature stateFeature = new Feature("state", Feature.STRINGTYPE);
					LinkedList<EncounterIdConceptFeaturesDrugModel> data = new LinkedList<EncounterIdConceptFeaturesDrugModel>();

					while (rs.next()) {
						FeatureValue[] values = new FeatureValue[2];
						values[0] = new FeatureValue(ageFeature, getAge(rs.getString(4)));
						values[1] = new FeatureValue(stateFeature, rs.getString(5));
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

	private static String getIdsString(LinkedList<String> ids) {
		String str = "'";
		for (String id : ids) {
			str = str + id + "', '";

		}
		str = str + "0' ";
		return str;
	}

	public static void main(String[] args) {
		LinkedList<String> ids = new LinkedList<String>();
		ids.add("493");
		LinkedList<EncounterIdConceptFeaturesDrugModel> x = getData(ids);
		for(EncounterIdConceptFeaturesDrugModel tm: x) {
			System.out.println(tm);
		}
	}
}
