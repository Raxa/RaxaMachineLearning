package com.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/*
 * class to used to read the database properties from file "database.properties"
 */

public class PropertiesReader {

	private static String user = null;
	private static String password = null;
	private static String url = null;
	private static Logger log = Logger.getLogger(PropertiesReader.class);

	// method to get the UserId of database
	public static String getUser() {
		return user;
	}

	// method to get the password of database
	public static String getPassword() {
		return password;
	}

	// method to get the Url String of database
	public static String getUrl() {
		return url;
	}

	/*
	 * method to load the database properties
	 */

	public static void load() {

		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = PropertiesReader.class.getClassLoader().getResourceAsStream(
					"config/database.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			url = prop.getProperty("databaseURL");
			user = prop.getProperty("dbUser");
			password = prop.getProperty("dbPassword");

		} catch (IOException ex) {
			ex.printStackTrace();
			log.error(ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
			} else {
				log.error("empty properties file");
			}
		}
	}

	public static void main(String[] args) {
		load();
		System.out.println(getUrl() + ", " + getUser() + ", " + getPassword());
	}
}
