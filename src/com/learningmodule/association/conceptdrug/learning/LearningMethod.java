package com.learningmodule.association.conceptdrug.learning;

/*
 * Class used for finding association rules between the observations and drugs.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;

import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.model.PredictionMatrix;
import com.raxa.association.ConceptDrugDatabaseInput;

import weka.associations.Apriori;
import weka.core.Instances;

public class LearningMethod {

	// parameters of association rule finding
	private double lowerMinSupport = 0.0001;
	private double upperMinSupport = 1.0;
	private double minConfidence = 0.6;
	private double rulesToInstancesRatio = 0.4;
	private static Logger log = Logger.getLogger(LearningMethod.class);
	private PredictionMatrix resultMatrix;
	private String matrixFileName = "files/results";

	public LearningMethod(String matrixFileName){
		this.matrixFileName = matrixFileName;
	}
	
	/*
	 * method to get the Prediction Matrix
	 */
	public PredictionMatrix getMatrix() {
		if (resultMatrix == null) {
			return readResults();
		}
		return resultMatrix;
	}

	/*
	 * method to save the results of association rules in a file
	 */
	public PredictionMatrix readResults() {
		try {
			String path = PostProcessing.class.getResource("/").getFile();
			InputStream saveFile = new FileInputStream(path + matrixFileName);
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			resultMatrix = (PredictionMatrix) restore.readObject();
			restore.close();
			return resultMatrix;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}

	/*
	 * method for learning from database
	 */
	public void learn(ConceptDrugDatabaseInterface learningInterface) {

		// get the pre-processed data
		Instances instances = PreProcessing.process(learningInterface.getData());
		log.debug("Pre Processing Done");
		// Initialize the Weka class for association rule finding.
		Apriori algo = new Apriori();

		// set the algorithm parameters.
		algo.setLowerBoundMinSupport(lowerMinSupport);
		algo.setUpperBoundMinSupport(upperMinSupport);
		algo.setMinMetric(minConfidence);
		algo.setNumRules((int) (instances.numInstances() * rulesToInstancesRatio));

		try {
			// build association rules
			algo.buildAssociations(instances);
			 System.out.println(algo);
			 log.debug("Aprori Results:" + algo);
			// PostProcessing.save(algo.getAllTheRules(), instances);
			resultMatrix = PostProcessing.process(algo.getAllTheRules(), instances, matrixFileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
		System.out.println("learned");
		log.debug("learned\nMatrix:");
		log.debug(resultMatrix.toString());
		
		return;
	}
	public static void main(String[] args) {
		LearningMethod learn = new LearningMethod("results");
		learn.learn(new ConceptDrugDatabaseInput());
	}
}
