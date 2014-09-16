package com.learningmodule.association.conceptdrug.multifeature;

import java.util.List;

import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.PredictionResult;
import com.machine.learning.interfaces.LearningModuleInterface;
import com.machine.learning.request.SearchAttribute;

public class ConceptDrugLearningMultiFeatureModule implements LearningModuleInterface {

	private PredictionMethod predictionMethod;
	// min support for association rules
	private int minSupport = 1;

	// minConfidence for association rules
	private double minConfidence = 0.3;
	
	private ConceptDrugDatabaseInterface databaseInterface;
	
	public ConceptDrugLearningMultiFeatureModule(ConceptDrugDatabaseInterface databaseInterface) {
		this.databaseInterface = databaseInterface;
		predictionMethod = new PredictionMethod(minSupport, minConfidence, databaseInterface);
	}

	@Override
	public void learn() {
		predictionMethod = new PredictionMethod(minSupport, minConfidence, databaseInterface);
	}

	@Override
	public List<PredictionResult> predict(String query, SearchAttribute[] features) {
		return predictionMethod.predict(query, features);
	}

	public double getMinConfidence() {
		return minConfidence;
	}

	public void setMinConfidence(double minConfidence) {
		this.minConfidence = minConfidence;
		predictionMethod.setMinConfidence(minConfidence);
	}

	public int getMinSupport() {
		return minSupport;
	}

	public void setMinSupport(int minSupport) {
		predictionMethod.setMinSupport(minSupport);
		this.minSupport = minSupport;
	}

}
