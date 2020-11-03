package com.spring.prolog.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.spring.prolog.connection.DBConnection;

public class Database {
	StringBuffer sb = new StringBuffer();
	ResultSet rs = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	Connection conn = null;
	
	public Database() {
		conn = DBConnection.getInstance().getConnection();
		
		try {
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery("show databases like 'login'");
			if(!rs.next()) {
				stmt.executeUpdate("create database login");
				stmt.executeUpdate("create table login.login(Email varchar(30), Passwd varchar(30), LastName varchar(15), FirstName varchar(15))");
			}
			
			rs = stmt.executeQuery("show databases like 'alert'");
			if(!rs.next()) {
				stmt.executeUpdate("create database alert");
				stmt.executeUpdate("create table alert.alert(Email varchar(30), checkedLog varchar(5), detectedLog varchar(5), danger1 varchar(5), danger2 varchar(5), Date varchar(30))");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rs = null;
		
	}
	
	public void createDB(String dbName) {
		try {
			stmt = conn.createStatement();
			
			// db없으면 생성
			rs = stmt.executeQuery("show databases like '" + dbName + "'");
	    	if(!rs.next()){
	    		stmt.executeUpdate("create database " + dbName);
	    	}
	    	
	    	if(!dbName.equals("test")){
	    		rs = stmt.executeQuery("show databases like '" + dbName + "alert'");
		    	if(!rs.next()){
		    		stmt.executeUpdate("create database " + dbName + "alert");
		    	}
	    	}
	    	
	    	rs = null;
	    	
	    	stmt.executeQuery("use mysql");
			rs = stmt.executeQuery("select host, user, authentication_string from user where user = 'user'");
			while(!rs.next()) {
				stmt.executeUpdate("create user 'user'@'localhost' identified by '1234'");
				stmt.executeUpdate("grant all on *.* to 'user'@'localhost'");
			}
	    	
	    	DBConnection.setDBNAME(dbName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
    	try {
    		if(rs!=null) rs.close();
        	if(conn!=null) conn.close();
        	if(stmt !=null) stmt.close();
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
