<!DOCTYPE html>
<%@page import="com.spring.prolog.back.SqlInjection"%>
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
	window.location.href = "<c:url value = '/time'/>";
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
		//arr2[i] = arr2[i].substring(5);
		//arr2[i] = arr2[i].replace("-", "/");
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
	
	//System.out.println(dateCount + " " + clientIPCount);
	
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
	
	for (int i = 0; i < countryRate.length; i++) {
        for (int j = i + 1; j < countryRate.length; j++) {
            if (countryRate[i] < countryRate[j]) {
                double temp1 = countryRate[i];
                countryRate[i] = countryRate[j];
                countryRate[j] = temp1;
                
                String temp2 = countryArr[i];
                countryArr[i] = countryArr[j];
                countryArr[j] = temp2;
            }
        }
    }
	
	for (int i = 0; i < clientIPRate.length; i++) {
        for (int j = i + 1; j < clientIPRate.length; j++) {
            if (clientIPRate[i] < clientIPRate[j]) {
                double temp1 = clientIPRate[i];
                clientIPRate[i] = clientIPRate[j];
                clientIPRate[j] = temp1;
                
                String temp2 = clientIPArr[i];
                clientIPArr[i] = clientIPArr[j];
                clientIPArr[j] = temp2;
            }
        }
    }
	%>
	
			<div id="main">
				<span class="search-icon" onclick="openNav()">&#9776;</span>
			  	<a href="<c:url value = '/time'/>" class="up-menu">검색 조건 초기화</a>
				<%
				String id = "";
				
				id = (String)session.getAttribute("id");
				if(id.equals("test")){
					%>
					<a href="<c:url value='/home'/>" class="up-menu">로그인</a>
					<%
				}
				else{
					%>
					<a href="<c:url value='/logout'/>" class="up-menu">로그아웃</a>
					<a href="<c:url value='/alert'/>" class="up-menu">알림 데이터 불러오기</a>
					<%
				}
				%>
			</div>
            <div class="navbar-default sidebar" role="navigation" style="margin-top: 19px;">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        
                        <li>
                            <a href="<c:url value='/dashboard'/>"><i class="fa fa-hashtag" aria-hidden="true"></i> 대시보드</a>
                        </li>
                        <li>
                            <a><i class="fa fa-bar-chart" aria-hidden="true"></i> 차트 보고서<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a id="s" href="<c:url value='/summary'/>"><i class="fa fa-book" aria-hidden="true"></i> 종합 및 요약</a>
                                	<script>
                                    	var para = document.location.href.split("?");
                                    	if(para[1] != null){
                                    		document.getElementById('s').setAttribute('href', 'summary?' + para[1]);
                                    	}
                                    </script>
                                </li>
                                <li>
                                	<a id="c" href="<c:url value='/country'/>"><i class="fa fa-globe" aria-hidden="true"></i> 국가 및 IP</a>
                                	<script>
                                    	var para = document.location.href.split("?");
                                    	if(para[1] != null){
                                    		document.getElementById('c').setAttribute('href', 'country?' + para[1]);
                                    	}
                                    </script>
                                </li>
                                <li>
                                	<a id="t" href="<c:url value='/time'/>"><i class="fa fa-clock-o" aria-hidden="true"></i> 시간 및 공격 유형</a>
                                	<script>
                                    	var para = document.location.href.split("?");
                                    	if(para[1] != null){
                                    		document.getElementById('t').setAttribute('href', 'time?' + para[1]);
                                    	}
                                    </script>
                                </li>
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>
                        <li>
                        	<a id="p4" href="<c:url value='/table'/>"><i class="fa fa-table" aria-hidden="true"></i> 테이블 보고서</a>
                        	<script>
                                   	var para = document.location.href.split("?");
                                   	if(para[1] != null){
                                    	document.getElementById('p4').setAttribute('href', 'table?' + para[1]);
                                    }
                            </script>
                        </li>
                        <li>
                        	<a href="<c:url value='/print'/>"><i class="fa fa-print" aria-hidden="true"></i> 보고서 출력</a>
                        </li>
                        <li>
                        	<a href="<c:url value='/others'/>"><i class="fa fa-magic" aria-hidden="true"></i> 고객센터</a>
                        </li>                
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>
		</div>
        <div id="page-wrapper" calss="prolog-page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h3><i class="fa fa-clock-o" aria-hidden="true"></i>  시간 및 공격 유형</h3>
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
    <div id="time" class="show">
    	<div class="row">
	    	<div class="col-lg-6" style="margin-top: 10px;">
	    		<div class="panel panel-default panel-chart" style="overflow:hidden;">
	    			<!-- 시간 -->
	    			<!-- 여기다 차트를 넣어주세요.  -->
	    			<div id="timechart"></div>
	    		</div>
	    		
	    	</div>
	    	<div class="col-lg-6" style="margin-top: 10px;">
	    		<div class="panel panel-default panel-chart">
	    			<!-- 공격유형 -->
	    			<!-- 여기다 차트를 넣어주세요.  -->
	    			<div id="attackchart"></div>
	    		 </div>
	    	</div>
	    </div> 
	    <div class="row">
	    	<div style="float:right; margin-bottom: 10px; ">
	    			<button type="button" class="btn btn-info" id="more" onclick="openmore();">공격 유형별 보기 ▷</button>
	    </div>
	    </div>
	    <div class="row">
	    	<div class="col-lg-12" style="margin-top: 10px;">
	    		
	    		<div class="panel panel-default panel-manual">
            <div class="panel-heading" role="tab" id="headingZero">
                <h4 class="panel-title" style=" font-size: 16px; font-weight: bold;">
                    <a role="button" data-toggle="collapse" data-parent="#accordion" href="<c:url value='#collapseZero'/>" aria-expanded="true" aria-controls="collapseZero" style=" display: block;text-decoration: none;">
                        <i class="more-less glyphicon glyphicon-plus" style="float: right;color: #212121;"></i>
                         	<i class="fa fa-hand-o-right fa-1x" aria-hidden="true"></i> 차트 사용 설명서 <i class="fa fa-hand-o-left fa-1x" aria-hidden="true"></i>
                    </a>
                </h4>
            </div>
            <div id="collapseZero" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingZero">
                <div class="panel-body" style="border-top-color: #EEEEEE;font-size:14px;">
                        <div class="row">
                        <img src= "<c:url value='/resources/img/TIME.png'/>'" style="float: left; width: 600px; height: 300px;">
                      <div style="padding-left: 630px; font-size: 17px;padding-top: 80px;">
                        #1. 차트의 오른쪽 상단의 버튼을 이용하여 이미지로 저장이 가능합니다.<br>
                        #2. 시간 차트는 '◁' 버튼을 클릭시에 전체 날짜 차트로 전환됩니다.<br>
                        #3. 날짜 및 시간 차트는 해당 점에 마우스를 올릴 시 상단에 공격 빈도 횟수가 표시됩니다.<br>
                        #4. 가로축은 시간대를 나타냅니다.<br>
                        #5. 시간 차트의 점의 위치가  높을수록 공격 빈도가 높은 날짜입니다.<br>                     	
                       </div>
                       </div>
                       <div class="row"> 
                       <img src= "<c:url value='/resources/img/DATE.png'/>'" style="float: left; width: 600px; height: 300px;">
                      <div style="padding-left: 630px; font-size: 17px;padding-top: 80px;">
                        #1. 차트의 점을 클릭 시 해당 날짜에 맞는 시간 차트로 변경 됩니다.<br>
                        #2. 가로축은 날짜를 나타냅니다.                   	
                       </div>    
                       </div>   
                       <div class="row"> 
                       <img src= "<c:url value='/resources/img/ATTACK.png'/>'" style="float: left; width: 600px; height: 300px;">
                      <div style="padding-left: 630px; font-size: 17px;padding-top: 80px;">
                        #1. 차트의 오른쪽 상단의 버튼을 이용하여 이미지로 저장이 가능합니다.<br>
                       	#2. 공격 유형 차트는 해당 차트에 마우스를 올릴 시 상단에 공격 유형 별 비율이 표시됩니다.<br>
                       	#3. 공격 유형 차트의 하단 라벨을 클릭 시 해당 공격 유형을 차트에서 제외할 수 있습니다.<br>
                       	#4. 차트를 클릭할 시 해당 공격 유형에 관한 정보만 테이블 보고서로 검색됩니다.<br> 
                       	#5. 공격 유형 차트는 크기가 클수록 빈도가 높은 공격 유형입니다.<br>                	
                       </div>    
                       </div>                  	          
                </div>
            </div>
	    		</div>
	    	</div>
	    </div>
    </div>
    <div id="timemore" class="hidden">
	    <div class="row">
	    	<div class="col-lg-6" style="margin-top: 10px;">
	    		<div class="panel panel-default panel-chart" style="overflow:hidden;">
	    			<!-- 시간 -->
	    			<!-- 여기다 차트를 넣어주세요.  -->
	    			<div id="container2" class="more-chart-time"></div>
	    		</div>
	    	</div>
	    		    	<div class="col-lg-6" style="margin-top: 10px;">
	    		<div class="panel panel-default panel-chart" style="overflow:hidden;">
	    			<!-- 시간 -->
	    			<!-- 여기다 차트를 넣어주세요.  -->
	    			<div id="container3" class="more-chart-time"></div>
	    		</div>
	    	</div>
	    </div> 
	    	    <div class="row">
	    	<div class="col-lg-6" style="margin-top: 10px;">
	    		<div class="panel panel-default panel-chart" style="overflow:hidden;">
	    			<!-- 시간 -->
	    			<!-- 여기다 차트를 넣어주세요.  -->
	    			<div id="container4" class="more-chart-time"></div>
	    		</div>
	    	</div>
	    		    	<div class="col-lg-6" style="margin-top: 10px;">
	    		<div class="panel panel-default panel-chart" style="overflow:hidden;">
	    			<!-- 시간 -->
	    			<!-- 여기다 차트를 넣어주세요.  -->
	    			<div id="container5" class="more-chart-time"></div>
	    		</div>
	    	</div>
	    </div> 
	    	    <div class="row">
	    	<div class="col-lg-6" style="margin-top: 10px;">
	    		<div class="panel panel-default panel-chart" style="overflow:hidden;">
	    			<!-- 시간 -->
	    			<!-- 여기다 차트를 넣어주세요.  -->
	    			<div id="container6" class="more-chart-time"></div>
	    		</div>
	    	</div>
	    		    	<div class="col-lg-6" style="margin-top: 10px;">
	    		<div class="panel panel-default panel-chart" style="overflow:hidden;">
	    			<!-- 시간 -->
	    			<!-- 여기다 차트를 넣어주세요.  -->
	    			<div id="container7" class="more-chart-time"></div>
	    		</div>
	    	</div>
	    </div> 
	    <div class="row">
	    		    	<div style="float:right; margin-bottom: 10px; ">
	    			<button type="button" class="btn btn-info" id="more" onclick="closemore();">전체 유형 보기 ▷</button>
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
   
    
    <!-- Custom Theme JavaScript -->
    <script src="resources/dist/js/sb-admin-2.js"></script>

		<script type="text/javascript">
	function openmore() {
	    $('#time').removeClass("show");
	    $('#time').addClass("hidden");
	    $('#timemore').removeClass("hidden");
	    $('#timemore').addClass("show");
	}
	
	function closemore() {
	    $('#time').removeClass("hidden");
	    $('#time').addClass("show");
	    $('#timemore').removeClass("show");
	    $('#timemore').addClass("hidden");
	}
	</script>
