<%@page import="com.spring.prolog.dao.DAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id = "";
	
	id = (String)session.getAttribute("id");
	
	String dbEmail = id.replace("@", "");
  	String dbN[] = dbEmail.split("\\.");
  	String dbName = dbN[0] + "alert";
  	
  	DAO dao = new DAO();
  	int row = dao.alertCheck(dbName);
    if(row == 0){
    	%>
    	<script>
    		alert("알림 데이터가 없습니다.");
    		history.go(-1);
    	</script>
    	<%
    }
    else{
    	response.sendRedirect("nhome2");
    }
%>