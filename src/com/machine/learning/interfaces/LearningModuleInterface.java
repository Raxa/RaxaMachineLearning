package com.machine.learning.interfaces;

import java.util.List;

import com.learningmodule.association.conceptdrug.PredictionResult;

public interface LearningModuleInterface {
	public void learn();
	public List<PredictionResult> predict(String query);
}
