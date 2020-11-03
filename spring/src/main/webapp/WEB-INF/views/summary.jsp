<!DOCTYPE html>
<%@page import="com.spring.prolog.back.SqlInjection"%>
<%@page import="com.spring.prolog.connection.DBConnection"%>
<%@page import="com.spring.prolog.dto.DTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.spring.prolog.database.MyConstants"%>
<%@page import="com.spring.prolog.dao.DAO"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.FileNotFoundException"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.io.InputStreamReader"%>
<%@ page import="java.util.StringTokenizer"%>
<%@ page import="java.util.regex.Pattern"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.maxmind.geoip2.DatabaseReader" %>
<%@ page import="com.maxmind.geoip2.exception.AddressNotFoundException" %>
<%@ page import="com.maxmind.geoip2.exception.GeoIp2Exception" %>
<%@ page import="com.maxmind.geoip2.model.CityResponse" %>
<%@ page import="com.maxmind.geoip2.record.Country" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="java.util.Locale" %>


<html lang="ko">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>PRO|LOG</title>

    <!-- Bootstrap Core CSS -->
    <link href="resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/css/dataTables.bootstrap.min.css" rel="stylesheet">	

    <!-- MetisMenu CSS -->
    <link href="resources/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="resources/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="resources/vendor/morrisjs/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="resources/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    
    <link href="resources/css/custom.css" rel="stylesheet" type="text/css">
	
		
	
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

    <div id="wrapper">
        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="<c:url value = '/nhome'/>" style="font-weight: bold;"><i class="fa fa-pinterest-p" aria-hidden="true"></i>ro|Log</a>
            </div>
            <!-- /.navbar-header -->
            <!-- /.navbar-top-links -->
			<div id="mySidenav" class="sidenav">
		  <a href="javascript:void(0)" class="closebtn" onclick="closeNav()" style="display: block;">&times;</a>
			    <form id="search_form" name="search_form" role="form">
			    
			    <div class="form-group">
					<div>
                       <label class="label-color">출발지 IP</label>
                     </div>
                     <input name="client_ip" id="client_ip" class="form-control-search">
                 </div>
                 <div class="form-group">
                       <p></p>
                       <label class="label-color">목적지 IP</label>
                       <input name="server_ip" id="server_ip" class="form-control-search">                               
                 </div>
                  <div class="form-group">
                       <p></p>
                       <label class="label-color">목적지 PORT</label>
                       <input name="port" id="port" class="form-control-search">                               
                 </div>
                 <div class="form-group-select2">
                            <label class="label-color-select">위험 레벨</label>
                               <select name="level" id="level" class="form-control" style="width: 85%;">
                                   <option value="0">선택하세요</option>
                                   <option>1</option>
                                   <option>2</option>
                                   <option>3</option>
                                   <option>4</option>
                                   <option>5</option>
                                   <option>6</option>
                               </select>
                 </div>
                  <div class="form-group-select">
                            <label class="label-color-select">공격 형태</label>
                               <select name="attack" id="attack" class="form-control" style="width: 85%;">
                                   <option value="0">선택하세요</option>
                                   <option>SQL Injection</option>
                                   <option>Directory Listing</option>
                                   <option>LFI & RFI</option>
                                   <option>Scanning</option>
                                   <option>Web CGI</option>
                                   <option>Pattern Block</option>
                               </select>
                   </div>
                 <div class="form-group-search">
                        <button id="submit_button" type="submit" class="btn-search" onclick="search_click();">Search</button>
                 </div> 	   
                 </form> 
			</div>
			
			
		<%
			DAO dao = new DAO();
			int checkedLog = dao.getRowNum("select * from userLog");
			int detectedLog = dao.getRowNum("select * from warningLog");
			int dangerousLog = dao.getRowNum("select * from warningLog where AttackLevel='4' or AttackLevel='5' or AttackLevel='6'");
        %> 
           
		<%
		String server_ip = request.getParameter("server_ip");
		String client_ip = request.getParameter("client_ip");
		String port = request.getParameter("port");
		String level = request.getParameter("level");
		String attack = request.getParameter("attack");
		
		String original = "select * from warningLog where";
		String db_select = "select * from warningLog where";
		
		SqlInjection sqlInjection = new SqlInjection();
		sqlInjection.initialize();
		
		if(server_ip != null && server_ip != ""){
			server_ip = sqlInjection.makeSecureString(server_ip);
			if(original.equals(db_select)){
				db_select = db_select + " ServerIP='" + server_ip + "'"; 
			}
			else{
				db_select = db_select + " and ServerIP='" + server_ip + "'";
			}
			%>
			<script>
			document.getElementById("server_ip").value='<%=server_ip%>';
			</script>
			<%
		}
		if(client_ip != null && client_ip != ""){
			client_ip = sqlInjection.makeSecureString(client_ip);
			if(original.equals(db_select)){
				db_select = db_select + " ClientIP='" + client_ip + "'";
			}
			else{
				db_select = db_select + " and ClientIP='" + client_ip + "'";
			}
			%>
			<script>
			document.getElementById("client_ip").value='<%=client_ip%>';
			</script>
			<%
		}
		if(port != null && port != ""){
			port = sqlInjection.makeSecureString(port);
			if(original.equals(db_select)){
				db_select = db_select + " Port='" + port + "'";
			}
			else{
				db_select = db_select + " and Port='" + port + "'"; 
			}
			%>
			<script>
			document.getElementById("port").value='<%=port%>';
			</script>
			<%
		}
		if(level != null && level != "" && !level.equals("0")){
			if(original.equals(db_select)){
				db_select = db_select + " AttackLevel='" + level + "'";
			}
			else{
				db_select = db_select + " and AttackLevel='" + level + "'"; 
			}
			%>
			<script>
			document.getElementById("level").value='<%=level%>';
			</script>
			<%
		}
		if(attack != null && attack != "" && !attack.equals("0")){
			if(original.equals(db_select)){
				db_select = db_select + " Problem='" + attack + "'";
			}
			else{
				db_select = db_select + " and Problem='" + attack + "'"; 
			}
			%>
			<script>
			document.getElementById("attack").value='<%=attack%>';
			</script>
			<%
		}
		
		if(original.equals(db_select)){
			db_select = "select * from warningLog";
		}
		
	%>


	<%
	int rowCount = dao.getRowNum(db_select);
	if(rowCount == 0){
		rowCount = dao.getRowNum("select * from warningLog");
	%>
	<script>
	alert('해당 정보가 없습니다.');
	window.location.href = "<c:url value = '/summary'/>";
	</script>
	<%
	}
	File dbFile = new File(MyConstants.DATABASE_CITY_PATH);
    DatabaseReader reader = new DatabaseReader.Builder(dbFile).build();
    
	String[] arr1 = new String[rowCount];	//모든 국가 저장
	String[] arr2 = new String[rowCount];	//모든 날짜 저장
	String[] arr3 = new String[rowCount];	//모든 IP 저장
	String[] arr4 = new String[rowCount];	//모든 공격형태 저장
	String[] arr5 = new String[rowCount];	//모든 시간 저장
	
	int num1 = 0, num2 = 0, num3 = 0, num4 = 0, num5 = 0, num6 = 0;
	
	ArrayList<DTO> list = dao.getAllData(db_select);
	
	for(int i=0; i<list.size(); i++){
		arr2[num2++] = list.get(i).getDate();
		arr3[num3] = list.get(i).getClientIP();
		try{
	       	InetAddress ipAddress = InetAddress.getByName(arr3[num3]);
	        CityResponse r = reader.city(ipAddress);

	        Country country = r.getCountry();
	        arr1[num1++] = country.getIsoCode().toLowerCase();
	    }catch(AddressNotFoundException e){
	       	arr1[num1++] = "kr";
	    }
		num3++;
		arr4[num4++] = list.get(i).getProblem();
		arr5[num5++] = list.get(i).getTime();
	}
	
	for(int i=0; i<arr5.length; i++){
		String[] test = arr5[i].split(":");
		arr5[i] = test[0];
	}
	
	
	
	int countryCount = 1, dateCount = 1, clientIPCount = 1, attackCount = 1, timeCount = 1;	//다른 날짜, IP 개수, 공격형태
	
	for(int i=0; i<arr1.length; i++){
		int c = 0;
		for(int j=0; j<i; j++){
			if(arr1[i].equals(arr1[j])){
				break;
			}
			else c++;
			
			if(j==i-1){
				if(c==j+1){
					countryCount++;
				}
			}
		}
	}
	
	for(int i=0; i<arr2.length; i++){
		int c = 0;
		for(int j=0; j<i; j++){
			if(arr2[i].equals(arr2[j])){
				break;
			}
			else c++;
			
			if(j == i - 1){
				if(c == j + 1){
					dateCount++;
				}
			}
		}
	}
	
	for(int i=0; i<arr3.length; i++){
		int c = 0;
		for(int j=0; j<i; j++){
			if(arr3[i].equals(arr3[j])){
				break;
			}
			else c++;
			
			if(j==i-1){
				if(c==j+1){
					clientIPCount++;
				}
			}
		}
	}
	
	for(int i=0; i<arr4.length; i++){
		int c = 0;
		for(int j=0; j<i; j++){
			if(arr4[i].equals(arr4[j])){
				break;
			}
			else c++;
			
			if(j==i-1){
				if(c==j+1){
					attackCount++;
				}
			}
		}
	}
	
	for(int i=0; i<arr5.length; i++){
		int c = 0;
		for(int j=0; j<i; j++){
			if(arr5[i].equals(arr5[j])){
				break;
			}
			else c++;
			
			if(j==i-1){
				if(c==j+1){
					timeCount++;
				}
			}
		}
	}
	
	
	String[] countryArr = new String[countryCount];	//겹치지 않는 나라
	int[] countryValue = new int[countryCount];		// 나라에 해당되는 공격 횟수
	double[] countryRate = new double[countryCount];
	for(int i=0; i<countryValue.length; i++){
		countryValue[i] = 0;
	}
	for(int i=0; i<countryRate.length; i++){
		countryRate[i] = 0;
	}
	
	String[] dateArr = new String[dateCount];	//겹치지 않는 날짜
	int[] dateValue = new int[dateCount];		//날짜에 해당되는 공격 횟수
	for(int i=0; i<dateValue.length; i++){
		dateValue[i] = 0;
	}
	
	String[] clientIPArr = new String[clientIPCount];	//겹치지 않는 IP
	int[] clientIPValue = new int[clientIPCount];		//공격당한 아이피 횟수
	double[] clientIPRate = new double[clientIPCount];		//IP 비율
	for(int i=0; i<clientIPValue.length; i++){
		clientIPValue[i] = 0;
	}
	for(int i=0; i<clientIPRate.length; i++){
		clientIPRate[i] = 0;
	}
	
	String[] attackArr = new String[attackCount];	//겹치지 않는 공격형태
	int[] attackValue = new int[attackCount];
	double[] attackRate = new double[attackCount];
	for(int i=0; i<attackValue.length; i++){
		attackValue[i] = 0;
	}
	for(int i=0; i<attackRate.length; i++){
		attackRate[i] = 0;
	}
	
	String[] timeArr = new String[timeCount];
	int[] timeValue = new int[timeCount];
	for(int i=0; i<timeValue.length; i++){
		timeValue[i] = 0;
	}
	
	countryArr[0] = arr1[0];
	int k = 1;
	for(int i=0; i<arr1.length; i++){
		int c = 0;
		for(int j=0; j<i; j++){
			if(arr1[i].equals(arr1[j])){
				break;
			}
			else c++;
			
			if(j == i - 1){
				if(c == j + 1){
					countryArr[k++] = arr1[i];
				}
			}
		}
	}
	
	for(int i=0; i<arr1.length; i++){
		for(int j=0; j<countryArr.length; j++){
			if(countryArr[j].equals(arr1[i])){
				countryValue[j]++;
			}
		}
	}
	for(int i=0; i<countryRate.length; i++){
		countryRate[i] = countryValue[i] * 100.0 /  arr1.length;
		countryRate[i] = Math.round(countryRate[i] * 100d) / 100d;
	}
	
	dateArr[0] = arr2[0];
	k = 1;
	for(int i=0; i<arr2.length; i++){
		int c = 0;
		for(int j=0; j<i; j++){
			if(arr2[i].equals(arr2[j])){
				break;
			}
			else c++;
			
			if(j == i - 1){
				if(c == j + 1){
					dateArr[k++] = arr2[i];
				}
			}
		}
	}
	
	for(int i=0; i<arr2.length; i++){
		for(int j=0; j<dateArr.length; j++){
			if(dateArr[j].equals(arr2[i])){
				dateValue[j]++;
			}
		}
	}

	
	clientIPArr[0] = arr3[0];
	k = 1;
	for(int i=0; i<arr3.length; i++){
		int c = 0;
		for(int j=0; j<i; j++){
			if(arr3[i].equals(arr3[j])){
				break;
			}
			else c++;
			
			if(j == i - 1){
				if(c == j + 1){
					clientIPArr[k++] = arr3[i];
				}
			}
		}
	}
	
	for(int i=0; i<arr3.length; i++){
		for(int j=0; j<clientIPArr.length; j++){
			if(clientIPArr[j].equals(arr3[i])){
				clientIPValue[j]++;
			}
		}
	}
	
	for(int i=0; i<clientIPRate.length; i++){
		clientIPRate[i] = clientIPValue[i] * 100.0 /  arr3.length;
		clientIPRate[i] = Math.round(clientIPRate[i] * 100d) / 100d;
	}
	
	attackArr[0] = arr4[0];
	k = 1;
	for(int i=0; i<arr4.length; i++){
		int c = 0;
		for(int j=0; j<i; j++){
			if(arr4[i].equals(arr4[j])){
				break;
			}
			else c++;
			
			if(j == i - 1){
				if(c == j + 1){
					attackArr[k++] = arr4[i];
				}
			}
		}
	}
	
	for(int i=0; i<arr4.length; i++){
		for(int j=0; j<attackArr.length; j++){
			if(attackArr[j].equals(arr4[i])){
				attackValue[j]++;
			}
		}
	}
	
	for(int i=0; i<attackRate.length; i++){
		attackRate[i] = attackValue[i] * 100.0 /  arr4.length;
		attackRate[i] = Math.round(attackRate[i] * 100d) / 100d;
	}
	
	timeArr[0] = arr5[0];
	k = 1;
	for(int i=0; i<arr5.length; i++){
		int c = 0;
		for(int j=0; j<i; j++){
			if(arr5[i].equals(arr5[j])){
				break;
			}
			else c++;
			
			if(j == i - 1){
				if(c == j + 1){
					timeArr[k++] = arr5[i];
				}
			}
		}
	}
	
	for(int i=0; i<arr5.length; i++){
		for(int j=0; j<timeArr.length; j++){
			if(timeArr[j].equals(arr5[i])){
				timeValue[j]++;
			}
		}
	}
	%>
	
			<div id="main">
				<span class="search-icon" onclick="openNav()">&#9776;</span>
			  	<a href="<c:url value = '/summary'/>" style="padding-right:15px;font-size: 13px;text-transform: uppercase;font-weight: 700;color: #777; float: right;">검색 조건 초기화</a>
				<%
				String id = "";
				
				id = (String)session.getAttribute("id");
				if(id.equals("test")){
					%>
					<a href="<c:url value = '/home'/>" class="up-menu">로그인</a>
					<%
				}
				else{
					%>
					<a href="<c:url value = '/logout'/>" class="up-menu">로그아웃</a>
					<a href="<c:url value = '/alert'/>" class="up-menu">알림 데이터 불러오기</a>
					<%
				}
				%>
			</div>
            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        
                        <li>
                            <a href="<c:url value = '/dashboard'/>"><i class="fa fa-hashtag" aria-hidden="true"></i> 대시보드</a>
                        </li>
                        <li>
                            <a><i class="fa fa-bar-chart" aria-hidden="true"></i> 차트 보고서<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a id="s" href="<c:url value = '/summary'/>"><i class="fa fa-book" aria-hidden="true"></i> 종합 및 요약</a>
                                	<script>
                                    	var para = document.location.href.split("?");
                                    	if(para[1] != null){
                                    		document.getElementById('s').setAttribute('href', 'summary?' + para[1]);
                                    	}
                                    </script>
                                </li>
                                <li>
                                	<a id="c" href="<c:url value = '/country'/>"><i class="fa fa-globe" aria-hidden="true"></i> 국가 및 IP</a>
                                	<script>
                                    	var para = document.location.href.split("?");
                                    	if(para[1] != null){
                                    		document.getElementById('c').setAttribute('href', 'country?' + para[1]);
                                    	}
                                    </script>
                                </li>
                                <li>
                                	<a id="t" href="<c:url value = '/time'/>"><i class="fa fa-clock-o" aria-hidden="true"></i> 시간 및 공격 유형</a>
                                	<script>
                                    	var para = document.location.href.split("?");
                                    	if(para[1] != null){
                                    		document.getElementById('t').setAttribute('href', 'time?' + para[1]);
                                    	}
                                    </script>
                                </li>
                               <!--  <li>
                                    <a id="a" href="attack"><i class="fa fa-bug" aria-hidden="true"></i> 공격 유형</a>
                                	<script>
                                    	var para = document.location.href.split("?");
                                    	if(para[1] != null){
                                    		document.getElementById('a').setAttribute('href', 'attack?' + para[1]);
                                    	}
                                    </script>
                                </li> -->
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>
                        <li>
                        	<a id="p4" href="<c:url value = '/table'/>"><i class="fa fa-table" aria-hidden="true"></i> 테이블 보고서</a>
                        	<script>
                                   	var para = document.location.href.split("?");
                                   	if(para[1] != null){
                                    	document.getElementById('p4').setAttribute('href', 'table?' + para[1]);
                                    }
                            </script>
                        </li>
                        <li>
                        	<a href="<c:url value = '/print'/>"><i class="fa fa-print" aria-hidden="true"></i> 보고서 출력</a>
                        </li>
                        <li>
                        	<a href="<c:url value = '/others'/>"><i class="fa fa-magic" aria-hidden="true"></i> 고객센터</a>
                        </li>                
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>
		</div>
        <div id="page-wrapper" class="prolog-page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h3><i class="fa fa-book" aria-hidden="true"></i> 종합 및 요약</h3>
                    <h5>종합적인 결과를 요약해 놓은 페이지 입니다.</h5>
                </div>
             </div>
               
            
                <!-- /.col-lg-12 -->
         <div class="row">
         
	         <div class="col-lg-12">
	             <div class="panel" style="background-color : #808080;">
	                        <div class="panel-heading">
	                            <div class="row">
	                                <div class="col-xs-12 text-right">
	                                   <div class="detect_log">탐지된 로그 / 검사한 로그 : <span style="color: #ffad33;"><%=detectedLog %>/<%=checkedLog %></span></div>
	                                </div>
	                       </div>
	                  </div>
	         </div>
	      </div>
      </div>
      
   <div class="chart-box chart-box0 show">
	    <div class="row">
	    	<div class="col-lg-6" style="margin-top: 10px;">
	    		<div class="panel panel-default panel-chart">
	    			<!-- 종합 거미줄 차트 -->
	    			<div id="spider"></div>
	    		</div>
	    	</div>
	    	<div class="col-lg-6" style="margin-top: 10px;">
	    		<div class="panel panel-default panel-chart" style="overflow:hidden;">
	    			<!-- 예상피해 BAR 차트  -->
	    			<div id="bar"></div>
	    		</div>
	    	</div>
	    </div>
	    	      <div class="row">
	    	<div class="col-lg-12" style="margin-top: 10px;">
	    		<div class="panel panel-default">
					<!-- 예상피해 설명 테이블  -->
					<div class="panel-heading">
                <h4 class="panel-title" style="font-weight:bold;">
                	<i class="fa fa-hand-o-right fa-1x" aria-hidden="true"></i> 공격 별 예상 피해 <i class="fa fa-hand-o-left fa-1x" aria-hidden="true"></i>
                </h4>
            </div>
                <div class="panel-body">         
					<table class= "table table-bordered">
    <colgroup span="2"></colgroup>
    <col>
    <col>
    <thead>
        <tr>
            <th>예상 피해 정도</th>
            <th scope ="col">공격 유형</th>
            <th scope="col">예상 피해</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <th rowspan="3" scope="rowgroup" style=" width: 118px;">상</th>
            <th>SQL Injection</th>
            <td>개인(DB) 정보 노출,2차 추가 공격 발생 가능성</td>
        </tr>
        <tr>
            <th>Directory Listing</th>
            <td>개인 정보 탈취, 웹 서버 공격</td>
        </tr>
        <tr>
        	 <th>LFI & RFI</th>
            <td>공격 컴퓨터에 악성 코드 발생</td>
        </tr>
    </tbody>
    <tbody>
        <tr>
            <th rowspan="2" scope="rowgroup">중</th>
            <th>Scanning</th>
            <td>개인 정보 수집, 추가 공격 가능성</td>
        </tr>
        <tr>
            <th>Web CGI</th>
            <td>웹 쿠키 정보 노출, 거짓 페이지 생성 후 개인 정보 탈취</td>
        </tr>
    </tbody>
    <tbody>
        <tr>
            <th scope="rowgroup">하</th>
            <th>Pattern Block</th>
            <td>시스템 과부화 및 서비스 성능 저하</td>
        </tr>
    </tbody>
