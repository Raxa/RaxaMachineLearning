package com.learningmodule.association.conceptdrug;

import java.io.IOException;
import java.util.LinkedList;

import com.learningmodule.association.conceptdrug.learning.LearningMethod;
import com.learningmodule.association.conceptdrug.predictionmodule.PredictionMethod;
import com.machine.learning.interfaces.LearningModuleInterface;
import com.machine.learning.request.SearchAttribute;

/*
 * Machine Learning Module for the Association rule finding
 * algorithm for finding relation between concepts and drugs
 */

public class ConceptDrugAssociationModule implements LearningModuleInterface {

	// Object implementing interface required to get the data for algorithm
	private ConceptDrugDatabaseInterface conceptDrugLearningInterface;

	// Learning part of Module
	private LearningMethod conceptDrugLeanrning;

	// Prediction part of Module
	private PredictionMethod conceptDrugPrediction;

	/*
	 * Constructor Object for this module require object that implements the
	 * interface for drug/concept data
	 */
	public ConceptDrugAssociationModule(
			ConceptDrugDatabaseInterface conceptDrugReleationLearningInterface) {
		this.conceptDrugLearningInterface = conceptDrugReleationLearningInterface;
		conceptDrugLeanrning = new LearningMethod(
				conceptDrugReleationLearningInterface.getMatrixFileName());
		try {
			//conceptDrugLeanrning.learn(conceptDrugLearningInterface);
			conceptDrugPrediction = new PredictionMethod(conceptDrugLeanrning.getMatrix(),
					conceptDrugReleationLearningInterface);
		} catch (Exception e) {
			conceptDrugLeanrning.learn(conceptDrugLearningInterface);
			try {
				conceptDrugPrediction = new PredictionMethod(conceptDrugLeanrning.getMatrix(),
						conceptDrugReleationLearningInterface);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	@Override
	public void learn() {
		conceptDrugLeanrning.learn(conceptDrugLearningInterface);
		try {
			conceptDrugPrediction.setPredictionMatrix(conceptDrugLeanrning.getMatrix());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(conceptDrugLeanrning.getMatrix());
	}

	@Override
	public LinkedList<PredictionResult> predict(String query, SearchAttribute[] features) {
		return conceptDrugPrediction.predict(query);
	}

	/*public static void main(String[] args) {
		ConceptDrugAssociationModule module = new ConceptDrugAssociationModule(
				new OpenMRSConceptDrugDatabaseInput());
		//module.learn();
		//module.predict("");
		module.predict("anemia", new SearchAttribute[0]);
		//module.learn();
	}*/
}
