<%@page import="com.spring.prolog.connection.DBConnection"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*"%>

<html>
<head>
<style>                                         
   #file { width:0; height:0; }
   #loaderbackground{
   		position:fixed;
   		z-index:9998;
   		width:100%;
   		height:100%;
   		display:none;
   		background-color:rgba(0,0,0,0.3);
   } 
   #loader {
  position: fixed;
  left: 50%;
  top: 50%;
  z-index: 9999;
  margin: -35px 0 0 -35px;
  border: 4px solid #f3f3f3;
  border-radius: 50%;
  border-top: 4px solid #3498db;
  width: 80px;
  height: 80px;
  -webkit-animation: spin 2s linear infinite;
  animation: spin 2s linear infinite;
  display:none;
}              
@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}   
</style>

  <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>PRO|LOG - log reporter</title>

    <!-- Bootstrap Core CSS -->
    <link href="resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="resources/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="resources/dist/css/sb-admin-2.css" rel="stylesheet">

	<!-- Plugin CSS -->
    <link href="resources/vendor/magnific-popup/magnific-popup.css" rel="stylesheet">
	
    <!-- Custom Fonts -->
    <link href="resources/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
   <link href="resources/css/stylish-portfolio.css" rel="stylesheet">
   <link href="resources/css/creative.css" rel="stylesheet">
   <link href="resources/css/custom.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="loaderbackground"></div>
<div id="loader">
	
</div>
<nav class="navbar navbar-default navbar-fixed-top" role="navigation" style="margin-bottom: 0">

            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                </button>
                <a class="navbar-brand" href="<c:url value='/home'/>" style="font-weight: bold;"><i class="fa fa-pinterest-p" aria-hidden="true"></i>ro|Log</a>
            </div>
              <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="background-color: #ededed;">
                <ul class="nav navbar-nav navbar-right" style="padding-right: 20px;">
                	<li>
                		<a class="page-scroll" href="<c:url value='/home'/>" style="color: #777;">LOGOUT</a>
                	</li>
                	<li>
                		<a class="page-scroll" href="<c:url value='#home'/>" style="color: #777;">Home</a>
                	</li>
                    <li>
                        <a class="page-scroll" href="<c:url value='#services'/>" style="color: #777;">Services</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="<c:url value='#developers'/>"style="color: #777;">Developers</a>
                    </li>
                </ul>
            </div>
