package com.machine.learning.request;

public class SearchAttribute {
	private String name;
	private Object value;

	public SearchAttribute(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "SearchAttribute [name=" + name + ", value=" + value + "]";
	}
}