<script>
	$('.panel-menu').click(function(){
		
		$('.panel-menu').removeClass('selected');
		$('.chart-box').removeClass('show');
		$('.panel-menu').find('.add-caret').removeClass('add-selected');
		
		$(this).addClass('selected');
		$(this).find('.add-caret').addClass('add-selected');
		
		var index = $(".chart-panel").index($(this).parent().parent());
		
		if (index == 0 ) {
			$('.chart-box0').addClass('show');
		}
		
		if (index == 1 ) {
			$('.chart-box1').addClass('show');
		}
		if (index == 2 ) {
			$('.chart-box2').addClass('show');
		}
		if (index == 3 ) {
			$('.chart-box3').addClass('show');
		}
		if (index == 4 ) {
			$('.chart-box4').addClass('show');
		}
		
	});

</script>
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

<%
String data_chart1 = "[";

for(int i=0; i<countryArr.length; i++){
	if(i==countryArr.length - 1){
		data_chart1 = data_chart1 + "['" + countryArr[i] + "', " + countryRate[i] + "] ]";
	}
	else{
		data_chart1 = data_chart1 + "['" + countryArr[i] + "', " + countryRate[i] + "],";
	}
	//System.out.println(countryArr[i]);
}
//System.out.println(data_chart1);

%>