</nav>
<!-- <a class="navbar-brand" href="home" style="font-weight: bold;"><i class="fa fa-pinterest-p" aria-hidden="true"></i>ro|Log</a> -->
<section id="home" style="padding: 50px 0px;">
   <header id="top" class="header">
        <div class="text-vertical-center">
            <h1 style="font-weight: bold;"><i class="fa fa-pinterest-p" aria-hidden="true"></i>RO|LOG</h1>
            <h3>PRO|LOG는 여러분에게 편리한 로그 검사 기능을 제공합니다.</h3>
            <h5>버튼을 클릭하여 검사하고 싶은 로그파일을 선택해주세요.</h5>
            <br>
            <form name="form" method="post" enctype="multipart/form-data" action="dashboard">
                <input type="file" id="file" name="file" accept=".log"/>
               <a id="btn-upload" class="btn btn-dark btn-lg">PRO|LOG</a>
           </form>
        </div>
    </header>
 </section>
       <section id="services">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <h2 class="section-heading">At Our Service</h2>
                    <hr class="primary" style="border-color: #337ab7;
    border-width: 3px;
    max-width: 50px;">
                </div>
            </div>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-krw text-primary"></i>
                        <h3 style="font-weight:bold;">무료 프로그램</h3>
                        <p class="text-muted">이 프로그램은 누구나 무료로 이용할 수 있는 프로그램 입니다.</p>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-bar-chart fa-4x text-primary" aria-hidden="true"></i>
                        <h3 style="font-weight:bold;">시각화</h3>
                        <p class="text-muted">이 프로그램은 분석 결과를 누구나 쉽게 이용 할 수 있도록 다양한 차트를 이용하여 구성된 프로그램 입니다.</p>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-pagelines text-primary"></i> 
                        <h3 style="font-weight:bold;">초보자용</h3>
                        <p class="text-muted">이 프로그램은 처음 사용하는 이용자도 쉽게 사용할 수 있는 프로그램 입니다.</p>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-heart text-primary"></i>
                        <h3 style="font-weight:bold;">한국어 지원</h3>
                        <p class="text-muted">이 프로그램은 타 프로그램과 차별화 된 한국어를 지원하는 프로그램 입니다.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <section class="no-padding" id="developers">
        <div class="container-fluid">
            <div class="row no-gutter popup-gallery">
                <div class="col-lg-4 col-sm-6">
                    <a href="<c:url value='/resources/img/portfolio/fullsize/1.png'/>" class="portfolio-box">
                        <img src="<c:url value='/resources/img/portfolio/fullsize/1.png'/>" class="img-responsive" alt="">
                        <div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                    MADE BY
                                </div>
                                <div class="project-name">
                                    HWANG.S.H
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <a href="<c:url value='/resources/img/portfolio/fullsize/6.png'/>" class="portfolio-box">
                        <img src="<c:url value='/resources/img/portfolio/thumbnails/6.png'/>" class="img-responsive" alt="">
                        <div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                    MADE BY
                                </div>
                                <div class="project-name">
                                    PRO|LOG
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <a href="<c:url value='/resources/img/portfolio/fullsize/3.png'/>" class="portfolio-box">
                        <img src="<c:url value='/resources/img/portfolio/thumbnails/3.png'/>"class="img-responsive" alt="">
                        <div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                    MADE BY
                                </div>
                                <div class="project-name">
                                    KIM.M.J
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <a href="<c:url value='/resources/img/portfolio/fullsize/5.png'/>" class="portfolio-box">
                        <img src="<c:url value='/resources/img/portfolio/thumbnails/5.png'/>" alt="">
                        <div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                    MADE BY
                                </div>
                                <div class="project-name">
                                    JEON.Y.B
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <a href="<c:url value='/resources/img/portfolio/fullsize/4.png'/>" class="portfolio-box">
                        <img src="<c:url value='/resources/img/portfolio/thumbnails/4.png'/>" class="img-responsive" alt="">
                        <div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                    THANKS TO
                                </div>
                                <div class="project-name">
                                    KIM.D.Y
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <a href="<c:url value='/resources/img/portfolio/fullsize/2.png'/>" class="portfolio-box">
                        <img src="<c:url value='/resources/img/portfolio/thumbnails/2.png'/>" class="img-responsive" alt="">
                        <div class="portfolio-box-caption">
                            <div class="portfolio-box-caption-content">
                                <div class="project-category text-faded">
                                    MADE BY
                                </div>
                                <div class="project-name">
                                    KIM.M.K
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    </section>
    
    <!-- jQuery -->
    <script src="resources/vendor/jquery/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="resources/vendor/bootstrap/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="resources/vendor/metisMenu/metisMenu.min.js"></script>


    <!-- Custom Theme JavaScript -->
    <script src="resources/dist/js/sb-admin-2.js"></script>
    <script type="text/javascript"></script>

	<!--creative js  -->
	<script src="resources/js/creative.js"></script>
	    
    <!-- Plugin JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>
    <script src="resources/vendor/scrollreveal/scrollreveal.min.js"></script>
    <script src="resources/vendor/magnific-popup/jquery.magnific-popup.min.js"></script>
    
    <%
	    String id = "";
		
		id = (String)session.getAttribute("id");
		if(id.equals("test")){
			response.sendRedirect("home");
		}
		else{
			String dbEmail = id.replace("@", "");
		  	String dbN[] = dbEmail.split("\\.");
		  	String dbName = dbN[0] + "alert";
		 	DBConnection.setDBNAME(dbName);
		}
    
    %>
    
    
      <script>
      
      document.getElementById('loader').style.display="block";
	        	document.getElementById('loaderbackground').style.display="block";
	      		  document.form.action = "<c:url value='/dashboard'/>";
	                document.form.submit();
   
    
    $("#menu-close").click(function(e) {
        e.preventDefault();
        $("#sidebar-wrapper").toggleClass("active");
    });
    // Opens the sidebar menu
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#sidebar-wrapper").toggleClass("active");
    });
    // Scrolls to the selected menu item on the page
    $(function() {
        $('a[href*=#]:not([href=#],[data-toggle],[data-target],[data-slide])').click(function() {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') || location.hostname == this.hostname) {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                if (target.length) {
                    $('html,body').animate({
                        scrollTop: target.offset().top
                    }, 1000);
                    return false;
                }
            }
        });
    });
    //#to-top button appears after scrolling
    var fixed = false;
    $(document).scroll(function() {
        if ($(this).scrollTop() > 250) {
            if (!fixed) {
                fixed = true;
                // $('#to-top').css({position:'fixed', display:'block'});
                $('#to-top').show("slow", function() {
                    $('#to-top').css({
                        position: 'fixed',
                        display: 'block'
                    });
                });
            }
        } else {
            if (fixed) {
                fixed = false;
                $('#to-top').hide("slow", function() {
                    $('#to-top').css({
                        display: 'none'
                    });
                });
            }
        }
    });
    // Disable Google Maps scrolling
    // See http://stackoverflow.com/a/25904582/1607849
    // Disable scroll zooming and bind back the click event
    var onMapMouseleaveHandler = function(event) {
        var that = $(this);
        that.on('click', onMapClickHandler);
        that.off('mouseleave', onMapMouseleaveHandler);
        that.find('iframe').css("pointer-events", "none");
    }
    var onMapClickHandler = function(event) {
            var that = $(this);
            // Disable the click handler until the user leaves the map area
            that.off('click', onMapClickHandler);
            // Enable scrolling zoom
            that.find('iframe').css("pointer-events", "auto");
            // Handle the mouse leave event
            that.on('mouseleave', onMapMouseleaveHandler);
        }
        // Enable map zooming with mouse scroll when the user clicks the map
    $('.map').on('click', onMapClickHandler);
    </script>
    <script type="text/javascript">
    window.onload = function() {
        setTimeout (function () {
        scrollTo(0,0);
        }, 100);
    }
    </script>
</body>
</html>