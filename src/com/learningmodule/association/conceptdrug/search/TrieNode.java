package com.learningmodule.association.conceptdrug.search;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/*
 * Trie datastructure that will be used for constructing the 
 * dictionary for Levenshtien Search.
 */

public class TrieNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// variable is null is node does not represent
	private String word;
	
	// hashtable with key as charecter and value as childrens of this node
	private Hashtable<Character, TrieNode> children;
	
	// HashSet of concepts with this word as
	private HashSet<Integer> conceptIds;
	
	public TrieNode() {
		this.word = null;
		children = new Hashtable<Character, TrieNode>();
		conceptIds = new HashSet<Integer>();
	}
	
	public boolean isWord() {
		return word!=null;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getWord() {
		return this.word;
	}
	
	public void addChild(Character x) {
		children.put(x, new TrieNode());
	}
	
	public TrieNode getChild(Character x) {
		return children.get(x);
	}

	public Set<Character> getKeys() {
		return this.children.keySet();
	}
	
	public Set<Integer> getConcepts() {
		return this.conceptIds;
	}
	
	/*
	 * method to insert a word with suffix at this node
	 */
	public void insert(String suffix, String word, Integer conceptId) {
		if(suffix.length() > 0) {
			if(!children.containsKey(suffix.charAt(0))) {
				addChild(suffix.charAt(0));
			}
			children.get(suffix.charAt(0)).insert(suffix.substring(1), word, conceptId);
		} else {
			this.setWord(word);
			conceptIds.add(conceptId);
		}
	}
}