var data = <%=data_chart1%>;
/* Highcharts.setOptions({
    colors:[]
});
 */
// Create the chart

var overviewSeries1 = [];//첫번째
var drillDownSeries1 = [];//두번째

<%
String dates = "";
for(int i=0; i<dateArr.length; i++){
	   if(i == dateArr.length - 1) dates += dateArr[i];
	   else dates += dateArr[i] + ",";
}
%>

var datesString="<%=dates%>";
var date = datesString.split(",");
var dates = new Array(date.length);

for(var i=0; i<date.length; i++){
	   var datesArr = date[i].split("-");
	   dates[i] = Math.floor(new Date(datesArr[0]*1, datesArr[1]*1-1, datesArr[2]*1+1).getTime()/86400000)*86400000; //86400000 = 1일
	   //dates[i] *= 1000;
	   //var d = new Date(dates[i]);
	   //alert(d);	   
}

<%
String averages = "";
for(int i=0; i<dateValue.length; i++){
	   if(i == dateValue.length - 1) averages += dateValue[i];
	   else averages += dateValue[i] + ",";
}
%>

var averagesString="<%=averages%>";
var averages = averagesString.split(",");
for(var i=0; i<averages.length; i++){
	   averages[i] *= 1;
}  


var temp = new Array(<%=dateCount%>);
var index = 0;
var index2 = 0;
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   temp[<%=i%>] = new Array(<%=dateValue[i]%>);
	   <%
}
%>
<%
list = dao.getAllData(db_select + " order by Date");
for(int i=0; i<list.size(); i++) {
	   %>
	   if (!temp[index][temp[index].length-1]) {
		   temp[index][index2]="<%=list.get(i).getTime()%>";
	   } else {
		   index++;
		   index2 = 0;
		   temp[index][index2]="<%=list.get(i).getTime()%>";
	   }
	   index2++;
	   <%
}
%>

var drillArray = new Array(<%=dateCount%>);
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   
	   drillArray[<%=i%>] = [[dates[<%=i%>],0],[dates[<%=i%>]+3600000,0],[dates[<%=i%>]+7200000,0],[dates[<%=i%>]+10800000,0],[dates[<%=i%>]+14400000,0],[dates[<%=i%>]+18000000,0],[dates[<%=i%>]+21600000,0],[dates[<%=i%>]+25200000,0],[dates[<%=i%>]+28800000,0],[dates[<%=i%>]+32400000,0],[dates[<%=i%>]+36000000,0],[dates[<%=i%>]+39600000,0],[dates[<%=i%>]+43200000,0],[dates[<%=i%>]+46800000,0],[dates[<%=i%>]+50400000,0],[dates[<%=i%>]+54000000,0],[dates[<%=i%>]+57600000,0],[dates[<%=i%>]+61200000,0],[dates[<%=i%>]+64800000,0],[dates[<%=i%>]+68400000,0],[dates[<%=i%>]+72000000,0],[dates[<%=i%>]+75600000,0],[dates[<%=i%>]+79200000,0],[dates[<%=i%>]+82800000,0]];
	   <%
}
%>


<%
for(int i=0;i<dateCount;i++) {
	   for(int j=0;j<dateValue[i];j++) {
		 %>
		var t = temp[<%=i%>][<%=j%>].split(":");
		if (t[0].indexOf("0") == 0){
			t[0] = t[0].substr(1,1);
		}
		drillArray[<%=i%>][t[0]][1]++;
	<%   }
}
%>
var list =  drillArray;

for (var i = 0; i < averages.length; i++) {
    var overviewSeries = {};
    overviewSeries.x = dates[i];
    overviewSeries.y = averages[i];
    overviewSeries.drilldown = 'list' + i;
    overviewSeries1.push(overviewSeries);
}

var drillDownSeries1 = [];

for (var j = 0; j < list.length; j++) {
    var drillDownSeries = {};
    drillDownSeries.id = 'list' + j;
    drillDownSeries.data = list[j];
    drillDownSeries1.push(drillDownSeries);
}



