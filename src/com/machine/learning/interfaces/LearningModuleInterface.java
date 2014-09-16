package com.machine.learning.interfaces;

import java.util.List;

import com.learningmodule.association.conceptdrug.PredictionResult;
import com.machine.learning.request.SearchAttribute;

public interface LearningModuleInterface {
	public void learn();
	public List<PredictionResult> predict(String query, SearchAttribute[] features);
}
