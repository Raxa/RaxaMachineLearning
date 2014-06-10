package com.learningmodule.association.conceptdrug.learning;

/*
 * Class used for finding association rules between the observations and drugs.
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import com.learningmodule.association.conceptdrug.model.PredictionMatrix;

import weka.associations.Apriori;
import weka.core.Instances;

public class ConceptDrugLearning {
	
	// parameters of association rule finding
	private static final double lowerMinSupport = 0.0001;
	private static final double upperMinSupport = 1.0;
	private static final double minConfidence = 0.6;
	private static final double rulesToInstancesRatio = 0.4;

	private static PredictionMatrix resultMatrix;
	
	/*
	 * method to get the Prediction Matrix
	 */
	public static PredictionMatrix getMatrix() {
		if(resultMatrix == null) {
			return readResults();
		}
		return resultMatrix;
	}
	
	/*
	 * method to save the results of association rules in a file
	 */
	public static PredictionMatrix readResults() {
		try {
			InputStream saveFile = ConceptDrugLearning.class.getClassLoader().getResourceAsStream("files/results");
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			PredictionMatrix obj = (PredictionMatrix) restore.readObject();
			restore.close();
			return obj;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * method for learning from database
	 */
	public static void learn() {
		
		// get the pre-processed data
		Instances instances = PreProcessing.process(DrugConceptDataCollector.getData());
		
		// Initialize the Weka class for association rule finding.
		Apriori algo = new Apriori();
		
		// set the algorithm parameters.
		algo.setLowerBoundMinSupport(lowerMinSupport);
		algo.setUpperBoundMinSupport(upperMinSupport);
		algo.setMinMetric(minConfidence);
		algo.setNumRules((int)(instances.numInstances()*rulesToInstancesRatio));
		
		try {
			// build association rules
			algo.buildAssociations(instances);
			// System.out.println(algo);
			// PostProcessing.save(algo.getAllTheRules(), instances);
			resultMatrix = PostProcessing.process(algo.getAllTheRules(), instances);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("learned");
		return;
	}
	public static void main(String args[]) {
		//DrugConceptDataCollector.makeConnection();
		learn();
	}
}
