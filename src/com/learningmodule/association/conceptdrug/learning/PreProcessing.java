package com.learningmodule.association.conceptdrug.learning;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.SparseInstance;

import com.learningmodule.association.conceptdrug.model.EncounterIdConceptDrug;

/*
 * class for preprocessing the data and converting it into form of Instances for Weka.
 */
public class PreProcessing {
	private static Logger log = Logger.getLogger(PreProcessing.class);

	public static Instances process(LinkedList<EncounterIdConceptDrug> data) {

		// get an iterator on data
		Iterator<EncounterIdConceptDrug> itr = data.iterator();

		// hash tables for concepts and drugs with key as the concept_id and
		// drug_id
		// concept hashtable contains the value as the column index of concept
		// attribute in instances
		Hashtable<Integer, Integer> conceptTable = new Hashtable<Integer, Integer>();
		Hashtable<Integer, Integer> drugTable = new Hashtable<Integer, Integer>();

		// nominal vector that contains all drugIds for the drug attribute in
		// Instance
		FastVector drugs = new FastVector();

		// nominal vector of attributes of the instances
		FastVector attrib = new FastVector();

		// nominal vector of values for concepts which is "true" if it exist or
		// value is missing
		FastVector conceptValue = new FastVector();
		conceptValue.addElement("true");

		int conceptCounter = 0;
		int drugCounter = 0;
		int noOfEncounters = 0;
		int currentEncounterId = 0;
		EncounterIdConceptDrug temp;

		// loop where we find distinct concept_id's, drug_id's and total number
		// of distinct encounter_id
		while (itr.hasNext()) {
			temp = itr.next();

			// if concept_id is not already there in hash table insert it
			if (!conceptTable.containsKey(temp.getConceptId())) {
				conceptTable.put(temp.getConceptId(), conceptCounter++);

				// make a new attribute for every distinct concept_id found
				attrib.addElement(new Attribute(Integer.toString(temp.getConceptId()), conceptValue));
			}

			// if drug_id not already there is hash table insert it
			if (!drugTable.containsKey(temp.getDrug())) {
				drugTable.put(temp.getDrug(), drugCounter++);

				// add new value to nominal vector of drugs as you find
				// different drugs
				drugs.addElement(Integer.toString(temp.getDrug()));
			}

			// increase the counter for number of encounter_id's as you get a id
			// different from previous one
			if (temp.getEncounterId() != currentEncounterId) {
				currentEncounterId = temp.getEncounterId();
				noOfEncounters++;
			}
		}
		// set the nominal vector of drug as the vector for drug attribute
		Attribute drugAttribute = new Attribute("drugs", drugs);

		// add drug attribute to the vector of attributes for instances
		attrib.addElement(drugAttribute);
		Instances instances = new Instances("dataset", attrib, noOfEncounters);
		itr = data.iterator();

		// Initialize the instance with number of attributes
		SparseInstance inst = new SparseInstance(conceptCounter + 1);
		inst.setDataset(instances);
		currentEncounterId = 0;
		// loop that copies the data from records to the Instances for Weka
		while (itr.hasNext()) {

			temp = itr.next();
			// if the encounderId is different from previous one
			if (temp.getEncounterId() != currentEncounterId && currentEncounterId != 0) {
				// add this instance to instances
				instances.add(inst);

				// create new instance
				inst = new SparseInstance(conceptCounter + 1);

				// set the value of drugattribute for instance
				inst.setValue(drugAttribute, Integer.toString(temp.getDrug()));
				inst.setDataset(instances);
			}
			if (currentEncounterId == 0)
				inst.setValue(drugAttribute, Integer.toString(temp.getDrug()));

			// set the value of attribute for the concept as true
			inst.setValue(conceptTable.get(temp.getConceptId()).intValue(), "true");
			currentEncounterId = temp.getEncounterId();
		}
		// filter the attributes and return the instances
		return filterAttributes(instances);
	}

	/*
	 * Method that returns the instances after filtering the concepts that are
	 * very frequent
	 */
	public static Instances filterAttributes(Instances inst) {

		int size = inst.numInstances();
		for (int i = 0; i < inst.numAttributes() - 1; i++) {
			int missCount = inst.attributeStats(i).missingCount;
			// check if the concept is frequent in most of the transaction
			if (missCount < 0.5 * size) {
				inst.deleteAttributeAt(i--);
			}
		}
		return inst;
	}

	public static void main(String args[]) {
		// DrugConceptDataCollector.makeConnection();
		//System.out.println(process(DrugConceptDataCollector.getData()));
	}
}