</table>
                </div>
	    		</div>
	    	</div>
	    </div>
	     <div class="row">
	    	<div class="col-lg-12" style="margin-top: 10px;">
	    		
	    		<div class="panel panel-default panel-manual">
            <div class="panel-heading" role="tab" id="headingZero">
                <h4 class="panel-title" style=" font-size: 16px; font-weight: bold;">
                    <a role="button" data-toggle="collapse" data-parent="#accordion" href="<c:url value = '#collapseZero'/>" aria-expanded="true" aria-controls="collapseZero" style=" display: block;text-decoration: none;">
                        <i class="more-less glyphicon glyphicon-plus" style="float: right;color: #212121;"></i>
                         	<i class="fa fa-hand-o-right fa-1x" aria-hidden="true"></i> 차트 사용 설명서 <i class="fa fa-hand-o-left fa-1x" aria-hidden="true"></i>
                    </a>
                </h4>
            </div>
            <div id="collapseZero" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingZero">
                <div class="panel-body" style="border-top-color: #EEEEEE;font-size:14px;">
                        <div class="row"> 
                       <img src= "<c:url value='/resources/img/DANGER.png'/>"style="float: left; width: 600px; height: 300px;">
                      <div style="padding-left: 630px; font-size: 17px;padding-top: 80px;">
                        #1. 차트의 오른쪽 상단의 버튼을 이용하여 이미지로 저장이 가능합니다.<br>
                       	#2. 빨간색 그래프는 위험도를 나타내며,파란색 그래프는 빈도 수를 나타냅니다.<br> 
                       	#3. 위험도 및 빈도수 차트는 해당 점에 마우스를 올릴 시 상단에 공격 빈도 횟수 및 위험도가 표시됩니다.<br>     	
                       </div>    
                       </div>
                       <div class="row"> 
                       <img src= "<c:url value='/resources/img/OMG.png'/>" style="float: left; width: 600px; height: 300px;">
                      <div style="padding-left: 630px; font-size: 17px;padding-top: 80px;">
                        #1. 차트의 오른쪽 상단의 버튼을 이용하여 이미지로 저장이 가능합니다.<br>
                       	#2. 예상 피해 차트는 해당 차트에 마우스를 올릴 시 상단에 예상 피해도 별 비율이 표시됩니다<br>
                       	#3. 예상 피해도 비율입니다.<br>           	
                       </div>    
                       </div>                      	          
                </div>
            </div>
	    		</div>
	    	</div>
	    </div>
    </div>
    
    
    
    
    
	</div>
    <!-- /#wrapper -->

	<script>
	function openNav() {
	    document.getElementById("mySidenav").style.width = "250px";
	    document.getElementById("main").style.marginRight = "250px";
	    document.getElementById("page-wrapper").style.marginRight = "250px";
	}
	
	function closeNav() {
	    document.getElementById("mySidenav").style.width = "0";
	    document.getElementById("main").style.marginRight= "0";
	    document.getElementById("page-wrapper").style.marginRight= "0";
	}
	</script>
	
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.15/js/dataTables.bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="resources/vendor/metisMenu/metisMenu.min.js"></script>

    <!-- HighCharts Charts JavaScript -->
    <script src="https://code.highcharts.com/highcharts.js"></script>
    <script src="https://code.highcharts.com/modules/data.js"></script>
   <script src="https://code.highcharts.com/modules/drilldown.js"></script>
	<script src="https://code.highcharts.com/highcharts-more.js"></script>
	<script src="https://code.highcharts.com/modules/exporting.js"></script>
	
	<!-- MapChart -->
	<script src="http://code.highcharts.com/maps/modules/map.js"></script>
	<script src="http://code.highcharts.com/maps/modules/exporting.js"></script>
	<script src="https://code.highcharts.com/mapdata/custom/world-palestine-highres.js"></script>
   
   <!-- creative -->
   <script src="resources/js/creative.js"></script>
    
    <!-- Custom Theme JavaScript -->
    <script src="resources/dist/js/sb-admin-2.js"></script>

