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
<%@ page import="java.net.InetAddress" %>
<%@ page import="com.maxmind.geoip2.exception.AddressNotFoundException" %>
<%@ page import="com.maxmind.geoip2.model.CityResponse" %>
<%@ page import="com.maxmind.geoip2.DatabaseReader" %>
<%@ page import="com.maxmind.geoip2.record.City" %>
<%@ page import="com.maxmind.geoip2.record.Country" %>
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
			     <form id="search_form" role="form">
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
                         <button id="submit_button" type="submit" class="btn-search">Search</button>
                 </div> 	
                 </form>    
			</div>
	
			<div id="main">
				<span class="search-icon" onclick="openNav()">&#9776;</span>
			  	<a href="<c:url value='/table'/>" class="up-menu">검색 조건 초기화</a>
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
                                <!-- <li>
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
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h3><i class="fa fa-table" aria-hidden="true"></i> 테이블 보고서</h3>
                    <h5>차트 보고서를 종합적인 결과와 함께 테이블 형태로 볼 수 있는 페이지 입니다.</h5>
                </div>
             </div>
                 <%
                 File dbFile = new File(MyConstants.DATABASE_CITY_PATH);
                 DatabaseReader reader = new DatabaseReader.Builder(dbFile).build();
                 DAO dao = new DAO();
              	int checkedLog = dao.getRowNum("select * from userLog");
              	int detectedLog = dao.getRowNum("select * from warningLog");
              	int dangerousLog = dao.getRowNum("select * from warningLog where AttackLevel='4' or AttackLevel='5' or AttackLevel='6'");
            
           %> 
            
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
		
		//System.out.println(db_select);
	%>
	
	<%
	int rowCount = dao.getRowNum(db_select);
	if(rowCount == 0){
		db_select = "select * from warningLog";
	%>
	<script>
	alert('해당 정보가 없습니다.');
	window.location.href = "<c:url value='/table'/>";
	</script>
	<%
	}
	%>
	
	<%
int dangerous1 = 0, dangerous2 = 0;
	
if(db_select.equals("select * from warningLog")){
	dangerous1 = dao.getRowNum("select * from warningLog where AttackLevel='1' or AttackLevel='2' or AttackLevel='3'");
	dangerous2 = dao.getRowNum("select * from warningLog where AttackLevel='4' or AttackLevel='5' or AttackLevel='6'");
}
else{
	dangerous1 = dao.getRowNum(db_select + "and (AttackLevel='1' or AttackLevel='2' or AttackLevel='3')");
	dangerous2 = dao.getRowNum(db_select + "and (AttackLevel='4' or AttackLevel='5' or AttackLevel='6')");
}

int logNum = dangerous1 + dangerous2;

double per_dangerous1 = dangerous1 * 100.0 / logNum;
per_dangerous1 = Math.round(per_dangerous1 * 100d) / 100d;

double per_dangerous2 = dangerous2 * 100.0 / logNum;
per_dangerous2 = Math.round(per_dangerous2 * 100d) / 100d;

double per_safe = 0.00;

if(logNum == 0){
	per_safe = 100.00;
}

