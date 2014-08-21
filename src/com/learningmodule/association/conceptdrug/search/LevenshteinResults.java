package com.learningmodule.association.conceptdrug.search;

import java.util.Set;

/*
 * Class that represent the model for results of Levenshtein Search
 */

public class LevenshteinResults {
	private String name;
	private double weight;
	private Set<String> conceptIds;

	public LevenshteinResults(String name, int distance, int maxCost, Set<String> concepts) {
		this.name = name;
		
		// set the weight given Levenshtein Distance as the below formula
		this.weight = ((double) (maxCost - distance + 1)) / ((double) (maxCost + 2));
		this.conceptIds = concepts;
	}

	public String getName() {
		return this.name;
	}

	public double getWeight() {
		return this.weight;
	}

	public Set<String> getConcepts() {
		return this.conceptIds;
	}
}