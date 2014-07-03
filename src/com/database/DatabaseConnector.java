package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;


/*
 * Class that implements the methods related to the database connection
 */

public class DatabaseConnector {
	private static Connection connection = null;
	private static Logger log = Logger.getLogger(DatabaseConnector.class);
	/*
	 * make connection as soon as the class is loaded
	 */
	static {
		makeConnection();
	}
	
	/*
	 * method to make the connection with the database
	 */
	public static void makeConnection() {

		try {
			PropertiesReader.load();
			// get the connection with the database
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(PropertiesReader.getUrl(),
					PropertiesReader.getUser(), PropertiesReader.getPassword());
			System.out.println("MySQL JDBC Driver Registered!");
			log.debug("MySQL JDBC Driver Registered!");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			log.fatal(e);
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.fatal(e);
		}
	}

	/*
	 * method to check if database is connected
	 */
	public static boolean isConnected() {
		if (connection != null) {
			try {
				return !connection.isClosed();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	/*
	 * method the close the connection with database.
	 */
	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
	}

	public static Connection getConnection() {
		if(connection == null) {
			makeConnection();
		}
		return connection;
	}
}
