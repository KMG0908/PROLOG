package com.spring.prolog.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.spring.prolog.connection.DBConnection;
import com.spring.prolog.database.Database;
import com.spring.prolog.database.MyConstants;
import com.spring.prolog.dto.AlertDTO;
import com.spring.prolog.dto.DTO;
import com.spring.prolog.dto.UserDTO;

public class DAO {
	StringBuffer sb = new StringBuffer();
	ResultSet rs = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	Connection conn = null;
	
	private String tableName1 = "userLog";			// 사용자 로그
	private String tableName2 = "filteringWord";	// 필터링 단어
	private String tableName3 = "warningLog";		// 위험 로그
	private String[] table_column1 = {"LogFormat", "Date", "Time", "ClientIP", "UserName", "ServerIP", "Port", "Method", "UriStem", "Uriquery", "Status", "SubStatus", "Win32Status", "TimeTaken", "UserAgent"};
	private String[] table_column2 = {"id", "AttackLevel", "Problem", "filtering"};
	public String[] table_column3 = {"AttackLevel", "Problem", "Country", "Date", "Time", "ClientIP", "ServerIP", "Port"};
	
	public DAO() {
		conn = DBConnection.getInstance().getConnection();
	}
	
	public boolean initTable(){
		if(rs != null) rs = null;
		
		try {
			stmt = conn.createStatement();
			
			String sqlCT1 = "create table userLog(";
	        String sqlCT2 = "create table filteringWord(";
	        String sqlCT3 = "create table warningLog(";
	         
	        for(int i=0; i<table_column1.length; i++){
	        	if(i != table_column1.length - 1){
	         		sqlCT1 += table_column1[i] + " varchar(500) NOT NULL, ";
	        	}
	        	else{
	        		sqlCT1 += table_column1[i] + " varchar(500) NOT NULL) default charset=utf8;";
	        	}
	        }
	     			
	     	rs = stmt.executeQuery("show tables like '" + tableName1 + "'");
	     	
	     	// 테이블 생성
	     	if(!rs.next()){
	     		stmt.executeUpdate(sqlCT1);
	     	}
	     	// 테이블 존재
	     	else{
	     		stmt.executeUpdate("delete from " + tableName1);
	     	}
	     	
	     	for(int i=0; i<table_column2.length; i++){
	     		if(i != table_column2.length - 1){
	         		sqlCT2 += table_column2[i] + " varchar(500) NOT NULL, ";
	         	}
	         	else{
	         		sqlCT2 += table_column2[i] + " varchar(500) NOT NULL) default charset=utf8;";
	         	}
	         }
	     	
			rs = stmt.executeQuery("show tables like '" + tableName2 + "'");
	     	
	     	// 테이블 생성
	     	if(rs.next() == false){
	     		stmt.executeUpdate(sqlCT2);
	     	}
	     	// 테이블 존재
	     	else{
	     		stmt.executeUpdate("delete from " + tableName2);
	     	}
	     	
	     	
	     	for(int i=0; i<table_column3.length; i++){
	         	if(i != table_column3.length - 1){
	         		sqlCT3 += table_column3[i] + " varchar(500) NOT NULL, ";
	         	}
	         	else{
	         		sqlCT3 += table_column3[i] + " varchar(500) NOT NULL) default charset=utf8;";
	         	}
	        }
	     	
			rs = stmt.executeQuery("show tables like '" + tableName3 + "'");
	     	
	     	// 테이블 생성
	     	if(rs.next() == false){
	     		stmt.executeUpdate(sqlCT3);
	     	}
	     	// 테이블 존재
	     	else{
	     		stmt.executeUpdate("delete from " + tableName3);
	     	}
	     	
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public boolean parseLog(File file) throws IOException, GeoIp2Exception {
       	BufferedReader br = null;      
        InputStreamReader isr = null;   
        FileInputStream fis = null;
        
        File dbFile = new File(MyConstants.DATABASE_CITY_PATH);
        DatabaseReader reader = new DatabaseReader.Builder(dbFile).build();

        try {	
        	// 파일을 읽어들여 File Input 스트림 객체 생성
        	System.out.println(System.getProperty("user.dir") + "\\" + file.getPath());
            fis = new FileInputStream(System.getProperty("user.dir") + "\\" + file);
         
	        // File Input 스트림 객체를 이용해 Input 스트림 객체를 생서하는데 인코딩을 euc-kr로 지정
	        isr = new InputStreamReader(fis, "euc-kr");
	         
	        // Input 스트림 객체를 이용하여 버퍼를 생성
	        br = new BufferedReader(isr); 
	        
    		String temp = "";
			while((temp = br.readLine()) != null){
				String log_format = "";
				String date = "";
            	String time = "";
            	String client_ip = "";
            	String user_name = "";
            	String server_ip = "";
            	String port = "";
            	String method = "";
            	String uri_stem = "";
            	String uri_query = "";
            	String status = "";
            	String substatus = "";
            	String win32_status = "";
            	String time_taken = "";
            	String user_agent = "";
            	int log = 0;
            	
            	if(temp.length() == 0) continue;
            	if(temp.startsWith("#"))continue;
            	
            	String arr[] = temp.split("\\s+");
            	
            	if(arr[0].contains(".")){
            		if(arr.length == 15) log = 2;	//IIS Format
            		else if(temp.contains("[")) log = 3;	//NCSA Format
            	}
            	else{
            		log = 1;	//W3C Format
            	}
            	
            	switch(log){
            	case 1:		//W3C Format
            		log_format = "W3C";
            		date = "EMPTY";
                	time = "EMPTY";
                	client_ip = "EMPTY";
                	user_name = "EMPTY";
                	server_ip = "EMPTY";
                	port = "EMPTY";
                	method = "EMPTY";
                	uri_stem = "EMPTY";
                	uri_query = "EMPTY";
                	status = "EMPTY";
                	substatus = "EMPTY";
            		win32_status = "EMPTY";
            		time_taken = "EMPTY";
                	user_agent = "EMPTY";
                	
            		String http_status[] = {"100", "101", "102", "200", "201", "202", "203", "204", "205", "206", "207", "208", "226", "300", "301", "302", "303", "304", "305", "307", "308", "400", "401", "402", "403", "404", "405", "406", "407", "408", "409", "410", "411", "412", "413", "414", "415", "416", "417", "418", "420", "422", "423", "424", "425", "426", "428", "429", "431", "444", "449", "450", "451", "494", "495", "496", "497", "499", "500", "501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "511", "598", "599"};
                	String http_substatus[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "100"};
                	String win32[] = {"0", "2", "3", "5", "64", "995", "1236", "121", "22"};
                	String win32_notSame[] = {"64", "995", "1236", "121", "22"};	// http_substatus code랑 겹치지 않는 부분
            		
            		String date_pattern = "([0-9]{4})-([0-9]{2})-([0-9]{2})";
            		String time_pattern = "([0-9]{2}):([0-9]{2}):([0-9]{2})";
            		String ip_pattern = "([0-9]{1,3}).([0-9]{1,3}).([0-9]{1,3}).([0-9]{1,3})";
            		String stem_pattern = "^/.*";
            		String query_pattern = "^.+=.+";
            		String agent_pattern = "^(Mozilla).*";
            		String num_pattern = "^[0-9]*$";
                	
            		boolean b_date, b_time, b_ip, b_stem, b_query, b_agent, b_num;
            		
                	int ipNum = 0;
                	int status_flag = 0;	// http_substatus의 0과 win32의 0 순서로 구분
                	int portORtime = 0;
                	
            		for(int i=0; i<arr.length; i++){
                    	boolean isContinue = false;
                    	
            			b_date = Pattern.matches(date_pattern, arr[i]);
            			if(b_date){
            				date = arr[i];
            				continue;
            			}
            			
            			b_time = Pattern.matches(time_pattern, arr[i]);
            			if(b_time){
            				time = arr[i];
            				continue;
            			}
            			
            			b_ip = Pattern.matches(ip_pattern, arr[i]);
            			if(b_ip){
            				if(ipNum == 0){
            					client_ip = arr[i];
            					ipNum++;
            					b_ip = false;
            				}
            				else{
            					server_ip = arr[i];
            				}
            				continue;
            			}
            			      	
                    	b_stem = Pattern.matches(stem_pattern, arr[i]);
                    	if(b_stem){
                    		uri_stem = arr[i];
                    		continue;
                    	}
                    	
                    	b_query = Pattern.matches(query_pattern, arr[i]);
                    	if(b_query){
                    		uri_query = arr[i];
                    		continue;
                    	}
                    	
                    	b_agent = Pattern.matches(agent_pattern, arr[i]);
                    	if(b_agent){
                    		user_agent = arr[i];
                    		continue;
                    	}
                    	
                    	if(arr[i].equals("GET") || arr[i].equals("POST")){
                    		method = arr[i];
                    		continue;
                    	}
                    	
                    	for(int j=0; j<http_status.length; j++){
                    		if(arr[i].equals(http_status[j])){
                    			status = arr[i];
                    			isContinue = true;
                    			break;
                    		}
                    	}
                    	
                    	if(status_flag == 0){
                    		for(int j=0; j<http_substatus.length; j++){
                            	if(arr[i].equals(http_substatus[j])){
                            		substatus = arr[i];
                            		status_flag = 1;
                            		isContinue = true;
                            		break;
                            	}
                            }
                    	}
                    	else{
                    		for(int j=0; j<win32.length; j++){
                        		if(arr[i].equals(win32[j])){
                        			win32_status = arr[i];
                        			isContinue = true;
                        			break;
                        		}
                    		}
                    	}
                    	
                    	for(int j=0; j<win32_notSame.length; j++){
                    		if(arr[i].equals(win32_notSame[j])){
                    			win32_status = arr[i];
                    			isContinue = true;
                    			break;
                    		}
                    	}
                    	if(isContinue) continue;
                    	
                    	b_num = Pattern.matches(num_pattern, arr[i]);
                    	if(b_num){
                    		if(portORtime == 0){
                    			port = arr[i];
                    			portORtime++;
                    		}
                    		else time_taken = arr[i];
                    	}
                    }
            		break;
            	case 2:		//IIS Format
            		log_format = "IIS";
            		date = arr[2];
                	time = arr[3];
                	client_ip = arr[0];
                	user_name = arr[1];
                	server_ip = arr[6];
                	port = "EMPTY";
                	method = arr[12];
                	uri_stem = arr[13];
                	uri_query = "EMPTY";
                	status = arr[10];
                	substatus = "EMPTY";
            		win32_status = arr[11];
            		time_taken = arr[7];
                	user_agent = "EMPTY";
            		break;
            	case 3:		//NCSA Format
            		String DT[] = arr[3].split("\\[|/|:");
            		String month = "";
            		String[] month_arr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
            		String[] month_num = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
            		for(int i=0; i<month_arr.length; i++){
            			if(DT[2].equals(month_arr[i])) month = month_num[i];
            		}
            		String request_type[] = arr[5].split("\"");
            		log_format = "NCSA";
            		date = DT[3] + "-" + month + "-" + DT[1];
                	time = DT[4] + ":" + DT[5] + ":" + DT[6];
                	client_ip = arr[0];
                	user_name = arr[2];
                	server_ip = "EMPTY";
                	port = "EMPTY";
                	method = request_type[1];
                	uri_stem = arr[6];
                	uri_query = "EMPTY";
                	status = arr[8];
                	substatus = "EMPTY";
            		win32_status = "EMPTY";
            		time_taken = "EMPTY";
                	user_agent = "EMPTY";
            		break;
            	case 0:
            		System.out.println("오류!");
            		break;
            	}
            	
            	String sqlInsert1 = "insert into userLog values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            	
            	pstmt = conn.prepareStatement(sqlInsert1);
            	
            	pstmt.setString(1, log_format);
            	pstmt.setString(2, date);
            	pstmt.setString(3, time);
            	pstmt.setString(4, client_ip);
            	pstmt.setString(5, user_name);
            	pstmt.setString(6, server_ip);
            	pstmt.setString(7, port);
            	pstmt.setString(8, method);
            	pstmt.setString(9, uri_stem);
            	pstmt.setString(10, uri_query);
            	pstmt.setString(11, status);
            	pstmt.setString(12, substatus);
            	pstmt.setString(13, win32_status);
            	pstmt.setString(14, time_taken);
            	pstmt.setString(15, user_agent);
            	
            	pstmt.executeUpdate();
            	
            	/*String sqlInsert1 = "insert into userLog values('" + log_format + "', '" 
            			+ date + "', '" + time + "', '" + client_ip + "', '"
            			+ user_name + "', '" + server_ip + "', '" + port + "', '"
            			+ method + "', '" + uri_stem + "', '" + uri_query + "', '"
            			+ status + "', '" + substatus + "', '" + win32_status + "', '"
            			+ time_taken + "', '" + user_agent + "');";
            	stmt.executeUpdate(sqlInsert1);	//테이블1*/				
			}
			
			br.close();
			isr.close();
			fis.close();
			
			String path = "C:/Users/soldesk/Documents/prolog/prolog/txt/filteringWord.txt";
			File filteringFile = new File(path);
			BufferedReader br_ = new BufferedReader(new InputStreamReader(new FileInputStream(filteringFile), "utf-8"));
			String line = "";
			int flag = 0;
			
			while((line = br_.readLine()) != null) {
				if(flag == 0) {
					flag = 1;
					continue;
				}
				String strArray[] = line.split("\t");
				
				String sqlInsert2 = "insert into filteringWord values(?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sqlInsert2);
				pstmt.setString(1, strArray[0]);
				pstmt.setString(2, strArray[1]);
				pstmt.setString(3, strArray[2]);
				pstmt.setString(4, strArray[3]);
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement("select * from filteringWord ");
			ResultSet rs1 = pstmt.executeQuery();

	       /*	Statement stmt2 = this.conn.createStatement();
	       	ResultSet rs1 = stmt2.executeQuery("select * from " + tableName2);*/
			int k = 0;
	        while(rs1.next()){
	        	String attack_level = rs1.getString("AttackLevel");
	        	String pr = rs1.getString("Problem");
	           	String word = rs1.getString("filtering");
	           	//String problem = rs1.getString("ProblemName");
	           	
	           	if(word.equals("/images")) {
	           		word = "%" + word + "%";
	           		pstmt = conn.prepareStatement("select * from userLog where UriStem like ? and Status='404'");
	           	}
	           	else if(word.equals("/Script")) {
	           		word = "%" + word + "%";
	           		pstmt = conn.prepareStatement("select * from userLog where UriStem like ? and Status='404'");
	           	}
	           	else if(word.equals("union%20")) {
	           		word = word.replaceAll("%", "#%");
	           		word = "%" + word + "%";
	           		pstmt = conn.prepareStatement("select * from userLog where UriStem like ? escape '#' and not UriStem like '%union#%20select#%20%' escape '#'");
	           	}
	           	else if(word.contains("%")) {
	           		word = word.replaceAll("%", "#%");
	           		word = "%" + word + "%";
	           		pstmt = conn.prepareStatement("select * from userLog where UriStem like ? escape '#'");
	           	}
	           	else {
	           		word = "%" + word + "%";
	           		pstmt = conn.prepareStatement("select * from userLog where UriStem like ?");
	           	}
	           	
	           	pstmt.setString(1, word);
	           	ResultSet rs2 = pstmt.executeQuery();
	           	
	           	/*Statement stmt3 = conn.createStatement();
	           	Statement stmt4 = conn.createStatement();
	           	ResultSet rs2 = stmt3.executeQuery("select * from userLog where UriStem regexp \"" + word + "\";");
	           	
	           	if(word.equals("/images")){
	           		rs2 = stmt3.executeQuery("select * from userLog where UriStem regexp \"/images\" and Status=\"404\";");
	           	}
	           	else if(word.equals("/Script")){
	           		rs2 = stmt3.executeQuery("select * from userLog where UriStem regexp \"Script\" and Status=\"404\";");
	           	}*/
	           	
	           	String sqlInsert3 = "insert into warningLog values(?, ?, ?, ?, ?, ?, ?, ?)";
	           	//PreparedStatement pstmt3 = conn.prepareStatement(sqlInsert3);
	           	pstmt = conn.prepareStatement(sqlInsert3);
	           	
	           	while(rs2.next()){
	           		for(int i=1; i<table_column3.length; i++){
	           			if(i == 1){
	           				pstmt.setString(1, attack_level);
	           				pstmt.setString(2, pr);
	           			}
	           			else if(i == 2){
	           				String countryName = "";
	           				try{
	           	            	InetAddress ipAddress = InetAddress.getByName(rs2.getString("ClientIP"));
	           	                CityResponse response = reader.city(ipAddress);

	           	                Country country = response.getCountry();
	           	                countryName = country.getName();
	           	                //System.out.println("Country IsoCode: "+ country.getIsoCode()); 
	           	                //System.out.println("Country Name: "+ country.getName());
	           	            }catch(AddressNotFoundException e){
	           	            	countryName = "LOCAL";
	           	            }
	           				pstmt.setString(3, countryName);
	           				//sqlInsert3 += countryName + "', '";
	           			}
	           			/*else if(i != table_column3.length - 1){
	           				pstmt.setString(i-1, rs2.getString(table_column3[i]));
	                   		sqlInsert3 += rs2.getString(table_column3[i]) + "', '";
	                   	}
	                   	else{
	                   		sqlInsert3 += rs2.getString(table_column3[i]) + "');";
	                   	}*/
	           			else {
	           				pstmt.setString(i+1, rs2.getString(table_column3[i]));
	           			}
	               	}
	           		pstmt.executeUpdate();
	           		//stmt4.executeUpdate(sqlInsert3);
	           	}
	        }
	        //stmt2.close();
	        rs1.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int loginCheck(String id, String passwd){
		if(rs != null) rs = null;
		
		int flag = -1;
		
		try {
			pstmt = conn.prepareStatement("select * from login.login where Email=? and Passwd=?");
			pstmt.setString(1, id);
			pstmt.setString(2, passwd);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				flag = 1;
			}
			else flag = 0;
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return flag;
	 }
	
	public int idCheck(UserDTO dto){
		Database database = new Database();
		
		if(rs != null) rs = null;
		
		int flag = -1;
		
		try {
			pstmt = conn.prepareStatement("select * from login.login where Email=?");
			pstmt.setString(1, dto.getEmail());
			rs = pstmt.executeQuery();
			rs.last();
			
			int idNum = rs.getRow();
	        if(idNum == 0){
	        	flag = 1;
	        	
	        	pstmt = conn.prepareStatement("insert into login.login values(?, ?, ?, ?)");
	        	pstmt.setString(1, dto.getEmail());
	        	pstmt.setString(2, dto.getPasswd());
	        	pstmt.setString(3, dto.getLastName());
	        	pstmt.setString(4, dto.getFirstName());
	        	pstmt.executeUpdate();
	        	
	        	String dbEmail = dto.getEmail();
	        	dbEmail = dbEmail.replace("@", "");
	        	String dbN[] = dbEmail.split("\\.");
	        	
	        	database.createDB(dbN[0]);
	        }
	        else{
	        	flag = 0;
	        }
			
			/*rs = pstmt.executeQuery("select * from login where Email=\'" + getEmail() +"\'");
			rs.last();
	        int idNum = rs.getRow();
	        if(idNum == 0){
	        	flag = 1;
	        	stmt.executeUpdate("insert into login values('" + getEmail() + "', '" + getPasswd() + "', '" + getFirstName() + "', '" + getLastName() + "');");
	        	String dbEmail = getEmail();
	        	dbEmail = dbEmail.replace("@", "");
	        	String dbN[] = dbEmail.split("\\.");
	        	rs = stmt.executeQuery("show databases like '" + dbN[0] + "'");
		    	if(rs.next() == false){
		    		stmt.executeUpdate("create database " + dbN[0]);
		    	}
		    	rs = stmt.executeQuery("show databases like '" + dbN[0] + "alert'");
			    if(rs.next() == false){
			    	stmt.executeUpdate("create database " + dbN[0] + "alert");
			    }
	        }
	        else{
	        	flag = 0;
	        }*/
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public int alertCheck(String dbName) {
		if(rs != null) rs = null;
		
		int row = 0;
        
        try {
        	stmt = conn.createStatement();
			rs = stmt.executeQuery("show tables in " + dbName + " like 'warningLog'");
			if(rs.next()){
	        	rs = stmt.executeQuery("select * from " + dbName + ".warningLog");
	        	rs.last();
	        	row = rs.getRow();
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return row;
	}
	
	/*public int getCheckedLog() {
		if(rs != null) rs = null;
		
		int row = 0;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from userLog ");
			rs.last();
			row = rs.getRow();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return row;
	}
	
	public int getDetectedLog() {
		if(rs != null) rs = null;
		
		int row = 0;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from warningLog ");
			rs.last();
			row = rs.getRow();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return row;
	}
	
	public int getDangerousLog() {
		if(rs != null) rs = null;
		
		int row = 0;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from warningLog where AttackLevel='4' or AttackLevel='5' or AttackLevel='6'");
			rs.last();
			row = rs.getRow();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return row;
	}*/
	
	public int getRowNum(String sql) {
		if(rs != null) rs = null;
		
		int row = 0;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rs.last();
			row = rs.getRow();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return row;
	}
	
	public ArrayList<DTO> getAllData(String sql){
		if(rs != null) rs = null;
		
		ArrayList<DTO> list = new ArrayList<DTO>();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				do {
					list.add(new DTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
				} while(rs.next());
			}
			else {
				rs = stmt.executeQuery("select * from warningLog");
				while(rs.next()) {
					list.add(new DTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public void deleteTable(String dbName) {
		if(rs != null) rs = null;
		
    	String dbEmail = dbName.replace("@", "");
    	String dbN[] = dbEmail.split("\\.");
    	
    	String sql = "delete from " + dbN[0] + ".";
    	
    	try {
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery("show tables like '" + tableName1 + "'");
	     	if(!rs.next()){
	     		stmt.executeUpdate(sql + tableName1);
	     	}
	     	
	     	rs = stmt.executeQuery("show tables like '" + tableName2 + "'");
	     	if(!rs.next()){
	     		stmt.executeUpdate(sql + tableName2);
	     	}
	     	
	     	rs = stmt.executeQuery("show tables like '" + tableName3 + "'");
	     	if(!rs.next()){
	     		stmt.executeUpdate(sql + tableName3);
	     	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
}
