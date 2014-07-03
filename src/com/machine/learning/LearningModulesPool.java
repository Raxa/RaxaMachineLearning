package com.machine.learning;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.learningmodule.association.conceptdrug.PredictionResult;
import com.machine.learning.interfaces.LearningModuleInterface;

public class LearningModulesPool {
	private static LinkedList<LearningModuleInterface> modules = new LinkedList<LearningModuleInterface>();
	private static Logger log = Logger.getLogger(LearningModulesPool.class);

	public static void addLearningModule(LearningModuleInterface module) {
		modules.add(module);
		log.debug("Module Added: " + module.getClass());
	}

	public static List<PredictionResult> predict(String query) {
		return modules.get(0).predict(query);
	}

	public static void learn() {
		modules.get(0).learn();
	}

}
