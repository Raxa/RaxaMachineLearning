package com.learningmodule.association.conceptdrug.learning;

import weka.associations.Apriori;
import weka.core.FastVector;
import weka.core.Instances;

public class ConceptDrugLearning {
	public static Apriori learn() {
		Instances instances = PreProcessing.process(DrugConceptDataCollector.getData());
		Apriori algo = new Apriori();
		algo.setLowerBoundMinSupport(0.005);
		algo.setUpperBoundMinSupport(1);
		algo.setMinMetric(0.7);
		algo.setNumRules(50);
		try {
			algo.buildAssociations(instances);
			return algo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return algo;
	}
	public static void main(String args[]) {
		DrugConceptDataCollector.makeConnection();
		Apriori algo = learn();
		FastVector[] rules = algo.getAllTheRules();
		for(int i=0;i<rules.length;i++) {
			System.out.println(rules[i].size());
		}
	}
}
