package com.machine.learning.request;

public class LearningRequest {
	private boolean learn;
	private String learningKey;

	public LearningRequest(boolean learn, String learningKey) {
		this.learn = learn;
		this.learningKey = learningKey;
	}

	public boolean isLearn() {
		return learn;
	}

	public void setLearn(boolean learn) {
		this.learn = learn;
	}

	public String getLearningKey() {
		return learningKey;
	}

	public void setLearningKey(String learningKey) {
		this.learningKey = learningKey;
	}

	@Override
	public String toString() {
		return "LearningRequest [learn=" + learn + ", learningKey=" + learningKey + "]";
	}
}
