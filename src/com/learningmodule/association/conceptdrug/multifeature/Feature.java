package com.learningmodule.association.conceptdrug.multifeature;


public class Feature {

	public final static int STRINGTYPE = 0;
	public final static int INTEGERTYPE = 1;

	private String featureName;
	private int featureType;
	private DistanceCalculator distanceCalculator;

	public Feature(String featureName, int featureType) throws Exception {
		if (featureType != STRINGTYPE) {
			Exception e = new Exception("only string type is allowed with this constructor");
			throw e;
		} else {
			this.featureName = featureName;
			this.featureType = featureType;
			this.distanceCalculator = new DistanceCalculator() {

				@Override
				public double getDistance(Object i1, Object i2) throws Exception {
					String s1 = (String) i1;
					String s2 = (String) i2;
					if (s1.equals(s2)) {
						return 1.0;
					}
					return 0.0;
				}
			};
		}
	}

	public Feature(String featureName, int featureType, DistanceCalculator distanceCaluculator)
			throws Exception {
		if (featureType == STRINGTYPE || featureType == INTEGERTYPE) {
			this.featureName = featureName;
			this.featureType = featureType;
			this.distanceCalculator = distanceCaluculator;
		} else {
			Exception e = new Exception(
					"argument for featureType must be either STRINGTYPE or INTEGERTYPE");
			throw e;
		}
	}

	public static interface DistanceCalculator {
		public double getDistance(Object i1, Object i2) throws Exception;
	}

	public String getFeatureName() {
		return featureName;
	}

	public boolean isStringType() {
		if (featureType == STRINGTYPE)
			return true;
		return false;
	}

	public boolean isIntegerType() {
		if (featureType == INTEGERTYPE)
			return true;
		return false;
	}
	
	public int getFeatureType() {
		return this.featureType;
	}
	
	public double getDistance(FeatureValue f1, FeatureValue f2) throws Exception {
		return distanceCalculator.getDistance(f1.getValue(), f2.getValue());
	}
}
