package com.learningmodule.association.conceptdrug.search;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.learningmodule.association.conceptdrug.ConceptDrugDatabaseInterface;
import com.learningmodule.association.conceptdrug.model.ConceptWordModel;

/*
 * Class that implements method to get the Approximate String matches using Levenshtien Distance
 * for a string and to make the Dictionary
 * link of the algorithm explanation http://stevehanov.ca/blog/index.php?id=114
 */

public class LevenshteinSearch {
	
	// root node of the Trie
	private TrieNode trie;
	
	private Logger log = Logger.getLogger(LevenshteinSearch.class);
	
	// database interface to get dictionary for concept words
	private ConceptDrugDatabaseInterface learningInterface;
	
	// constructor for this class
	public LevenshteinSearch(ConceptDrugDatabaseInterface learningInterface) {
		this.learningInterface = learningInterface;
		
		// make the dictionary when Object is created
		makeDictionary();
	}
	/*
	 * Method that will construct the dictionary by getting all the
	 * concept_words from database
	 */
	public void makeDictionary() {

		// get the list of words from database
		LinkedList<ConceptWordModel> names = learningInterface.getConceptWords();
		
		log.info("dictonary size:" + names.size());
		// initialise the rootNode of Trie
		trie = new TrieNode();

		// add word to dictionary for all concept words
		for (ConceptWordModel name : names) {
			addWordInDictonary(name.getConceptWord().toUpperCase(), name.getConceptId());
		}
		names.clear();
	}

	// method to add a concept word with its conceptId in dictionary.
	public void addWordInDictonary(String word, String conceptId) {
		trie.insert(word, word, conceptId);
	}

	/*
	 * method to search concepts with given string as concept name or words
	 */
	public LinkedList<LevenshteinResults> search(String query, int maxCost) {
		if (trie == null) {
			makeDictionary();
		}

		// split the words from query
		String[] words = query.split(" ");

		// initialize the list of results
		LinkedList<LevenshteinResults> results = new LinkedList<LevenshteinResults>();

		// for every word in the query search for the match concept words and
		// return the list of matched words their concept_ids
		for (String word : words) {
			if (!word.equalsIgnoreCase(" ")) {
				word = word.toUpperCase();
				int columns = word.length();

				// initialize 1st row of the distance matrix
				int[] curRow = new int[word.length() + 1];
				for (int i = 0; i <= columns; i++) {
					curRow[i] = i;
				}

				// for every letter in this row call searchRecursive function
				// and add the respective results in final results
				for (Character letter : trie.getKeys()) {
					results.addAll(searchRecursive(word, trie.getChild(letter), letter, curRow,
							maxCost));
				}
			}
		}
		return results;
	}

	/*
	 * Recusive part of string search that will be used in every node of Trie
	 */
	private LinkedList<LevenshteinResults> searchRecursive(String word, TrieNode node,
			Character letter, int[] preRow, int maxCost) {
		LinkedList<LevenshteinResults> results = new LinkedList<LevenshteinResults>();
		int columns = word.length();

		// initialize the next row of distance matrix
		int[] curRow = new int[word.length() + 1];

		// 1st column of new row is one addition to previous
		curRow[0] = preRow[0] + 1;

		// calculation of distance for every column
		for (int i = 1; i <= columns; i++) {

			// insertion cost
			int insertCost = curRow[i - 1] + 1;

			// deletion cost
			int deleteCost = preRow[i] + 1;

			// replacement cost
			int replaceCost = preRow[i - 1];
			if (word.charAt(i - 1) != letter) {
				replaceCost = replaceCost + 1;
			}

			// cost at this column is min of above three costs
			curRow[i] = min(insertCost, deleteCost, replaceCost);
		}

		// if the cost of last column is less the maxCost and node contains a
		// word the then word and conceptIds will added in results
		if (curRow[columns] <= maxCost && node.isWord()) {
			results.add(new LevenshteinResults(node.getWord(), curRow[columns], maxCost, node
					.getConcepts()));
		}
		
		// if min of distance in this rows is less the maxCost we apply the method recursively on its children node
		if (min(curRow) <= maxCost) {

			for (Character key : node.getKeys()) {
				results.addAll(searchRecursive(word, node.getChild(key), key, curRow, maxCost));
			}
		}
		return results;
	}

	private static int min(int i, int j, int k) {
		if (i <= j && i <= k)
			return i;
		else if (j <= i && j <= k)
			return j;
		else
			return k;
	}

	private static int min(int[] array) {
		int min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (min > array[i]) {
				min = array[i];
			}
		}
		return min;
	}

	/*public static void main(String[] args) {
		makeDictionary();
		LinkedList<LevenshteinResults> temp = search("Asthma", 2);
		System.out.println(temp.size());
		for (LevenshteinResults x : temp) {
			System.out.println(x.getName() + ", " + x.getWeight() + ", " + x.getConcepts().size());
		}
	}*/
}
