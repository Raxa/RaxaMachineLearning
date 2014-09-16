package com.machine.learning.request;

import java.util.Arrays;

public class SearchQueryRequest {
	private String query;
	private SearchAttribute[] features;

	public void setFeature(SearchAttribute[] features) {
		this.features = features;
	}
	
	public SearchAttribute[] getFeatures() {
		return this.features;
	}
	
	public SearchQueryRequest(String query, SearchAttribute[] features) {
		super();
		this.query = query;
		this.features = features;
	}

	public SearchQueryRequest(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String toString() {
		return "SearchQueryRequest [query=" + query + ", features=" + Arrays.toString(features)
				+ "]";
	}

}
