package com.spring.prolog.back;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimerTask;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.spring.prolog.connection.DBConnection;
import com.spring.prolog.dao.AlertDAO;
import com.spring.prolog.dao.DAO;
import com.spring.prolog.dto.AlertDTO;

public class WeeklySearch extends TimerTask{
	private String email;
	public long size1 = 0;
	public long size2 = 0;

	@Override 
	public void run() {
		System.out.println(new Date() + "	WeeklySearch!");
		
		File dir = new File("C:/inetpub/logs/LogFiles");
		File defaultFile = null;
	  	boolean flag = false;
	  	
	    if(dir.isDirectory()){
	    	String filePath = "";
	  		File[] dirList = dir.listFiles();
	  		if(dirList.length != 0){
	  			String dirPath = dirList[0].toString();
	  			File subDir = new File(dirPath);
	  			File[] subDirList = subDir.listFiles();
	  			if(subDirList.length != 0){
	  				filePath = subDirList[subDirList.length - 1].toString();
					defaultFile = new File(filePath);
	  				flag = true;
	  			}
	  		}
	  	}
	    
	    if(flag){
	    	Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss"); 
			
			String dbEmail = getEmail();
			dbEmail = dbEmail.replace("@", "");
			String dbN[] = dbEmail.split("\\.");
			
			System.out.println("aaa");
			AlertDAO dao = new AlertDAO();
			System.out.println("ccc");
			dao.initTable(dbN[0]);
			System.out.println("ddd");
			try {
				dao.parseLog(defaultFile);
			} catch (IOException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			} catch (GeoIp2Exception e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			}
			System.out.println("bbb");
	        
	        int checkedLog = dao.getRowNum("select * from " + dbN[0] + "alert.userLog");
			int detectedLog = dao.getRowNum("select * from " + dbN[0] + "alert.warningLog");
			int danger1 = dao.getRowNum("select * from " + dbN[0] + "alert.warningLog where AttackLevel='1' or AttackLevel='2' or AttackLevel='3'");		        
			int danger2 = dao.getRowNum("select * from " + dbN[0] + "alert.warningLog where AttackLevel='4' or AttackLevel='5' or AttackLevel='6'");
			
			if(detectedLog != 0){
				
				int rowNum = dao.getRowNum("select * from alert.alert where Email='" + getEmail() + "'");
			    
			    if(rowNum != 0){
			    	dao.deleteAlret(dbN[0]);
			    }
			    
			    AlertDTO alertDTO = new AlertDTO(getEmail(), String.valueOf(checkedLog), String.valueOf(detectedLog), String.valueOf(danger1), String.valueOf(danger2), sdf.format(date));
							        
				dao.insertAlert(alertDTO);
				
				sendMail(getEmail(), checkedLog, detectedLog);
			} 
			
			try {
				dao.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*try {
				Desktop.getDesktop().browse(new URI("http://localhost:8080/strap/main")); 
			} catch (IOException e) { 
				e.printStackTrace(); 
			} catch (URISyntaxException e) { 
				e.printStackTrace(); 
			}*/
	    }
	}
	
	private void sendMail(String toEmail, int checkedLog, int detectedLog) {
		// TODO 자동 생성된 메소드 스텁
		String host     = "smtp.naver.com";
		final String user   = "pro_log@naver.com";
		final String password  = "skroejd123@";

		String to     = toEmail;

		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
				}
			});

		  
		// Compose the message	  
		try {	  
			MimeMessage message = new MimeMessage(session);		  
			message.setFrom(new InternetAddress(user));		  
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			message.setSubject("[PRO|LOG]");
			/*MimeMultipart multipart = new MimeMultipart();
			   
			MimeBodyPart part = new MimeBodyPart();
			part.setContent("<font size=\"5\" color=\"red\"> 위험합니다. </font><br><br> 전체 로그 <strong>" + checkedLog + " </strong>중 <font color=\"red\">" + detectedLog +"</font>개의 위험이 발견되었습니다. 확인이 필요합니다.", "text/html; charset=utf-8");
			multipart.addBodyPart(part);
			    
			part = new MimeBodyPart();
			FileDataSource fds = new FileDataSource("C:/Users/kmg/Desktop/표지.jpg");
			part.setDataHandler(new DataHandler(fds));
			part.setFileName("AAA.jpg");
			multipart.addBodyPart(part);
			   
			message.setContent(multipart);*/
			   
			message.setContent("<font size=\"5\" color=\"red\"> 위험합니다. </font><br><br> 전체 로그 <strong>" + checkedLog + " </strong>중 <font color=\"red\">" + detectedLog +"</font>개의 위험이 발견되었습니다. 확인이 필요합니다.", "text/html; charset=utf-8");
			
			// send the message
			Transport.send(message);
			System.out.println("message sent successfully...");

		} catch (MessagingException e) {
			e.printStackTrace();	
		}
	}
	
	public String getEmail() {
        return email;
    }
	
    public void setEmail(String email) {
    	this.email = email;
    }
}
