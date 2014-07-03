package com.learningmodule.association.conceptdrug.predictionmodule;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.learningmodule.association.conceptdrug.AbstractDrugModel;
import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.PredictionResult;
import com.learningmodule.association.conceptdrug.model.ConceptDrugPredictionResult;

/*
 * class to fetch the drug data from database
 */

public class DrugTableOperation {

	private static Logger log = Logger.getLogger(DrugTableOperation.class);

	// method to get the list of Drugs details from database of given drugIDs
	public static LinkedList<PredictionResult> search(
			LinkedList<ConceptDrugPredictionResult> drugs,
			ConceptDrugDatabaseInterface learningInterface) {

		LinkedList<AbstractDrugModel> drugsInfo = learningInterface
				.getDrugInfoByDrugIds(getIds(drugs));
		for (AbstractDrugModel drugInfo : drugsInfo) {

			// search for drugId in drug results
			for (ConceptDrugPredictionResult drug : drugs) {
				if (drug.getDrug().getDrugId() == drugInfo.getDrugId()) {
					drug.setDrug(drugInfo);
				}
			}
		}
		LinkedList<PredictionResult> results = new LinkedList<PredictionResult>();
		for (ConceptDrugPredictionResult result : drugs) {
			results.add(result);
			log.info(result.toString());
		}
		return results;
	}

	// create the string of drugIds separated by comma that will put in sql
	// query
	private static LinkedList<Integer> getIds(LinkedList<ConceptDrugPredictionResult> drugs) {
		LinkedList<Integer> ids = new LinkedList<Integer>();
		for (ConceptDrugPredictionResult drug : drugs) {
			ids.add(drug.getDrug().getDrugId());
		}
		return ids;
	}

	public static void main(String[] args) {
		LinkedList<ConceptDrugPredictionResult> rows = new LinkedList<>();
		rows.add(new ConceptDrugPredictionResult(3000463, 0.8));
		rows.add(new ConceptDrugPredictionResult(3000472, 0.7));
		// rows = search(rows);
		for (ConceptDrugPredictionResult row : rows) {
			System.out.println(row.toString());
		}
	}
}
