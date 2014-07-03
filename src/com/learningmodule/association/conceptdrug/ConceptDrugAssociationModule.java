package com.learningmodule.association.conceptdrug;

import java.util.LinkedList;

import com.learningmodule.association.conceptdrug.learning.LearningMethod;
import com.learningmodule.association.conceptdrug.predictionmodule.PredictionMethod;
import com.machine.learning.interfaces.LearningModuleInterface;
import com.raxa.association.ConceptDrugDatabaseInput;

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
		conceptDrugPrediction = new PredictionMethod(conceptDrugLeanrning.getMatrix(),
				conceptDrugReleationLearningInterface);
	}

	@Override
	public void learn() {
		conceptDrugLeanrning.learn(conceptDrugLearningInterface);
		conceptDrugPrediction.setPredictionMatrix(conceptDrugLeanrning.getMatrix());
		// System.out.println(conceptDrugLeanrning.getMatrix());
	}

	@Override
	public LinkedList<PredictionResult> predict(String query) {
		return conceptDrugPrediction.predict(query);
	}

	public static void main(String[] args) {
		ConceptDrugAssociationModule module = new ConceptDrugAssociationModule(
				new ConceptDrugDatabaseInput());
		module.predict("");
		module.predict("asthma");
		// module.learn();
	}
}