var index = 0;
var index2 = 0;
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   temp[<%=i%>] = new Array(<%=dateValue[i]%>);
	   <%
}
%>
<%
if(db_select.equals("select * from warningLog")){
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum("select * from warningLog where AttackLevel='6' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData("select * from warningLog where AttackLevel='6' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
else{
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum(db_select + " and AttackLevel='6' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData(db_select + " and AttackLevel='6' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
%>

var drillArray = new Array(<%=dateCount%>);
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   
	   drillArray[<%=i%>] = [[dates[<%=i%>],0],[dates[<%=i%>]+3600000,0],[dates[<%=i%>]+7200000,0],[dates[<%=i%>]+10800000,0],[dates[<%=i%>]+14400000,0],[dates[<%=i%>]+18000000,0],[dates[<%=i%>]+21600000,0],[dates[<%=i%>]+25200000,0],[dates[<%=i%>]+28800000,0],[dates[<%=i%>]+32400000,0],[dates[<%=i%>]+36000000,0],[dates[<%=i%>]+39600000,0],[dates[<%=i%>]+43200000,0],[dates[<%=i%>]+46800000,0],[dates[<%=i%>]+50400000,0],[dates[<%=i%>]+54000000,0],[dates[<%=i%>]+57600000,0],[dates[<%=i%>]+61200000,0],[dates[<%=i%>]+64800000,0],[dates[<%=i%>]+68400000,0],[dates[<%=i%>]+72000000,0],[dates[<%=i%>]+75600000,0],[dates[<%=i%>]+79200000,0],[dates[<%=i%>]+82800000,0]];
	   <%
}
%>


<%
for(int i=0;i<dateCount;i++) {
	   for(int j=0;j<dateValue[i];j++) {
		 %>	
		 if(temp[<%=i%>][<%=j%>] != null){
		var t = temp[<%=i%>][<%=j%>].split(":");
		if (t[0].indexOf("0") == 0){
			t[0] = t[0].substr(1,1);
		}
		drillArray[<%=i%>][t[0]][1]++;
		 }
	<%   }
}
%>
var list =  drillArray;

var overviewSeries2 = [];

var rNum = new Array(<%=dateCount%>);
<%
int rNum[] = new int[dateCount];
for(int i=0; i<dateValue.length; i++){
	if(db_select.equals("select * from warningLog")){
		rNum[i] = dao.getRowNum("select * from warningLog where AttackLevel='6' and Date='" + dateArr[i] + "'");
    }
    else{
		rNum[i] = dao.getRowNum(db_select + " and AttackLevel='6' and Date='" + dateArr[i] + "'");
    }
	%>
	rNum[<%=i%>] = <%=rNum[i]%>;
	<%
}
%>
for (var i = 0; i < averages.length; i++) {
    var overviewSeries = {};
    overviewSeries.x = dates[i];
    overviewSeries.y = rNum[i];
    overviewSeries.drilldown = 'list' + i;
    overviewSeries2.push(overviewSeries);
}

var drillDownSeries2 = [];

for (var j = 0; j < list.length; j++) {
    var drillDownSeries = {};
    drillDownSeries.id = 'list' + j;
    drillDownSeries.data = list[j];
    drillDownSeries2.push(drillDownSeries);
}



var index = 0;
var index2 = 0;
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   temp[<%=i%>] = new Array(<%=dateValue[i]%>);
	   <%
}
%>
<%
if(db_select.equals("select * from warningLog")){
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum("select * from warningLog where AttackLevel='5' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData("select * from warningLog where AttackLevel='5' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
else{
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum(db_select + " and AttackLevel='5' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData(db_select + " and AttackLevel='5' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
%>

var drillArray = new Array(<%=dateCount%>);
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   
	   drillArray[<%=i%>] = [[dates[<%=i%>],0],[dates[<%=i%>]+3600000,0],[dates[<%=i%>]+7200000,0],[dates[<%=i%>]+10800000,0],[dates[<%=i%>]+14400000,0],[dates[<%=i%>]+18000000,0],[dates[<%=i%>]+21600000,0],[dates[<%=i%>]+25200000,0],[dates[<%=i%>]+28800000,0],[dates[<%=i%>]+32400000,0],[dates[<%=i%>]+36000000,0],[dates[<%=i%>]+39600000,0],[dates[<%=i%>]+43200000,0],[dates[<%=i%>]+46800000,0],[dates[<%=i%>]+50400000,0],[dates[<%=i%>]+54000000,0],[dates[<%=i%>]+57600000,0],[dates[<%=i%>]+61200000,0],[dates[<%=i%>]+64800000,0],[dates[<%=i%>]+68400000,0],[dates[<%=i%>]+72000000,0],[dates[<%=i%>]+75600000,0],[dates[<%=i%>]+79200000,0],[dates[<%=i%>]+82800000,0]];
	   <%
}
%>


<%
for(int i=0;i<dateCount;i++) {
	   for(int j=0;j<dateValue[i];j++) {
		 %>	
		 if(temp[<%=i%>][<%=j%>] != null){
				var t = temp[<%=i%>][<%=j%>].split(":");
				if (t[0].indexOf("0") == 0){
					t[0] = t[0].substr(1,1);
				}
				drillArray[<%=i%>][t[0]][1]++;
				 }
			<%   }
}
%>
var list =  drillArray;

var overviewSeries3 = [];

var rNum = new Array(<%=dateCount%>);
<%
for(int i=0; i<dateValue.length; i++){
	if(db_select.equals("select * from warningLog")){
		rNum[i] = dao.getRowNum("select * from warningLog where AttackLevel='5' and Date='" + dateArr[i] + "'");
    }
    else{
		rNum[i] = dao.getRowNum(db_select + " and AttackLevel='5' and Date='" + dateArr[i] + "'");
    }
	%>
	rNum[<%=i%>] = <%=rNum[i]%>;
	<%
}
%>
for (var i = 0; i < averages.length; i++) {
    var overviewSeries = {};
    overviewSeries.x = dates[i];
    overviewSeries.y = rNum[i];
    overviewSeries.drilldown = 'list' + i;
    overviewSeries3.push(overviewSeries);
}

var drillDownSeries3 = [];

for (var j = 0; j < list.length; j++) {
    var drillDownSeries = {};
    drillDownSeries.id = 'list' + j;
    drillDownSeries.data = list[j];
    drillDownSeries3.push(drillDownSeries);
}


var index = 0;
var index2 = 0;
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   temp[<%=i%>] = new Array(<%=dateValue[i]%>);
	   <%
}
%>

var rNum = new Array(<%=dateCount%>);
<%
for(int i=0; i<dateValue.length; i++){
	if(db_select.equals("select * from warningLog")){
		rNum[i] = dao.getRowNum("select * from warningLog where AttackLevel='4' and Date='" + dateArr[i] + "'");
    }
    else{
		rNum[i] = dao.getRowNum(db_select + " and AttackLevel='4' and Date='" + dateArr[i] + "'");
    }
	%>
	rNum[<%=i%>] = <%=rNum[i]%>;
	<%
}
%>

<%
if(db_select.equals("select * from warningLog")){
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum("select * from warningLog where AttackLevel='4' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData("select * from warningLog where AttackLevel='4' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
else{
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum(db_select + " and AttackLevel='4' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData(db_select + " and AttackLevel='4' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
%>

var drillArray = new Array(<%=dateCount%>);
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   
	   drillArray[<%=i%>] = [[dates[<%=i%>],0],[dates[<%=i%>]+3600000,0],[dates[<%=i%>]+7200000,0],[dates[<%=i%>]+10800000,0],[dates[<%=i%>]+14400000,0],[dates[<%=i%>]+18000000,0],[dates[<%=i%>]+21600000,0],[dates[<%=i%>]+25200000,0],[dates[<%=i%>]+28800000,0],[dates[<%=i%>]+32400000,0],[dates[<%=i%>]+36000000,0],[dates[<%=i%>]+39600000,0],[dates[<%=i%>]+43200000,0],[dates[<%=i%>]+46800000,0],[dates[<%=i%>]+50400000,0],[dates[<%=i%>]+54000000,0],[dates[<%=i%>]+57600000,0],[dates[<%=i%>]+61200000,0],[dates[<%=i%>]+64800000,0],[dates[<%=i%>]+68400000,0],[dates[<%=i%>]+72000000,0],[dates[<%=i%>]+75600000,0],[dates[<%=i%>]+79200000,0],[dates[<%=i%>]+82800000,0]];
	   <%
}
%>


<%
for(int i=0;i<dateCount;i++) {
	   for(int j=0;j<dateValue[i];j++) {
		 %>	
		 if(temp[<%=i%>][<%=j%>] != null){
				var t = temp[<%=i%>][<%=j%>].split(":");
				if (t[0].indexOf("0") == 0){
					t[0] = t[0].substr(1,1);
				}
				drillArray[<%=i%>][t[0]][1]++;
				 }
			<%   }
}
%>
var list =  drillArray;

var overviewSeries4 = [];

var rNum = new Array(<%=dateCount%>);
<%
for(int i=0; i<dateValue.length; i++){
	if(db_select.equals("select * from warningLog")){
		rNum[i] = dao.getRowNum("select * from warningLog where AttackLevel='4' and Date='" + dateArr[i] + "'");
    }
    else{
		rNum[i] = dao.getRowNum(db_select + " and AttackLevel='4' and Date='" + dateArr[i] + "'");
    }
	%>
	rNum[<%=i%>] = <%=rNum[i]%>;
	<%
}
%>
for (var i = 0; i < averages.length; i++) {
    var overviewSeries = {};
    overviewSeries.x = dates[i];
    overviewSeries.y = rNum[i];
    overviewSeries.drilldown = 'list' + i;
    overviewSeries4.push(overviewSeries);
}

var drillDownSeries4 = [];

for (var j = 0; j < list.length; j++) {
    var drillDownSeries = {};
    drillDownSeries.id = 'list' + j;
    drillDownSeries.data = list[j];
    drillDownSeries4.push(drillDownSeries);
}


var index = 0;
var index2 = 0;
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   temp[<%=i%>] = new Array(<%=dateValue[i]%>);
	   <%
}
%>
<%
if(db_select.equals("select * from warningLog")){
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum("select * from warningLog where AttackLevel='3' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData("select * from warningLog where AttackLevel='3' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
else{
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum(db_select + " and AttackLevel='3' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData(db_select + " and AttackLevel='3' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
%>

var drillArray = new Array(<%=dateCount%>);
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   
	   drillArray[<%=i%>] = [[dates[<%=i%>],0],[dates[<%=i%>]+3600000,0],[dates[<%=i%>]+7200000,0],[dates[<%=i%>]+10800000,0],[dates[<%=i%>]+14400000,0],[dates[<%=i%>]+18000000,0],[dates[<%=i%>]+21600000,0],[dates[<%=i%>]+25200000,0],[dates[<%=i%>]+28800000,0],[dates[<%=i%>]+32400000,0],[dates[<%=i%>]+36000000,0],[dates[<%=i%>]+39600000,0],[dates[<%=i%>]+43200000,0],[dates[<%=i%>]+46800000,0],[dates[<%=i%>]+50400000,0],[dates[<%=i%>]+54000000,0],[dates[<%=i%>]+57600000,0],[dates[<%=i%>]+61200000,0],[dates[<%=i%>]+64800000,0],[dates[<%=i%>]+68400000,0],[dates[<%=i%>]+72000000,0],[dates[<%=i%>]+75600000,0],[dates[<%=i%>]+79200000,0],[dates[<%=i%>]+82800000,0]];
	   <%
}
%>


<%
for(int i=0;i<dateCount;i++) {
	   for(int j=0;j<dateValue[i];j++) {
		 %>	
		 if(temp[<%=i%>][<%=j%>] != null){
				var t = temp[<%=i%>][<%=j%>].split(":");
				if (t[0].indexOf("0") == 0){
					t[0] = t[0].substr(1,1);
				}
				drillArray[<%=i%>][t[0]][1]++;
				 }
			<%   }
}
%>
var list =  drillArray;

var overviewSeries5 = [];

var rNum = new Array(<%=dateCount%>);
<%
for(int i=0; i<dateValue.length; i++){
	if(db_select.equals("select * from warningLog")){
		rNum[i] = dao.getRowNum("select * from warningLog where AttackLevel='3' and Date='" + dateArr[i] + "'");
    }
    else{
		rNum[i] = dao.getRowNum(db_select + " and AttackLevel='3' and Date='" + dateArr[i] + "'");
    }
	%>
	rNum[<%=i%>] = <%=rNum[i]%>;
	<%
}
%>
for (var i = 0; i < averages.length; i++) {
    var overviewSeries = {};
    overviewSeries.x = dates[i];
    overviewSeries.y = rNum[i];
    overviewSeries.drilldown = 'list' + i;
    overviewSeries5.push(overviewSeries);
}

var drillDownSeries5 = [];

for (var j = 0; j < list.length; j++) {
    var drillDownSeries = {};
    drillDownSeries.id = 'list' + j;
    drillDownSeries.data = list[j];
    drillDownSeries5.push(drillDownSeries);
}


var index = 0;
var index2 = 0;
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   temp[<%=i%>] = new Array(<%=dateValue[i]%>);
	   <%
}
%>
<%
if(db_select.equals("select * from warningLog")){
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum("select * from warningLog where AttackLevel='2' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData("select * from warningLog where AttackLevel='2' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
else{
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum(db_select + " and AttackLevel='2' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData(db_select + " and AttackLevel='2' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
%>

var drillArray = new Array(<%=dateCount%>);
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   
	   drillArray[<%=i%>] = [[dates[<%=i%>],0],[dates[<%=i%>]+3600000,0],[dates[<%=i%>]+7200000,0],[dates[<%=i%>]+10800000,0],[dates[<%=i%>]+14400000,0],[dates[<%=i%>]+18000000,0],[dates[<%=i%>]+21600000,0],[dates[<%=i%>]+25200000,0],[dates[<%=i%>]+28800000,0],[dates[<%=i%>]+32400000,0],[dates[<%=i%>]+36000000,0],[dates[<%=i%>]+39600000,0],[dates[<%=i%>]+43200000,0],[dates[<%=i%>]+46800000,0],[dates[<%=i%>]+50400000,0],[dates[<%=i%>]+54000000,0],[dates[<%=i%>]+57600000,0],[dates[<%=i%>]+61200000,0],[dates[<%=i%>]+64800000,0],[dates[<%=i%>]+68400000,0],[dates[<%=i%>]+72000000,0],[dates[<%=i%>]+75600000,0],[dates[<%=i%>]+79200000,0],[dates[<%=i%>]+82800000,0]];
	   <%
}
%>


<%
for(int i=0;i<dateCount;i++) {
	   for(int j=0;j<dateValue[i];j++) {
		 %>	
		 if(temp[<%=i%>][<%=j%>] != null){
				var t = temp[<%=i%>][<%=j%>].split(":");
				if (t[0].indexOf("0") == 0){
					t[0] = t[0].substr(1,1);
				}
				drillArray[<%=i%>][t[0]][1]++;
				 }
			<%   }
}
%>
var list =  drillArray;

var overviewSeries6 = [];

var rNum = new Array(<%=dateCount%>);
<%
for(int i=0; i<dateValue.length; i++){
	if(db_select.equals("select * from warningLog")){
		rNum[i] = dao.getRowNum("select * from warningLog where AttackLevel='2' and Date='" + dateArr[i] + "'");
    }
    else{
		rNum[i] = dao.getRowNum(db_select + " and AttackLevel='2' and Date='" + dateArr[i] + "'");
    }
	%>
	rNum[<%=i%>] = <%=rNum[i]%>;
	<%
}
%>
for (var i = 0; i < averages.length; i++) {
    var overviewSeries = {};
    overviewSeries.x = dates[i];
    overviewSeries.y = rNum[i];
    overviewSeries.drilldown = 'list' + i;
    overviewSeries6.push(overviewSeries);
}

var drillDownSeries6 = [];

for (var j = 0; j < list.length; j++) {
    var drillDownSeries = {};
    drillDownSeries.id = 'list' + j;
    drillDownSeries.data = list[j];
    drillDownSeries6.push(drillDownSeries);
}


var index = 0;
var index2 = 0;
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   temp[<%=i%>] = new Array(<%=dateValue[i]%>);
	   <%
}
%>
<%
if(db_select.equals("select * from warningLog")){
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum("select * from warningLog where AttackLevel='1' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData("select * from warningLog where AttackLevel='1' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
else{
	for(int i=0; i<dateArr.length; i++){
		int rowC = dao.getRowNum(db_select + " and AttackLevel='1' and Date='" + dateArr[i] + "'");
		if(rowC != 0){
			list = dao.getAllData(db_select + " and AttackLevel='1' and Date='" + dateArr[i] + "'");
			for(int j=0; j<list.size(); j++) {
				   %>
				   if (!temp[index][temp[index].length-1]) {
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   } else {
					   index++;
					   index2 = 0;
					   temp[index][index2]="<%=list.get(j).getTime()%>";
				   }
				   index2++;
				   <%
			}
			%>
			index++;
			index2 = 0;
			<%
		}
		else{
			%>
			index++;
			index2 = 0;
			<%
		}
	}
}
%>

var drillArray = new Array(<%=dateCount%>);
<%
for(int i=0;i<dateValue.length;i++) {
	   %>
	   
	   drillArray[<%=i%>] = [[dates[<%=i%>],0],[dates[<%=i%>]+3600000,0],[dates[<%=i%>]+7200000,0],[dates[<%=i%>]+10800000,0],[dates[<%=i%>]+14400000,0],[dates[<%=i%>]+18000000,0],[dates[<%=i%>]+21600000,0],[dates[<%=i%>]+25200000,0],[dates[<%=i%>]+28800000,0],[dates[<%=i%>]+32400000,0],[dates[<%=i%>]+36000000,0],[dates[<%=i%>]+39600000,0],[dates[<%=i%>]+43200000,0],[dates[<%=i%>]+46800000,0],[dates[<%=i%>]+50400000,0],[dates[<%=i%>]+54000000,0],[dates[<%=i%>]+57600000,0],[dates[<%=i%>]+61200000,0],[dates[<%=i%>]+64800000,0],[dates[<%=i%>]+68400000,0],[dates[<%=i%>]+72000000,0],[dates[<%=i%>]+75600000,0],[dates[<%=i%>]+79200000,0],[dates[<%=i%>]+82800000,0]];
	   <%
}
%>


<%
for(int i=0;i<dateCount;i++) {
	   for(int j=0;j<dateValue[i];j++) {
		 %>	
		 if(temp[<%=i%>][<%=j%>] != null){
				var t = temp[<%=i%>][<%=j%>].split(":");
				if (t[0].indexOf("0") == 0){
					t[0] = t[0].substr(1,1);
				}
				drillArray[<%=i%>][t[0]][1]++;
				 }
			<%   }
}
%>
var list =  drillArray;

var overviewSeries7 = [];

var rNum = new Array(<%=dateCount%>);
<%
for(int i=0; i<dateValue.length; i++){
	if(db_select.equals("select * from warningLog")){
		rNum[i] = dao.getRowNum("select * from warningLog where AttackLevel='1' and Date='" + dateArr[i] + "'");
    }
    else{
		rNum[i] = dao.getRowNum(db_select + " and AttackLevel='1' and Date='" + dateArr[i] + "'");
    }
	%>
	rNum[<%=i%>] = <%=rNum[i]%>;
	<%
}
%>
for (var i = 0; i < averages.length; i++) {
    var overviewSeries = {};
    overviewSeries.x = dates[i];
    overviewSeries.y = rNum[i];
    overviewSeries.drilldown = 'list' + i;
    overviewSeries7.push(overviewSeries);
}

var drillDownSeries7 = [];

for (var j = 0; j < list.length; j++) {
    var drillDownSeries = {};
    drillDownSeries.id = 'list' + j;
    drillDownSeries.data = list[j];
    drillDownSeries7.push(drillDownSeries);
}

Highcharts.setOptions({
    lang: {
        drillUpText: '◁ '
    }
});


var options = {

    chart: {
        type: 'line',
        events: {
            load: function(event) {
            	var num = 0;
            	var value = 0;
            	var len = <%=dateArr.length%>;
            	for(var i=0; i<len; i++){
            		if(value < this.series[0].data[i].y){
            			value = this.series[0].data[i].y;
            			num = i;
            		}
            	}
                this.series[0].data[num].doDrilldown();
            }
        }
    },

    title: {
        text: '날짜 및 시간'
    },

    xAxis: {
        type: 'datetime',
        dateTimeLabelFormats:{
     	   day: '%b %e, %Y'
        }
    },
    yAxis: {
        title: {
            text: '공격 받은 횟수'
        }
    },

    drilldown: {
        series: drillDownSeries1 //새정보
    },

    legend: {
        enabled: false
    },
    credits: {
        enabled: false
    },

    plotOptions: {
        series: {
     	   name: '공격 받은 횟수',
            dataLabels: {
                enabled:false
            },
            shadow: false
        },
        line:{
        	size:'10%'
        }

    },

    series: [{
        name: '공격 받은 횟수',
        //colorByPoint: true,
        data: overviewSeries1
    }]
};


// Column chart
options.chart.renderTo = 'timechart';

var options2 = {

	    chart: {
	        type: 'line',
	        events: {
	            load: function(event) {
	            	var num = 0;
	            	var value = 0;
	            	var len = <%=dateArr.length%>;
	            	for(var i=0; i<len; i++){
	            		if(value < this.series[0].data[i].y){
	            			value = this.series[0].data[i].y;
	            			num = i;
	            		}
	            	}
	                this.series[0].data[num].doDrilldown();
	            }
	        }
	    },

	    title: {
	        text: 'SQL Injection'
	    },

	    xAxis: {
	        type: 'datetime',
	        dateTimeLabelFormats:{
	     	   day: '%b %e, %Y'
	        }
	    },
	    yAxis: {
	        title: {
	            text: '공격 받은 횟수'
	        }
	    },

	    drilldown: {
	        series: drillDownSeries2 //새정보
	    },

	    legend: {
	        enabled: false
	    },
	    credits: {
	        enabled: false
	    },

	    plotOptions: {
	        series: {
	     	   name: '공격 받은 횟수',
	            dataLabels: {
	                enabled: false
	            },
	            shadow: false
	        },
	        line:{
	        	size:'10%'
	        }

	    },

	    series: [{
	        name: '공격 받은 횟수',
	        //colorByPoint: true,
	        data: overviewSeries2
	    }]
	};


	// Column chart
	options2.chart.renderTo = 'container2';
	
	var options3 = {

		    chart: {
		        type: 'line',
		        events: {
		            load: function(event) {
		            	var num = 0;
		            	var value = 0;
		            	var len = <%=dateArr.length%>;
		            	for(var i=0; i<len; i++){
		            		if(value < this.series[0].data[i].y){
		            			value = this.series[0].data[i].y;
		            			num = i;
		            		}
		            	}
		                this.series[0].data[num].doDrilldown();
		            }
		        }
		    },

		    title: {
		        text: 'Directory Listing'
		    },

		    xAxis: {
		        type: 'datetime',
		        dateTimeLabelFormats:{
		     	   day: '%b %e, %Y'
		        }
		    },
		    yAxis: {
		        title: {
		            text: '공격 받은 횟수'
		        }
		    },

		    drilldown: {
		        series: drillDownSeries3 //새정보
		    },

		    legend: {
		        enabled: false
		    },
		    credits: {
		        enabled: false
		    },

		    plotOptions: {
		        series: {
		     	   name: '공격 받은 횟수',
		            dataLabels: {
		                enabled: false
		            },
		            shadow: false
		        },
		        line:{
		        	size:'10%'
		        }

		    },

		    series: [{
		        name: '공격 받은 횟수',
		        //colorByPoint: true,
		        data: overviewSeries3
		    }]
		};


		// Column chart
		options3.chart.renderTo = 'container3';
		
		var options4 = {

			    chart: {
			        type: 'line',
			        events: {
			            load: function(event) {
			            	var num = 0;
			            	var value = 0;
			            	var len = <%=dateArr.length%>;
			            	for(var i=0; i<len; i++){
			            		if(value < this.series[0].data[i].y){
			            			value = this.series[0].data[i].y;
			            			num = i;
			            		}
			            	}
			                this.series[0].data[num].doDrilldown();
			            }
			        }
			    },

			    title: {
			        text: 'LFI & RFI'
			    },

			    xAxis: {
			        type: 'datetime',
			        dateTimeLabelFormats:{
			     	   day: '%b %e, %Y'
			        }
			    },
			    yAxis: {
			        title: {
			            text: '공격 받은 횟수'
			        }
			    },

			    drilldown: {
			        series: drillDownSeries4 //새정보
			    },

			    legend: {
			        enabled: false
			    },
			    credits: {
			        enabled: false
			    },

			    plotOptions: {
			        series: {
			     	   name: '공격 받은 횟수',
			            dataLabels: {
			                enabled: false
			            },
			            shadow: false
			        },
			        line:{
			        	size:'10%'
			        }

			    },

			    series: [{
			        name: '공격 받은 횟수',
			        //colorByPoint: true,
			        data: overviewSeries4
			    }]
			};


			// Column chart
			options4.chart.renderTo = 'container4';
			
			var options5 = {

				    chart: {
				        type: 'line',
				        events: {
				            load: function(event) {
				            	var num = 0;
				            	var value = 0;
				            	var len = <%=dateArr.length%>;
				            	for(var i=0; i<len; i++){
				            		if(value < this.series[0].data[i].y){
				            			value = this.series[0].data[i].y;
				            			num = i;
				            		}
				            	}
				                this.series[0].data[num].doDrilldown();
				            }
				        }
				    },

				    title: {
				        text: 'Scanning'
				    },

				    xAxis: {
				        type: 'datetime',
				        dateTimeLabelFormats:{
				     	   day: '%b %e, %Y'
				        }
				    },
				    yAxis: {
				        title: {
				            text: '공격 받은 횟수'
				        }
				    },

				    drilldown: {
				        series: drillDownSeries5 //새정보
				    },

				    legend: {
				        enabled: false
				    },
				    credits: {
				        enabled: false
				    },

				    plotOptions: {
				        series: {
				     	   name: '공격 받은 횟수',
				            dataLabels: {
				                enabled: false
				            },
				            shadow: false
				        },
				        line:{
				        	size:'10%'
				        }

				    },

				    series: [{
				        name: '공격 받은 횟수',
				        //colorByPoint: true,
				        data: overviewSeries5
				    }]
				};


				// Column chart
				options5.chart.renderTo = 'container5';
				
				var options6 = {

					    chart: {
					        type: 'line',
					        events: {
					            load: function(event) {
					            	var num = 0;
					            	var value = 0;
					            	var len = <%=dateArr.length%>;
					            	for(var i=0; i<len; i++){
					            		if(value < this.series[0].data[i].y){
					            			value = this.series[0].data[i].y;
					            			num = i;
					            		}
					            	}
					                this.series[0].data[num].doDrilldown();
					            }
					        }
					    },

					    title: {
					        text: 'Web CGI'
					    },

					    xAxis: {
					        type: 'datetime',
					        dateTimeLabelFormats:{
					     	   day: '%b %e, %Y'
					        }
					    },
					    yAxis: {
					        title: {
					            text: '공격 받은 횟수'
					        }
					    },

					    drilldown: {
					        series: drillDownSeries6 //새정보
					    },

					    legend: {
					        enabled: false
					    },
					    credits: {
					        enabled: false
					    },

					    plotOptions: {
					        series: {
					     	   name: '공격 받은 횟수',
					            dataLabels: {
					                enabled: false
					            },
					            shadow: false
					        },
					        line:{
					        	size:'10%'
					        }

					    },

					    series: [{
					        name: '공격 받은 횟수',
					        //colorByPoint: true,
					        data: overviewSeries6
					    }]
					};


					// Column chart
					options6.chart.renderTo = 'container6';
					
					var options7 = {

						    chart: {
						        type: 'line',
						        events: {
						            load: function(event) {
						            	var num = 0;
						            	var value = 0;
						            	var len = <%=dateArr.length%>;
						            	for(var i=0; i<len; i++){
						            		if(value < this.series[0].data[i].y){
						            			value = this.series[0].data[i].y;
						            			num = i;
						            		}
						            	}
						                this.series[0].data[num].doDrilldown();
						            }
						        }
						    },

						    title: {
						        text: 'Pattern block'
						    },

						    xAxis: {
						        type: 'datetime',
						        dateTimeLabelFormats:{
						     	   day: '%b %e, %Y'
						        }
						    },
						    yAxis: {
						        title: {
						            text: '공격 받은 횟수'
						        }
						    },

						    drilldown: {
						        series: drillDownSeries7 //새정보
						    },

						    legend: {
						        enabled: false
						    },
						    credits: {
						        enabled: false
						    },

						    plotOptions: {
						        series: {
						     	   name: '공격 받은 횟수',
						            dataLabels: {
						                enabled: false
						            },
						            shadow: false
						        },
						        line:{
						        	size:'8%'
						        }

						    },

						    series: [{
						        name: '공격 받은 횟수',
						        data: overviewSeries7
						    }]
						};


						// Column chart
						options7.chart.renderTo = 'container7';

var chart1 = new Highcharts.Chart(options);
var chart2 = new Highcharts.Chart(options2);
var chart3 = new Highcharts.Chart(options3);
var attackchart = new Highcharts.Chart(options4);
var chart5 = new Highcharts.Chart(options5);
var chart6 = new Highcharts.Chart(options6);
var chart7 = new Highcharts.Chart(options7);


<%
String data_chart3 = "[";
String drilldown_chart3 = "{ series: [";

int c = 0;
if(clientIPArr.length < 5) c = clientIPArr.length;
else c = 5;
for(int i=0; i<c; i++){
	   if(i==c - 1){
		   data_chart3 = data_chart3 + "{ name: '" + clientIPArr[i] + "', y: " + clientIPRate[i] + "}]";
	   }
	   else{
		   data_chart3 = data_chart3 + "{ name: '" + clientIPArr[i] + "', y: " + clientIPRate[i] + "}, ";
	   }
}
%>

var data_chart3=<%=data_chart3%>;

function IP_Click() {
	
	var para = document.location.href.split("?");
	var loca = "";
	if(para[1] != null){
		loca = para[1];
	}
	
	var client = "client_ip=" + this.name;
	
	if(loca.indexOf('client_ip=&') == -1){
		if(loca.indexOf('client_ip=') == -1){
			if(loca == "") location.href = "table?" + client;
			else location.href = "table?" + loca + "&" + client;
		}
		else{
			location.href = "table?" + loca;
		}
	}
	else{
		var locaReplace = loca.replace("client_ip=", client);
		location.href = "table?" + locaReplace;
	}
}

<%
String data_attackchart = "[";
String drilldown_attackchart = "[";

for(int i=0; i<attackArr.length; i++){
	   if(i==attackArr.length-1){
		   data_attackchart = data_attackchart + "{ name: '" + attackArr[i] + "', y: " + attackRate[i] +"}]";
	   }
	   else{
		   data_attackchart = data_attackchart + "{ name: '" + attackArr[i] + "', y: " + attackRate[i] +"}, ";
	   }
}

%>
Highcharts.setOptions({
    lang: {
        drillUpText: '◁ '
    },
    colors:['#d874a9','#863a90','#9700e7','#4f309c','#474793','#004878','#1a7796','#1080c9','#7687ff','#b2cbed','#ffffff']
});
var data_attackchart=<%=data_attackchart%>;

Highcharts.chart('attackchart', {
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
    },
    title: {
        text: '공격 유형'
    },
    tooltip: {
        pointFormat: '<b>{point.percentage:.1f}%</b>'
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: false,
                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                style: {
                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                }
            },
            showInLegend: true ,
             point: { 
 			   events: { 
 				   click: Attack_Click
 			   }
            }  
        }
    },
    credits: {
        enabled: false
    },
    series: [{
        name: 'Rate',
        colorByPoint: true,
        data: data_attackchart
    }]
});

function Attack_Click() {
 	var para = document.location.href.split("?");
	var loca = "";
	if(para[1] != null){
		loca = para[1];
	}
	
	var nameReplace = this.name;
	nameReplace = nameReplace.replace(/(\s)/, "+");
	var attack = "attack=" + nameReplace;
	
	if(loca.indexOf('attack=0') == -1){
		if(loca.indexOf('attack=') == -1){
			if(loca == "") location.href = "table?" + attack;
			else location.href = "table?" + loca + "&" + attack;
		}
		else{
			location.href = "table?" + loca;
		}
	}
	else{
		var locaReplace = loca.replace("attack=0", attack);

		location.href = "table?" + locaReplace;
	}
} 

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