<%
	int[] numLevel = new int[6];
	String[] P = {"Pattern Block", "Web CGI", "Scanning", "LFI & RFI", "Directory Listing", "SQL Injection"};

	if(db_select.equals("select * from warningLog")){
		for(int i=0; i<numLevel.length; i++){
			int j = i + 1;
			numLevel[i] = dao.getRowNum("select * from warningLog where AttackLevel='" + j + "'");
		}
	}
	else{
		for(int i=0; i<numLevel.length; i++){
			int j = i + 1;
			numLevel[i] = dao.getRowNum(db_select + " and AttackLevel='" + j + "'");
		}
	}
	
	int highNum = 0;
	for(int i=0; i<numLevel.length; i++){
		if(numLevel[i] >= highNum) highNum = numLevel[i];
	}
	int highNum_length = (int)(Math.log10(highNum)+1);
	
	int mul = 1;
	
	for(int i=0; i<highNum_length-1; i++){
		mul *= 5;
	}
	
%>
<script type="text/javascript">

var mul = <%=mul%>;
Highcharts.chart('spider', {

    chart: {
        polar: true,
        type: 'line'
    },
    title: {
        text: '위험도 & 빈도수',
        x:0
    },

    pane: {
        size: '70%'
    },

    xAxis: {
        categories: ['SQL Injection', 'Directory Listing', 'LFI & RFI', 'Scanning', 
                     'Web CGI', 'Pattern Block'],
        tickmarkPlacement: 'on',
        lineWidth: 0
    },

    yAxis: {
        gridLineInterpolation: 'polygon',
        lineWidth: 0,
        min: 0
    },

    credits: {
        enabled: false
    },
    legend: {
        align: 'right',
        verticalAlign: 'top',
        y: 70,
        layout: 'vertical'
    },

    series: [{
        name: '위험도',
        data: [6 * mul, 5 * mul, 4 * mul, 3 * mul, 2 * mul, 1 * mul],
        pointPlacement: 'on',
        color: '#f70404'
    }, {
        name: '빈도수',
        data: [<%=numLevel[5]%>, <%=numLevel[4]%>, <%=numLevel[3]%>, <%=numLevel[2]%>, <%=numLevel[1]%>, <%=numLevel[0]%>],
        pointPlacement: 'on'
    }]

});

