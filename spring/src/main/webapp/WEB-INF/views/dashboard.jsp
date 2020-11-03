<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@page import="com.spring.prolog.back.SqlInjection"%>
<%@page import="com.spring.prolog.dto.DTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.spring.prolog.dao.DAO"%>
<%@page import="com.spring.prolog.database.MyConstants"%>
<%@page import="com.spring.prolog.connection.DBConnection"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

    <!-- MetisMenu CSS -->
    <link href="resources/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="resources/dist/css/sb-admin-2.css" rel="stylesheet">

  

    <!-- Custom Fonts -->
    <link href="resources/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
     <link href="resources/css/custom.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">
	
	<div class="row" style="display:none;">
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
	    <canvas id="canvas" style="display:none;"></canvas>
	    <canvas id="canvas2" style="display:none;"></canvas>
        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="nhome" style="font-weight: bold;"><i class="fa fa-pinterest-p" aria-hidden="true"></i>ro|Log</a>
            </div>
            <!-- /.navbar-header -->
            <!-- /.navbar-top-links -->
			<div id="main">
			<%
			String id = "";
			
			id = (String)session.getAttribute("id");
			if(id.equals("test")){
				%>
				<a href="home" class="up-menu">로그인</a>
				<%
			}
			else{
				%>
				<a href="logout" class="up-menu">로그아웃</a>
				<a href="alert" class="up-menu">알림 데이터 불러오기</a>
				<%
			}
			%>
			</div>
            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li>
                            <a href="#"><i class="fa fa-hashtag" aria-hidden="true"></i> 대시보드</a>
                        </li>
                        <li>
                            <a><i class="fa fa-bar-chart" aria-hidden="true"></i> 차트 보고서<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="summary"><i class="fa fa-book" aria-hidden="true"></i> 종합 및 요약</a>
                                </li>
                                <li>
                                	<a href="country"><i class="fa fa-globe" aria-hidden="true"></i> 국가 및 IP</a>
                                </li>
                                <li>
                                	<a href="time"><i class="fa fa-clock-o" aria-hidden="true"></i> 시간 및 공격 유형</a>
                                </li>
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>
                        <li>
                        	<a href="table"><i class="fa fa-table" aria-hidden="true"></i> 테이블 보고서</a>
                        </li>
                        <li>
                        	<a href="print"><i class="fa fa-print" aria-hidden="true"></i> 보고서 출력</a>
                        </li>
                        <li>
                        	<a href="others"><i class="fa fa-magic" aria-hidden="true"></i> 고객센터</a>
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
                    <h1 class="page-header"><i class="fa fa-hashtag" aria-hidden="true"></i>대시보드</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
               <div class="row">
         <div class="col-lg-12">
         <%
         	System.out.println("dashboard   " + DBConnection.getDBNAME());
          	
         	DAO dao = new DAO();
         	int checkedLog = dao.getRowNum("select * from userLog");
         	int detectedLog = dao.getRowNum("select * from warningLog");
         	int dangerousLog = dao.getRowNum("select * from warningLog where AttackLevel='4' or AttackLevel='5' or AttackLevel='6'");
       
           if(detectedLog == 0){
                 %>
                 <div class="jumbotron jumbotron-fluid" id="c_background">
               <div id="warnfont">안전합니다</div>
               <div id="subwarnfont">사용자님의 PC는 현재 안전한 상태입니다.</div>
               <%
           }
           else if(dangerousLog == 0){
                 %>
                  <div class="jumbotron jumbotron-fluid" id="w1_background" >
               <div id="warnfont" style="color: orange;">위험1단계</div>
               <div id="subwarnfont">사용자님의 PC는 현재 약간 위험한 상태입니다.</div>
               <div id="subwarnfont">보고서를 참고해 주세요.</div>
                  <%
           }
           else{
                  %>
                  <div class="jumbotron jumbotron-fluid" id="w2_background">
               <div id="warnfont" style="color: red;">위험2단계</div>
               <div id="subwarnfont">사용자님의 PC는 현재 매우 위험한 상태입니다.</div>
               <div id="subwarnfont">전문가와의 상담이 당장 필요합니다. 보고서를 참고해 주세요.</div>
                  <%
           }
           %>   
              
        <div class="container">
     </div>
      </div>
         </div>
      </div>
      <div class="row">
         <div class="col-lg-6">
             <div class="panel panel-yellow">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-check-square-o fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                   <div style="font-weight: bold; font-size: 20px;">탐지된 로그 / 검사한 로그</div>
                                    <div style="font-size: 50px;"><%=detectedLog %>/<%=checkedLog %></div>
                                </div>
                            </div>
                        </div>
         </div>
      </div>
         
         <div class="col-lg-6">
             <div class="panel panel-red">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-exclamation-circle fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div style="font-weight: bold; font-size: 20px;">위험도가 높은 로그 / 탐지된 로그</div>
                                    <div style="font-size: 50px;"><%=dangerousLog %>/<%=detectedLog %></div>
                                </div>
                            </div>
                        </div>
         </div>
      </div>
      
      <div>
          <button type="button" class="btn btn-default btn-lg" style="margin: 0 auto; display:block;"onclick="location.href='summary'">
             <i class="fa fa-hand-o-right fa-1x" aria-hidden="true"></i>자세히<i class="fa fa-hand-o-left fa-1x" aria-hidden="true"></i>
          </button>
      </div>
      <div>
         <p style="text-align: center; margin-top: 10px;">자세한 정보를 보려면 위 버튼을 클릭하시오.</p>
      </div>
        </div>
        <!-- /#page-wrapper -->
	</div>
    </div>
    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="resources/vendor/jquery/jquery.min.js"></script>

   
