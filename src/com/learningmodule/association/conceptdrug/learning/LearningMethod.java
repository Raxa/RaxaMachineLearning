package com.learningmodule.association.conceptdrug.learning;

/*
 * Class used for finding association rules between the observations and drugs.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.model.EncounterIdConceptDrug;
import com.learningmodule.association.conceptdrug.model.PredictionMatrix;

//import weka.associations.Apriori;
//import weka.core.Instances;

public class LearningMethod {

	// parameters of association rule finding
	private double lowerMinSupport = 0.0001;
	// private double upperMinSupport = 1.0;
	private double minConfidence = 0.3;
	// private double rulesToInstancesRatio = 0.4;
	private static Logger log = Logger.getLogger(LearningMethod.class);
	private PredictionMatrix resultMatrix;
	private String matrixFileName;

	public LearningMethod(String matrixFileName) {
		this.matrixFileName = matrixFileName;
	}

	/*
	 * method to get the Prediction Matrix
	 */
	public PredictionMatrix getMatrix() throws IOException {
		if (resultMatrix == null) {
			return readResults();
		}
		return resultMatrix;
	}

	/*
	 * method to save the results of association rules in a file
	 */
	public PredictionMatrix readResults() throws IOException {
		try {
			String path = PostProcessing.class.getResource("/").getFile();
			InputStream saveFile = new FileInputStream(path + "files/" + matrixFileName);
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			resultMatrix = (PredictionMatrix) restore.readObject();
			restore.close();
			System.out.print("xxx");
			return resultMatrix;
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
		// Instances instances =
		// PreProcessing.process(learningInterface.getData());
		LinkedList<EncounterIdConceptDrug> data = learningInterface.getData();
		log.debug("Pre Processing Done");
		// Initialize the Weka class for association rule finding.
		CustomApriori algo = new CustomApriori();

		// set the algorithm parameters.
		algo.setMinSupport(lowerMinSupport);
		// algo.setUpperBoundMinSupport(upperMinSupport);
		algo.setMinConfidence(minConfidence);
		// algo.setNumRules((int) (instances.numInstances() *
		// rulesToInstancesRatio));

		try {
			// build association rules
			resultMatrix = algo.buildAssociations(data);
			//System.out.println(resultMatrix);
			// log.debug("Aprori Results:" + algo);
			PostProcessing.saveResults(resultMatrix, matrixFileName);
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

	/*public static void main(String[] args) {
		LearningMethod learn = new LearningMethod("results");
		learn.learn(new OpenMRSConceptDrugDatabaseInput());
	}*/
}
