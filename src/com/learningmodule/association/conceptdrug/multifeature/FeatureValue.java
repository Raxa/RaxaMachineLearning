package com.learningmodule.association.conceptdrug.multifeature;

public class FeatureValue {
	private Feature feature;
	private Object value;

	public FeatureValue(Feature feature, Object value) {
		if ((value instanceof String && feature.getFeatureType() == Feature.STRINGTYPE)
				|| (value instanceof Integer && feature.getFeatureType() == Feature.INTEGERTYPE)) {
			this.feature = feature;
			this.value = value;
		}
	}

	public double getDistance(FeatureValue f1) throws Exception {
		if (f1.getFeature().getFeatureName().equals(this.feature.getFeatureName())
				&& f1.getFeature().getFeatureType() == this.feature.getFeatureType()) {
			return this.feature.getDistance(this, f1);
		}
		throw new Exception("feature Miss Match");
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean hasSameFeature(FeatureValue value) {
		return (feature.getFeatureName().equals(value.getFeature().getFeatureName()) && feature
				.getFeatureType() == value.getFeature().getFeatureType());
	}

	@Override
	public String toString() {
		return "FeatureValue [value=" + value + "]";
	}
}
