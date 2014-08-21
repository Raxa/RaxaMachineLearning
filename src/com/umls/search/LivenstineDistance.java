package com.umls.search;

public class LivenstineDistance {

	// method to get he livenstien distance between two string
	public static int getDistance(String str1, String str2) {
		int[][] mat = new int[str1.length()][str2.length()];
		for (int i = 0; i < str1.length(); i++) {
			mat[i][0] = i;
		}
		for (int j = 0; j < str2.length(); j++) {
			mat[0][j] = j;
		}
		for (int i = 1; i < str1.length(); i++) {
			for (int j = 1; j < str2.length(); j++) {
				if (str1.charAt(i) == str2.charAt(j)) {
					mat[i][j] = mat[i - 1][j - 1];
				} else {
					mat[i][j] = min(mat[i - 1][j - 1], mat[i - 1][j], mat[i][j - 1]) + 1;
				}
			}
		}
		return mat[str1.length() - 1][str2.length() - 1];
	}

	private static int min(int x, int y, int z) {
		if (x <= y && x <= z) {
			return x;
		} else if (y <= z) {
			return y;
		} else {
			return z;
		}
	}

	/*
	 * method to the distance between string and query depending about the number of
	 * words and Livenstine distance between words
	 */
	public static double getWordMatchDistance(String str1, String query) {
		str1 = str1.toLowerCase();
		query = query.toLowerCase();
		str1.replaceAll(",", "");
		query.replaceAll(",", "");
		String[] temp1 = str1.split("\\s+");
		String[] temp2 = query.split("\\s+");
		double distance = 0;
		for (int i = 0; i < temp2.length; i++) {
			double d = getFraction(getDistance(temp2[i], temp1[0]));
			for (int j = 1; j < temp1.length; j++) {
				d = min(d, getFraction(getDistance(temp2[i], temp1[j])));
			}
			distance = distance + d;
		}
		if (temp2.length < temp1.length) {
			distance = distance + 1.0 - ((double) temp2.length / (double) temp1.length);
		}
		distance = 1.0 / (distance + 1.0);
		return distance;
	}

	public static double min(double x, double y) {
		if (x <= y)
			return x;
		else
			return y;
	}

	public static double getFraction(int x) {
		if (x >= 4)
			return 1.0;
		else
			return (double) x / 4.0;
	}

	public static void main(String[] args) {
		System.out.println(getDistance("Theophylline 60 MG Extended Release Capsule",
				"Theophylline 500 MG Extended Release Tablet"));

	}
}
