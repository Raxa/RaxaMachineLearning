package com.machine.learning.request;

public class Request {
	LearningRequest learningRequest;
	SearchQueryRequest searchRequest;

	public LearningRequest getLearningRequest() {
		return learningRequest;
	}

	public void setLearningRequest(LearningRequest learningRequest) {
		this.learningRequest = learningRequest;
	}

	public SearchQueryRequest getSearchRequest() {
		return searchRequest;
	}

	public void setSearchRequest(SearchQueryRequest searchRequest) {
		this.searchRequest = searchRequest;
	}

	@Override
	public String toString() {
		return "Request [learningRequest=" + learningRequest + ", searchRequest=" + searchRequest
				+ "]";
	}
}
