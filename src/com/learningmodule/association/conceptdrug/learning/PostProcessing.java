package com.learningmodule.association.conceptdrug.learning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.learningmodule.association.conceptdrug.model.PredictionMatrix;

import weka.associations.ItemSet;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

/*
 * Class for Post Processing of the results from aproiri algorithm
 */

public class PostProcessing {

	private static Logger log = Logger.getLogger(PostProcessing.class);
	/*
	 * method to save the rules and instances in files "rules" and "instances"
	 */
	public static void save(FastVector[] allRules, Instances instances) {
		try {
			String path = PostProcessing.class.getResource("/").getFile();
			System.out.println(path);
			FileOutputStream saveFile = new FileOutputStream(path + "files/rules");
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(allRules);
			save.close();
			
			saveFile = new FileOutputStream(path + "files/instances");
			save = new ObjectOutputStream(saveFile);
			save.writeObject(instances);
			save.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	/*
	 * Method to save the final Prediction matrix
	 */
	public static void saveResults(PredictionMatrix matrix, String matrixFileName) {
		try {
			String path = PostProcessing.class.getResource("/").getFile();
			FileOutputStream saveFile = new FileOutputStream(path + "files/" + matrixFileName);
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(matrix);
			save.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		}
	}
	
	/*
	 * method to read the rules from file "rules"
	 */
	public static FastVector[] readRules() {
		try {

			String path = PostProcessing.class.getResource("/").getFile();
			InputStream saveFile = new FileInputStream(path + "files/rules");
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			FastVector[] obj = (FastVector[]) restore.readObject();
			restore.close();
			return obj;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * method to read the instances from file "instances"
	 */
	public static Instances readInstances() {
		try {
			String path = PostProcessing.class.getResource("/").getFile();
			InputStream saveFile = new FileInputStream(path + "files/instances");
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			Instances obj = (Instances) restore.readObject();
			restore.close();
			return obj;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * method that constructs the Prediction Matrix from the rules generated from algorithm and instances
	 */
	public static PredictionMatrix process(FastVector[] allRules, Instances insts, String matrixFileName) {
		// initialize the matrix with numbers of rows as the number of distinct concepts in input data
		PredictionMatrix matrix = new PredictionMatrix(insts.numAttributes() - 1);
		
		// traverse through every rule
		for (int i = 0; i < allRules[0].size(); i++) {
			
			// premises for i'th rule
			ItemSet premises = (ItemSet) allRules[0].elementAt(i);
			
			// consequences of i'th rule
			ItemSet consequences = (ItemSet) allRules[1].elementAt(i);
			
			// confidence of i'th rule
			double conf = (double) allRules[2].elementAt(i);
			
			// if the conseqences has a value for drug attribute
			if (consequences.itemAt(consequences.items().length - 1) != -1) {
				
				// get the drug attribute 
				Attribute drug = insts.attribute(consequences.items().length - 1);
				
				// for every concept attribute in premises
				for (int j = 0; j < premises.items().length - 1; j++) {
					if (premises.itemAt(j) != -1) {
						// add a new cell in the Prediction Matrix
						matrix.addCell((insts.attribute(j)).name(),
								drug.value(consequences.itemAt(consequences
										.items().length - 1)), conf);
					}
				}
			}
			System.out.println(conf);
			log.info("confidence: " + conf);
		}
		saveResults(matrix, matrixFileName);
		return matrix;
	}

	public static void main(String args[]) {
		//save(null, null);
		readRules();
	}
}