<script type="text/javascript" src="http://canvg.github.io/canvg/rgbcolor.js"></script> 
<script type="text/javascript" src="http://canvg.github.io/canvg/StackBlur.js"></script>
<script type="text/javascript" src="http://canvg.github.io/canvg/canvg.js"></script> 
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/canvg/1.4/rgbcolor.min.js"></script>
<!-- Optional if you want blur -->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/stackblur-canvas/1.4.1/stackblur.min.js"></script>
<!-- Main canvg code -->
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/canvg/dist/browser/canvg.min.js"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="resources/vendor/bootstrap/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="resources/vendor/metisMenu/metisMenu.min.js"></script>
        <script src="https://code.highcharts.com/highcharts.js"></script>
	<script src="https://code.highcharts.com/highcharts-more.js"></script>
	<script src="https://code.highcharts.com/modules/exporting.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="resources/dist/js/sb-admin-2.js"></script>

	    <%
	int[] numLevel = new int[6];
	String[] P = {"Pattern Block", "Web CGI", "Scanning", "LFI & RFI", "Directory Listing", "SQL Injection"};

	for(int i=0; i<numLevel.length; i++){
		int j = i + 1;
		numLevel[i] = dao.getRowNum("select * from warningLog where AttackLevel='" + j + "'");
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
<%
int top = 0, middle = 0, bottom = 0;
bottom = dao.getRowNum("select * from warningLog where AttackLevel='1'");
middle = dao.getRowNum("select * from warningLog where AttackLevel='2' or AttackLevel='3'");
top = dao.getRowNum("select * from warningLog where AttackLevel='4' or AttackLevel='5' or AttackLevel='6'");

int account = top + middle + bottom;
double topP = 0, middleP = 0, bottomP = 0;
topP = top * 100.0 / account;
topP = Math.round(topP * 100d) / 100d;
middleP = middle * 100.0 / account;
middleP = Math.round(middleP * 100d) / 100d;
bottomP = bottom * 100.0 / account;
bottomP = Math.round(bottomP * 100d) / 100d;
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

		    plotOptions: {
		        series: {
		            animation: false
		        }
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
		        color: '#f70404',
		    }, {
		        name: '빈도수',
		        data: [<%=numLevel[5]%>, <%=numLevel[4]%>, <%=numLevel[3]%>, <%=numLevel[2]%>, <%=numLevel[1]%>, <%=numLevel[0]%>],
		        pointPlacement: 'on'
		    }]

		});
		    </script>
		    <script type="text/javascript">
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
		    legend: {
		        enabled: false
		    },
		    plotOptions: {
		        series: {
		            borderWidth: 0,
		            dataLabels: {
		                enabled: true,
		                format: '{point.y:.1f}%'
		            },
		            animation: false
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
	
    <script>
	    $(document).ready(function(){
	        var svg = document.getElementById('spider').children[0].innerHTML;
	        canvg(document.getElementById('canvas'),svg);
	        var img = canvas.toDataURL("image/png"); //img is data:image/png;base64
	        img = img.replace('data:image/png;base64,', '');
	        var data = "bin_data=" + img;
	        console.log(data);
	        
	        var svg2 = document.getElementById('bar').children[0].innerHTML;
	        canvg(document.getElementById('canvas2'),svg2);
	        var img2 = canvas2.toDataURL("image/png"); //img is data:image/png;base64
	        img2 = img2.replace('data:image/png;base64,', '');
	        data = data + "&bin_data2=" + img2;
	        $.ajax({
	          type: "POST",
	          url: "/prolog/print",  // this is the servlet url
	          data: data,
	          success: function(data){
	        	  
	          }
	        });
	    });
    </script>
</body>

</html>