<%
int top = 0, middle = 0, bottom = 0;
if(db_select.equals("select * from warningLog")){
	bottom = dao.getRowNum("select * from warningLog where AttackLevel='1'");
	middle = dao.getRowNum("select * from warningLog where AttackLevel='2' or AttackLevel='3'");
	top = dao.getRowNum("select * from warningLog where AttackLevel='4' or AttackLevel='5' or AttackLevel='6'");
}
else{
	bottom = dao.getRowNum(db_select + "and AttackLevel='1'");
	middle = dao.getRowNum(db_select + "and (AttackLevel='2' or AttackLevel='3')");
	top = dao.getRowNum(db_select + "and (AttackLevel='4' or AttackLevel='5' or AttackLevel='6')");
}
int account = top + middle + bottom;
double topP = 0, middleP = 0, bottomP = 0;
topP = top * 100.0 / account;
topP = Math.round(topP * 100d) / 100d;
middleP = middle * 100.0 / account;
middleP = Math.round(middleP * 100d) / 100d;
bottomP = bottom * 100.0 / account;
bottomP = Math.round(bottomP * 100d) / 100d;
%>

Highcharts.setOptions({
    colors:[ '#FF0000','#ff7733','#ffcc66']
});


Highcharts.chart('bar', {
    chart: {
        type: 'column'
    },
    title: {
        text: '예상 피해'
    },
   
    xAxis: {
        type: 'category'
    },
    yAxis: {
        title: {
            text: ''
        }
	
    },
    credits: {
        enabled: false
    },
    legend: {
        enabled: false
    },
    plotOptions: {
        series: {
            borderWidth: 0,
            dataLabels: {
                enabled: true,
                format: '{point.y:.1f}%'
            }
        }
    },

    tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
    },

    series: [{
        name: '예상 피해',
        colorByPoint: true,
        data: [{
            name: '상',
            y: <%=topP%>
        }, {
            name: '중',
            y: <%=middleP%>
        }, {
            name: '하',
            y: <%=bottomP%>
        }]
    }]
});

</script>
<script type="text/javascript">

$(document).ready(function () {

    /* DataTables */
    var myTable = $("#table").dataTable({
        "sDom": '<"row view-filter"<"col-sm-12"<"pull-left"l><"pull-right"f><"clearfix">>>t<"row view-pager"<"col-sm-12"<"text-center"ip>>>'
    });

});

	</script>
</body>

</html>