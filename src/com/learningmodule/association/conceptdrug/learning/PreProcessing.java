package com.learningmodule.association.conceptdrug.learning;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.SparseInstance;

import com.learningmodule.association.conceptdrug.model.EncounterIdConceptDrug;

public class PreProcessing {
	public static Instances process(LinkedList<EncounterIdConceptDrug> data) {
		Iterator<EncounterIdConceptDrug> itr = data.iterator();
		Hashtable<Integer, Integer> conceptTable = new Hashtable<Integer, Integer>();
		Hashtable<Integer, Integer> drugTable = new Hashtable<Integer, Integer>();
		FastVector drugs = new FastVector();
		FastVector attrib = new FastVector();
		FastVector conceptValue = new FastVector();
		conceptValue.addElement("true");
		int conceptCounter = 0;
		int drugCounter = 0;
		int noOfEncounters = 0;
		int currentEncounterId = 0;
		EncounterIdConceptDrug temp;
		while (itr.hasNext()) {
			temp = itr.next();
			if (!conceptTable.containsKey(temp.getConceptId())) {
				conceptTable.put(temp.getConceptId(), conceptCounter++);
				attrib.addElement(new Attribute(Integer.toString(temp.getConceptId()), conceptValue));
			}
			if (!drugTable.containsKey(temp.getDrug())) {
				drugTable.put(temp.getDrug(), drugCounter++);
				drugs.addElement(Integer.toString(temp.getDrug()));
			}
			if (temp.getEncounterId() != currentEncounterId) {
				currentEncounterId = temp.getEncounterId();
				noOfEncounters++;
			}
		}
		Attribute drugAttribute = new Attribute("drugs", drugs);
		attrib.addElement(drugAttribute);
		Instances instances = new Instances("dataset", attrib, noOfEncounters);
		itr = data.iterator();
		SparseInstance inst = new SparseInstance(conceptCounter + 1);
		inst.setDataset(instances);
		currentEncounterId = 0;
		while (itr.hasNext()) {
			
			temp = itr.next();
			if (temp.getEncounterId() != currentEncounterId && currentEncounterId != 0) {
				//System.out.println(itr.hasNext());
				instances.add(inst);
				inst = new SparseInstance(conceptCounter + 1);
				inst.setValue(drugAttribute, Integer.toString(temp.getDrug()));
				inst.setDataset(instances);
			}
			if(currentEncounterId == 0) inst.setValue(drugAttribute, Integer.toString(temp.getDrug()));
			inst.setValue(conceptTable.get(temp.getConceptId()).intValue(), "true");
			currentEncounterId = temp.getEncounterId();
		}
		return filter(instances);
	}

	public static Instances filter(Instances inst) {
		
		int size = inst.numInstances();
		for(int i=0;i<inst.numAttributes()-1;i++) {
			int missCount = inst.attributeStats(i).missingCount;
			if(missCount < 0.5*size) {
				inst.deleteAttributeAt(i--);
			}
		}
		return inst;
	}
	
	public static void main(String args[]) {
		DrugConceptDataCollector.makeConnection();
		System.out.println(	process(DrugConceptDataCollector.getData()));
	}
}
