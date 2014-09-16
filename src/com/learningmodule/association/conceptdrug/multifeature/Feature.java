package com.learningmodule.association.conceptdrug.multifeature;

/*
 * class models the Feature for medical record
 */
public class Feature {

	public final static int STRINGTYPE = 0;
	public final static int INTEGERTYPE = 1;

	// feature name
	private String featureName;
	
	// data type of feature value
	private int featureType;
	
	// Distance Calculator function for calculating distance between feature values
	private DistanceCalculator distanceCalculator;

	// default Feature constructor for String Type
	public Feature(String featureName, int featureType) throws Exception {
		if (featureType != STRINGTYPE) {
			Exception e = new Exception("only string type is allowed with this constructor");
			throw e;
		} else {
			this.featureName = featureName;
			this.featureType = featureType;
			
			// default distance calculator for String type Feature
			this.distanceCalculator = new DistanceCalculator() {

				@Override
				public double getDistance(Object i1, Object i2) throws Exception {
					String s1 = (String) i1;
					String s2 = (String) i2;
					
					// return distanca as 1.0 if strings match else 0.0
					if (s1.equals(s2)) {
						return 1.0;
					}
					return 0.0;
				}
			};
		}
	}

	// Feature constructor for give type and distance calculator function
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
