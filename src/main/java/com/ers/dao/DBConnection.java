package com.ers.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;


public class DBConnection {
	public final Logger log = Logger.getLogger(DBConnection.class);
	ClassLoader classLoader = getClass().getClassLoader();
	InputStream is;
	Properties p = new Properties();
	
	public DBConnection() {
		is = classLoader.getResourceAsStream("connection.properties");
		try {
			p.load(is);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		}
	}
	
	public Connection getDBConnection() throws SQLException {
		final String URL = p.getProperty("url");
		final String USERNAME = p.getProperty("username");
		final String PASSWORD = p.getProperty("password");
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}
}
