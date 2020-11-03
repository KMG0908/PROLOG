package com.spring.prolog.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	private static DBConnection dc = null;
	private static Connection conn = null;

	private static String DRIVER = "com.mysql.jdbc.Driver";
	public static String URL = "jdbc:mysql://localhost:3306";
	private static String DBNAME = "";
	private static String OPTIONS = "?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
	
	final String USER = "root";
	final String PASSWORD = "1234";
	
	private DBConnection() {}
	
	public static DBConnection getInstance() {
		if(dc == null) dc = new DBConnection();
		
		return dc;
	}
	
	public Connection getConnection() {
		if(conn == null) {
			try {
				Class.forName(DRIVER);
				
				if(DBNAME != "") {
					URL += "/" + DBNAME + OPTIONS; 
				}
				else {
					URL += OPTIONS;
				}
				
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				System.out.println("conn: " + conn);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return conn;
	}
	
	public static void setDBNAME(String dBNAME) {
		DBNAME = dBNAME;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate("use " + dBNAME);
			if(stmt != null) stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getDBNAME() {
		return DBNAME;
	}
}
