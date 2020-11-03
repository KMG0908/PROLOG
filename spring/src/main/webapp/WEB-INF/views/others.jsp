<!DOCTYPE html>
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
                <a class="navbar-brand" href="<c:url value='/nhome'/>" style="font-weight: bold;"><i class="fa fa-pinterest-p" aria-hidden="true"></i>ro|Log</a>
            </div>
            <!-- /.navbar-header -->
            <!-- /.navbar-top-links -->
		
	
			<div id="main">
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
                            <a href="<c:url value='/dashboard'/>" ><i class="fa fa-hashtag" aria-hidden="true"></i> 대시보드</a>
                        </li>
                        <li>
                            <a><i class="fa fa-bar-chart" aria-hidden="true"></i> 차트 보고서<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="<c:url value='/summary'/> "><i class="fa fa-book" aria-hidden="true"></i> 종합 및 요약</a>
                                </li>
                                <li>
                                	<a href="<c:url value='/country'/> "><i class="fa fa-globe" aria-hidden="true"></i> 국가 및 IP</a>
                                </li>
                                <li>
                                	<a href="<c:url value='/time'/> "><i class="fa fa-clock-o" aria-hidden="true"></i> 시간 및 공격 유형</a>
                                </li>
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>
                        <li>
                        	<a href="<c:url value='/table'/> "><i class="fa fa-table" aria-hidden="true"></i> 테이블 보고서</a>
                        </li>
                        <li>
                        	<a href="<c:url value='/print'/> "><i class="fa fa-print" aria-hidden="true"></i> 보고서 출력</a>
                        </li>
                        <li>
                        	<a href="<c:url value='/others'/> "><i class="fa fa-magic" aria-hidden="true"></i> 고객센터</a>
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
                    <h3><i class="fa fa-magic fa-1x" aria-hidden="true"></i> 고객센터</h3>
                    <h5>사용자에게 전문가를 연결시켜주는 페이지입니다.</h5>
                </div>
             </div>
               
            
                <!-- /.col-lg-12 -->
         <div class="row">
         
	         <div class="col-lg-12">
	             <div class="panel" style="background-color : #808080;padding-bottom: 17px;">
	                        <div class="panel-heading">
	                            <div class="row">
	                                <div class="col-xs-12 text-right">
	                                  
	                                </div>
	                       </div>
	                  </div>
	         </div>
	      </div>
      </div>
    <div class="row">
    	<div class="col-lg-12">
    	<div class="panel-heading">
    		<img src="<c:url value='/resources/img/kisa.png'/>" style="width: 250px; padding-bottom: 20px;">
    		<!-- <h3 style="font-weight: bold;">KISA</h3> -->
    	</div>
    	</div>
    </div>
    
 	<div class="panel panel-default others-panel-form">
	     <div class="row">
	     
                <div class="col-lg-3 col-md-6 text-center">
                    <a style="color : black;" href="https://www.kisa.or.kr/business/violation/violation1_sub1.jsp" target="_blank">
    
                        <i class="fa fa-internet-explorer fa-4x text-primary"></i>
                        <h3 style="font-weight:bold;">Web Site</h3></a>
                        <p class="text-muted">KISA 홈페이지 입니다.</p>
                    </div>
  
                 <div class="col-lg-3 col-md-6 text-center">
                    <a style="color:black;" href="https://www.facebook.com/bohonara/photos/a.704209882964401.1073741827.704159932969396/900575366661184/" target="_blank">
     
                        <i class="fa fa-4x fa-facebook text-primary"></i>
                        <h3 style="font-weight:bold;">Facebook</h3></a>
                        <p class="text-muted">KISA 페이스북 입니다.</p>
                    </div>
               
                <div class="col-lg-3 col-md-6 text-center">
                 
                        <i class="fa fa-phone fa-4x text-primary" aria-hidden="true"></i>
                        <h3 style="font-weight:bold;">Call</h3>
                        <p class="text-muted">국내 어디서나 국번없이 118</p>
                   
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                 
                        <i class="fa fa-4x fa-envelope text-primary"></i> 
                        <h3 style="font-weight:bold;">웹 문의 사항</h3>
                        <p class="text-muted">Kisa@naver.com</p>
                   
                </div>
        </div>
        </div>
        
         <div class="row">
    	<div class="col-lg-12">
    	<div class="panel-heading">
    		<img src="<c:url value='/resources/img/cybersecurity.jpg'/>" style="width: 250px; height: 110px;padding-left: 2px;">
    		<!-- <h3 style="font-weight: bold;">사이버 경찰청</h3> -->
    	</div>
    	</div>
    </div>
    
 	<div class="panel panel-default others-panel-form" >
	     <div class="row">
	     
                <div class="col-lg-3 col-md-6 text-center">
                    <a style="color : black;" href="http://cyberbureau.police.go.kr/index.do" target="_blank">
    
                        <i class="fa fa-internet-explorer fa-4x text-primary"></i>
                        <h3 style="font-weight:bold;">Web Site</h3></a>
                        <p class="text-muted">사이버 경찰청 홈페이지 입니다.</p>
                    </div>
  
                 <div class="col-lg-3 col-md-6 text-center">
                    <a style="color:black;" href="https://www.facebook.com/cyberbureau/" target="_blank">
     
                        <i class="fa fa-4x fa-facebook text-primary"></i>
                        <h3 style="font-weight:bold;">Facebook</h3></a>
                        <p class="text-muted">사이버 경찰청 페이스북입니다.</p>
                    </div>
               
                <div class="col-lg-3 col-md-6 text-center">
                 
                        <i class="fa fa-phone fa-4x text-primary" aria-hidden="true"></i>
                        <h3 style="font-weight:bold;">Call</h3>
                        <p class="text-muted">국내 어디서나 국번없이 182</p>
                   
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                 
                        <i class="fa fa-4x fa-envelope text-primary"></i> 
                        <h3 style="font-weight:bold;">E-Mail</h3>
                        <p class="text-muted">Cyber Police@naver.com</p>
                   
                </div>
        </div>
        </div>
        
         <div class="row">
    	<div class="col-lg-12">
    	<div class="panel-heading">
    		<!-- <h3 style="font-weight: bold;">국정원</h3> -->
    		<img src="<c:url value='/resources/img/kukjeongwon.jpg'/>" style="width: 250px; height: 80px;">
    	</div>
    	</div>
    </div>
    
 	<div class="panel panel-default others-panel-form" >
	     <div class="row">
	     
                <div class="col-lg-3 col-md-6 text-center">
                    <a style="color : black;" href="http://www.nis.go.kr/main.do" target="_blank">
    
                        <i class="fa fa-internet-explorer fa-4x text-primary"></i>
                        <h3 style="font-weight:bold;">Web Site</h3></a>
                        <p class="text-muted">국정원 홈페이지 입니다.</p>
                    </div>
  
                 <div class="col-lg-3 col-md-6 text-center">
                    <a style="color:black;" href="https://www.facebook.com/pages/%EA%B5%AD%EC%A0%95%EC%9B%90/421473977925410?ref=br_rs" target="_blank">
     
                        <i class="fa fa-4x fa-facebook text-primary"></i>
                        <h3 style="font-weight:bold;">Facebook</h3></a>
                        <p class="text-muted">국정원 페이스북 입니다.</p>
                    </div>
               
                <div class="col-lg-3 col-md-6 text-center">
                 
                        <i class="fa fa-phone fa-4x text-primary" aria-hidden="true"></i>
                        <h3 style="font-weight:bold;">Call</h3>
                        <p class="text-muted">국내 어디서나 국번없이 111</p>
                   
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                 
                        <i class="fa fa-4x fa-envelope text-primary"></i> 
                        <h3 style="font-weight:bold;">E-Mail</h3>
                        <p class="text-muted">Kukjeongwon@naver.com</p>
                   
                </div>
        </div>
        </div>
         <div class="row">
    	<div class="col-lg-12">
    	<div class="panel-heading">
    		<img src="<c:url value='/resources/img/kukbangbu.png'/>" style="width: 255px; height: 120px;">
    		<!-- <h3 style="font-weight: bold;">국방부</h3> -->
    	</div>
    	</div>
    </div>
    
 	<div class="panel panel-default others-panel-form" >
	     <div class="row">
	     
                <div class="col-lg-3 col-md-6 text-center">
                    <a style="color : black;" href="https://www.mnd.go.kr/"target="_blank">
    
                        <i class="fa fa-internet-explorer fa-4x text-primary"></i>
                        <h3 style="font-weight:bold;">Web Site</h3></a>
                        <p class="text-muted">국방부 홈페이지입니다.</p>
                    </div>
  
                 <div class="col-lg-3 col-md-6 text-center">
                    <a style="color:black;" href="https://www.facebook.com/MNDKOR/" target="_blank">
     
                        <i class="fa fa-4x fa-facebook text-primary"></i>
                        <h3 style="font-weight:bold;">Facebook</h3></a>
                        <p class="text-muted">국방부 페이스북입니다.</p>
                    </div>
               
                <div class="col-lg-3 col-md-6 text-center">
                 
                        <i class="fa fa-phone fa-4x text-primary" aria-hidden="true"></i>
                        <h3 style="font-weight:bold;">Call</h3>
                        <p class="text-muted">1577-9090</p>
                   
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                 
                        <i class="fa fa-4x fa-envelope text-primary"></i> 
                        <h3 style="font-weight:bold;">E-Mail</h3>
                        <p class="text-muted">Kukbangbu@naver.com</p>
                   
                </div>
        </div>
        </div>
        
        
        <div class="row">
        	<div class="col-lg-12">
        		<div class="row-text" style="font-size:16px;padding-top: 100px;"> PRO|LOG</div>
        		<div class="row-text" style="font-size :10px;padding-bottom: 15px;"> Suwon.Univ </div>
        	</div>
        </div>
        </div>
	    	</div>
  
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




</body>

</html>