%>
      <!-- 아래 본문 -->
      <div class="row">
     	 <div class="panel panel-default panel-chart">
     	 <div class="row">
	      <div class="col-lg-3" style="margin-top: 10px; ">
			<div id="table-container"></div>
		    		
		   </div>
		   <div class="col-lg-9" id="log-table-form">
		   		
		   			<table id="table" class="table table-striped table-hover table-responsive" >
       				<caption>로그분석표</caption>
			
	        <thead style="font-size: 13px;" >
	            <tr class = "active info">
	                <th>No.</th>
	                <th>공격형태</th>
	                <th><!-- +아이콘 -->위험도</th>
	                <th>공격자 IP</th>
	                <th><!-- +아이콘 -->국가</th>
	                <th>날짜</th>
	                <th>시간</th>
	            </tr>
	        </thead>
	        <tbody style="font-size: 13px;">
			<%
			ArrayList<DTO> list = new ArrayList<DTO>();
			list = dao.getAllData(db_select);
			int num = 1;
			if(list.size() != 0){
				for(int i=0; i<list.size(); i++){
					out.println("<tr>");
            		out.println("<td>" + num + "</td>");
            		if(list.get(i).getAttackLevel().equals("1") || list.get(i).getAttackLevel().equals("2") || list.get(i).getAttackLevel().equals("3")){
            			out.println("<td><img src='resources/img/shield2.jpg' style='width: 15px; height: 15px; display:inline;'/>" + list.get(i).getProblem() + "</td>");
            		}
            		else{
            			out.println("<td><img src='resources/img/shield1.jpg' style='width: 15px; height: 15px; display:inline;'/>" + list.get(i).getProblem() + "</td>");
            		}
            		out.println("<td>" + list.get(i).getAttackLevel() + "</td>");
            		out.println("<td>" + list.get(i).getClientIP() + "</td>");
            		
            		String cityName = "X";
       				try{
       	            	InetAddress ipAddress = InetAddress.getByName(list.get(i).getClientIP());
       	            	CityResponse re = reader.city(ipAddress);
       	                
       	             	City city = re.getCity();
       	             	cityName = city.getName();
       	             	if(cityName == null) cityName = "X";
       	            }catch(AddressNotFoundException e){
       	            	cityName = "X";
       	            }
       				
       				if(cityName.equals("X")){
       					out.println("<td><img src='resources/img/"+list.get(i).getCountry()+".png'style='width: 25px; height: 15px; display:inline;'/>" + list.get(i).getCountry() + "</td>");
       				}
       				else{
       					out.println("<td><img src='resources/img/"+list.get(i).getCountry()+".png'style='width: 25px; height: 15px; display:inline;'/>" + list.get(i).getCountry() + ", " + cityName + "</td>");
       				}
       				out.println("<td>" + list.get(i).getDate() + "</td>");
            		out.println("<td>" + list.get(i).getTime() + "</td>");
            		out.println("</tr>");
            		num++;
				}
            }
            else{
            	db_select = "select * from warningLog";
			%>
			<script>
			alert('해당 정보가 없습니다.');
			window.location.href = "<c:url value='/table'/>";
			</script>
			<%
				list = dao.getAllData(db_select);
				for(int i=0; i<list.size(); i++){
					out.println("<tr>");
            		out.println("<td>" + num + "</td>");
            		if(list.get(i).getAttackLevel().equals("1") || list.get(i).getAttackLevel().equals("2") || list.get(i).getAttackLevel().equals("3")){
            			out.println("<td><img src='resources/img/shield2.jpg' style='width: 15px; height: 15px; display:inline;'/>" + list.get(i).getProblem() + "</td>");
            		}
            		else{
            			out.println("<td><img src='resources/img/shield1.jpg' style='width: 15px; height: 15px; display:inline;'/>" + list.get(i).getProblem() + "</td>");
            		}
            		out.println("<td>" + list.get(i).getAttackLevel() + "</td>");
            		out.println("<td>" + list.get(i).getClientIP() + "</td>");
            		String cityName = "X";
       				try{
       	            	InetAddress ipAddress = InetAddress.getByName(list.get(i).getClientIP());
       	            	CityResponse re = reader.city(ipAddress);
       	                
       	             	City city = re.getCity();
       	             	cityName = city.getName();
       	             	if(cityName == null) cityName = "X";
       	            }catch(AddressNotFoundException e){
       	            	cityName = "X";
       	            }
       				
       				if(cityName.equals("X")){
       					out.println("<td><img src='resources/img/"+list.get(i).getCountry()+".png'style='width: 25px; height: 15px; display:inline;'/>" + list.get(i).getCountry() + "</td>");
       				}
       				else{
       					out.println("<td><img src='resources/img/"+list.get(i).getCountry()+".png'style='width: 25px; height: 15px; display:inline;'/>" + list.get(i).getCountry() + ", " + cityName + "</td>");
       				}
            		out.println("<td>" + list.get(i).getDate() + "</td>");
            		out.println("<td>" + list.get(i).getTime() + "</td>");
            		out.println("</tr>");
            		num++;
				}
            }
			rowCount = dao.getRowNum(db_select);
			
		    
			String[] arr1 = new String[rowCount];	//모든 국가 저장
			String[] arr2 = new String[rowCount];	//모든 날짜 저장
			String[] arr3 = new String[rowCount];	//모든 IP 저장
			String[] arr4 = new String[rowCount];	//모든 공격형태 저장
			String[] arr5 = new String[rowCount];	//모든 시간 저장
			
			int num1 = 0, num2 = 0, num3 = 0, num4 = 0, num5 = 0, num6 = 0;
			
			list = dao.getAllData(db_select);
			
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
			
			for (int i = 0; i < dateValue.length; i++) {
		        for (int j = i + 1; j < dateValue.length; j++) {
		            if (dateValue[i] < dateValue[j]) {
		                int temp1 = dateValue[i];
		                dateValue[i] = dateValue[j];
		                dateValue[j] = temp1;
		                
		                String temp2 = dateArr[i];
		                dateArr[i] = dateArr[j];
		                dateArr[j] = temp2;
		            }
		        }
		    }
			%>
	        </tbody>
	    </table>
		   	</div>	
		   </div>
	   </div>
	   </div>
	   
	   <div class="row">
	    	<div class="col-lg-12" style="margin-top: 10px;">
	    		<div class="panel panel-default">
					<!-- 예상피해 설명 테이블  -->
					<div class="panel-heading">
                <h4 class="panel-title" style="font-weight:bold; font-size: 18px; text-align: center;">
                	<i class="fa fa-hand-o-right fa-1x" aria-hidden="true"></i> 결과 <i class="fa fa-hand-o-left fa-1x" aria-hidden="true"></i>
                </h4>
            </div>
                <div class="panel-body" style="font-size : 16px; text-align: center;">
				<%
            if(dangerous2 != 0){
            %>
             - 위험 <!--위험 1 때는 1로 -->2단계 공격 : <%=dangerous2 %>건<br>
               (위험한 로그 /탐지 로그 /전체 로그 : <%=dangerous2 %>/<%=detectedLog %>/<%=checkedLog %>)<br><br>
            
                  - 공격을 가장 많은 받은 날짜 : <%=dateArr[0] %><br><br>
                <%
               String IP = "";
               int index=1;
               int rNum = 4;
               if(clientIPArr.length < 4) rNum = clientIPArr.length;
               for(int i=0; i<rNum; i++){
                  if(i == rNum - 1) IP += index + ". " + clientIPArr[i];
                  else {
                     IP += index + ". "+clientIPArr[i] + "<br>";
                  }
                  index++;
               }
               %>
               -위험 IP (상위 4개) : <br><%=IP %><br><br>
               <%
               Locale loc = new Locale("", countryArr[0]);
               %>
               -공격을 가장 많이 한 국가 : <%=loc.getDisplayCountry() %><br><br>
               <font class="table-result-page-font" >
               <위험2><br>
               		현재 전체 로그 중  총 <%=logNum %>개의 위험이 발견되었습니다.<br>
               		(탐지 로그/전체 로그 : <%=detectedLog %>/<%=checkedLog %>)<br>
                 	전문가의 조치가 즉시 필요합니다.<br>
               <a href="<c:url value='/others'/>">고객 센터</a>를 참고하여 전문가의 도움을 받으시는 것을 권고합니다.<br>

            	<% 
           	 	}
            	else if(dangerous1 != 0){
            	%>
            	- 위험 <!--위험 1 때는 1로 -->1단계 공격 : <%=dangerous1 %>건<br>
               (위험한 로그 /탐지 로그 /전체 로그 : <%=dangerous1 %>/<%=detectedLog %>/<%=checkedLog %>)<br><br>
            
                  - 공격을 가장 많은 받은 날짜 : <%=dateArr[0] %><br><br>
                <%
               String IP = "";
               int index=1;
               int rNum = 4;
               if(clientIPArr.length < 4) rNum = clientIPArr.length;
               for(int i=0; i<rNum; i++){
                  if(i == rNum - 1) IP += index + ". " + clientIPArr[i];
                  else {
                     IP += index + ". "+clientIPArr[i] + "<br>";
                  }
                  index++;
               }
               %>
               -위험 IP (상위 4개) : <br><%=IP %><br><br>
               <%
               Locale loc = new Locale("", countryArr[0]);
               %>
               -공격을 가장 많이 한 국가 : <%=loc.getDisplayCountry() %><br><br>
            	<font class="table-result-page-font">
            	<위험1><br>
            	현재 전체 로그 중 <%=logNum %>개의 위험이 발견되었습니다.<br>
            	(탐지 로그/전체 로그 : <%=detectedLog %>/<%=checkedLog %>)<br>
           	 	피해가 발생할 수 있으니 전문가와의 상담이 필요합니다.
           	 	보고서를 참고하여 도움을 받으시길 바랍니다.
            	<a href="<c:url value='/others'/>">고객 센터</a>를 참고하여 전문가의 도움을 받으시는 것을 권고합니다.<br>
            	<%
            	}
            	else{
            	%>
            	<안전><br>
            		현재 전체 로그 중 위험이 발견되지 않았습니다. 
            	<%
            	}
            	%>
				</font>                   
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
	<script src="https://code.highcharts.com/highcharts-more.js"></script>
	<script src="https://code.highcharts.com/modules/exporting.js"></script>
    
    <!-- Custom Theme JavaScript -->
    <script src="resources/dist/js/sb-admin-2.js"></script>
	
	<script type="text/javascript">
	
	Highcharts.getOptions().plotOptions.pie.colors = (function () {
	    var colors = ['#ff704d', '#ffcc66', '#4db8ff'];

	  
	    return colors;
	}());
	
	
	Highcharts.chart('table-container', {
	    chart: {
	        plotBackgroundColor: null,
	        plotBorderWidth: 0,
	        plotShadow: false
	    },
	    title: {
	        text: '',
	        align: 'center',
	        verticalAlign: 'middle',
	        y: 40
	    },
	    tooltip: {
	        pointFormat: '<b>{point.percentage:.1f}%</b>'
	    },
	    plotOptions: {
	        pie: {
	            dataLabels: {
	                enabled: false,
	                distance: 10,
	                style: {
	                    fontWeight: 'bold',
	                    color: 'white'
	                }
	            },
	            showInLegend: true,
	            size : '80%',
	            startAngle: -90,
	            endAngle: 90,
	            center: ['50%', '75%']
	        }
	    },
	    credits: {
	        enabled: false
	    },
	    series: [{
	        type: 'pie',
	        name: 'Browser share',
	        innerSize: '50%',
	        data: [
	            ['위험2', <%=per_dangerous2%>],
	            ['위험1', <%=per_dangerous1%>],
	            ['안전', <%=per_safe%>]
	        ]
	    }]
	});


</script>
	<script type="text/javascript">
		$(document).ready(function() {
	    	$('#table').DataTable();
		} );
	</script>
</body>

</html>