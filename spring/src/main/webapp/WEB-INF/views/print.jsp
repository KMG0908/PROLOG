<!DOCTYPE html>
<%@page import="com.spring.prolog.dto.DTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.spring.prolog.database.MyConstants"%>
<%@page import="com.spring.prolog.dao.DAO"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.FileNotFoundException"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.io.InputStreamReader"%>
<%@ page import="java.util.StringTokenizer"%>
<%@ page import="java.util.regex.Pattern"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.text.DecimalFormat"%>

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
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="<c:url value='/nhome'/>"  style="font-weight: bold;">
					<i class="fa fa-pinterest-p" aria-hidden="true"></i>ro|Log
				</a>
			</div>
			<!-- /.navbar-header -->
			<!-- /.navbar-top-links -->

			<div id="main">
				<%
				String id = "";
				
				id = (String)session.getAttribute("id");
				if(id.equals("test")){
					%>
					<a href="<c:url value='/home'/>"  class="up-menu">로그인</a>
					<%
				}
				else{
					%>
					<a href="<c:url value='/logout'/>"  class="up-menu" >로그아웃</a>
					<a href="<c:url value='/alert'/>"  class="up-menu">알림 데이터 불러오기</a>
					<%
				}
				%>
			</div>
			<div class="navbar-default sidebar" role="navigation"
				style="margin-top: 19px;">
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">

						<li><a href="<c:url value='/dashboard'/>" ><i class="fa fa-hashtag"
								aria-hidden="true"></i> 대시보드</a></li>
						<li><a><i class="fa fa-bar-chart" aria-hidden="true"></i>
								차트 보고서<span class="fa arrow"></span></a>
							<ul class="nav nav-second-level">
								<li><a id="s" href="<c:url value='/summary'/>" ><i class="fa fa-book"
										aria-hidden="true"></i> 종합 및 요약</a></li>
								<li><a id="c" href="<c:url value='/country'/>" ><i class="fa fa-globe"
										aria-hidden="true"></i> 국가 및 IP</a></li>
								<li><a id="t" href="<c:url value='/time'/>" ><i class="fa fa-clock-o"
										aria-hidden="true"></i> 시간 및 공격 유형</a></li>
							</ul> <!-- /.nav-second-level --></li>
						<li><a href="<c:url value='/table'/>" ><i class="fa fa-table"
								aria-hidden="true"></i> 테이블 보고서</a></li>
						<li><a href="<c:url value='/print'/>" ><i class="fa fa-print"
								aria-hidden="true"></i> 보고서 출력</a></li>
						<li><a href="<c:url value='/others'/>" ><i class="fa fa-magic"
								aria-hidden="true"></i> 고객센터</a></li>
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
				<h3>
					<i class="fa fa-print" aria-hidden="true"></i> 보고서 출력
				</h3>
				<h5>총괄 보고서를 출력 할 수 있는 페이지 입니다.</h5>
			</div>
		</div>
		<div class="row" style="display: none;">
			<div class="col-lg-6" style="margin-top: 10px;">
				<div class="panel panel-default panel-chart">
					<!-- 종합 거미줄 차트 -->
					<div id="spider"></div>
				</div>
			</div>
			<div class="col-lg-6" style="margin-top: 10px;">
				<div class="panel panel-default panel-chart"
					style="overflow: hidden;">
					<!-- 예상피해 BAR 차트  -->
					<div id="bar"></div>
				</div>
			</div>
		</div>
		<canvas id="canvas" style="display: none;"></canvas>
		<canvas id="canvas2" style="display: none;"></canvas>
		<!-- /.col-lg-12 -->
		<div class="row">

			<div class="col-lg-12">
				<div class="panel"
					style="background-color: #808080; padding-bottom: 17px;">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-12"></div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-lg-12">

				<div id="pdf" style="padding-bottom: 20px; height: 700px;"></div>

			</div>
		</div>

	</div>
	<!-- /#wrapper -->


	<!-- jQuery -->
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="resources/vendor/bootstrap/js/bootstrap.min.js"></script>
	<script
		src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>
	<script
		src="https://cdn.datatables.net/1.10.15/js/dataTables.bootstrap.min.js"></script>

	<script type="text/javascript"
		src="http://canvg.github.io/canvg/rgbcolor.js"></script>
	<script type="text/javascript"
		src="http://canvg.github.io/canvg/StackBlur.js"></script>
	<script type="text/javascript"
		src="http://canvg.github.io/canvg/canvg.js"></script>
	<!-- Metis Menu Plugin JavaScript -->
	<script src="resources/vendor/metisMenu/metisMenu.min.js"></script>

	<!-- HighCharts Charts JavaScript -->
	<script src="https://code.highcharts.com/highcharts.js"></script>
	<script src="https://code.highcharts.com/highcharts-more.js"></script>
	<script src="https://code.highcharts.com/modules/exporting.js"></script>

	<!-- print pdf -->
	<script src="resources/js/pdfobject.js"></script>

	<!-- Custom Theme JavaScript -->
	<script src="resources/dist/js/sb-admin-2.js"></script>

	<script>
		PDFObject.embed("resources/report.pdf", "#pdf");
	</script>

</body>

</html>