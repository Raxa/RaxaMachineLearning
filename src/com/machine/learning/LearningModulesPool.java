package com.machine.learning;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.learningmodule.association.conceptdrug.PredictionResult;
import com.machine.learning.interfaces.LearningModuleInterface;
import com.machine.learning.request.SearchAttribute;

/*
 * The class contains the list of learning modules that are added by ContextListner on start of this service
 */

public class LearningModulesPool {

	// LinkedList of Learning Modules in this pool
	private static LinkedList<LearningModuleInterface> modules = new LinkedList<LearningModuleInterface>();
	private static Logger log = Logger.getLogger(LearningModulesPool.class);

	// method to add a learning module in this pool
	public static void addLearningModule(LearningModuleInterface module) {
		modules.add(module);
		log.debug("Module Added: " + module.getClass());
	}

	/*
	 * method to get the results for a query from all modules in the pool and
	 * merge them to return all the results
	 */
	public static LinkedList<PredictionResult> predict(String query, SearchAttribute[] features) {

		LinkedList<PredictionResult> results = new LinkedList<PredictionResult>();

		// get the sorted results from a module and merge them with all results.
		for (int i = 0; i < modules.size(); i++) {
			results = join(modules.get(i).predict(query, features), results);
		}

		return results;
	}

	// method to merge/join two list of sorted prediction results
	private static LinkedList<PredictionResult> join(List<PredictionResult> list1,
			List<PredictionResult> list2) {
		// iterator on list1
		Iterator<PredictionResult> it1 = list1.iterator();

		// iteraton on list2
		Iterator<PredictionResult> it2 = list2.iterator();

		// results after joining
		LinkedList<PredictionResult> results = new LinkedList<PredictionResult>();
		PredictionResult temp1, temp2;
		if (it1.hasNext() && it2.hasNext()) {
			temp1 = it1.next();
			temp2 = it2.next();

			// if item in list1 has more confidence add it to the results else
			// add item of list2
			if (temp1.getConfidence() > temp2.getConfidence()) {
				results.add(temp1);
				temp1 = it1.next();
			} else {
				results.add(temp2);
				temp2 = it2.next();
			}

			// while one of the iterator reach its end
			while (it1.hasNext() && it2.hasNext()) {
				if (temp1.getConfidence() < temp2.getConfidence()) {
					results.add(temp1);
					temp1 = it1.next();
				} else {
					results.add(temp2);
					temp2 = it2.next();
				}
			}
		}
		// add items for list1 if any left
		while (it1.hasNext()) {
			results.add(it1.next());
		}

		// add items for list2 if any left
		while (it2.hasNext()) {
			results.add(it2.next());
		}
		return results;
	}

	/*
	 * method to call all the learning modules to initiate there learning
	 * algorithm.
	 */
	public static void learn() {
		Thread[] threads = new Thread[modules.size()];
		for (int i = 0; i < modules.size(); i++) {
			threads[i] = runLearningThread(i);
		}
		for (int i = 0; i < modules.size(); i++) {
			while (threads[i].isAlive())
				;
		}
	}

	private static Thread runLearningThread(int i) {
		final int j = i;
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				modules.get(j).learn();
			}
		});
		thread.run();
		return thread;
	}
}
