<%@page import="com.spring.prolog.connection.DBConnection"%>
<%@page import="com.spring.prolog.back.Pop"%>
<%@page import="com.spring.prolog.back.Scheduler"%>
<%@page import="com.spring.prolog.dto.UserDTO"%>
<%@page import="com.spring.prolog.dao.DAO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.swing.JOptionPane" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.*"%>

<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>PRO|LOG</title>
      
      
<link href='https://fonts.googleapis.com/css?family=Titillium+Web:400,300,600' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css">
<link rel="stylesheet" href="resources/css/style.css">   
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
<body id="home-background">

	<nav class="navbar navbar-default navbar-fixed-top" role="navigation" style="margin-bottom: 0">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"></button>
	        <a class="navbar-brand" href="#" style="font-weight: bold;"><i class="fa fa-pinterest-p" aria-hidden="true"></i>ro|Log</a>
	    </div>
	    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="background-color: #ededed;">
	    	<ul class="nav navbar-nav navbar-right" style="padding-right: 20px;"></ul>
	  	</div>
	</nav>
  
	<div class= container style= "padding-top : 100px;">
	<div class = row>
	  <div class="form">
      
      <div id="taps">
	  <ul class="tab-group">
	    <li id="tab1" class="tab active"><a href="#login">로그인</a></li>
	    <li id="tab2" class="tab"><a href="#signup">회원가입</a></li>
	  </ul>
		</div>
      
      <div class="tab-content">
              <div id="login">   
          <h1 style = "color: white; padding-bottom : 20px;">로그인</h1>
          
          <form id="form" name="form" action="<c:url value='/home'/>"; method="post">
          
            <div class="field-wrap">
            <label>
              이메일<span class="req">*</span>
            </label>
            <input id="loginEmail" name="loginEmail" type="email" class="sign-up-form"/>
          </div>
          
          <div class="field-wrap">
            <label>
              비밀번호<span class="req">*</span>
            </label>
            <input id="loginPasswd" name="loginPasswd" type="password" class="sign-up-form"/>
          </div>
          
          <p class="forgot"><a href="nhome" style ="color : #337ab7;">파일 업로드</a></p>
          
          <input type="submit" class="button button-block" value="확인" style="height:55px;"/>
          
          </form>
		
		</div>
      
        <div id="signup">   
          <h1 style = "color : white; margin-bottom: 20px;">회원가입</h1>
          
          <form id="signUp" name="singUp" action="<c:url value='/home'/>"; method="post" >
          
          <div class="top-row">
            <div class="field-wrap">
              <label>
                성<span class="req">*</span>
              </label>
              <input type="text" id="firstName" name="firstName" class="sign-up-form"/>
            </div>
        
            <div class="field-wrap">
              <label>
                이름<span class="req">*</span>
              </label>
              <input type="text" id="lastName" name="lastName" class="sign-up-form"/>
            </div>
          </div>

          <div class="field-wrap">
            <label>
              이메일<span class="req">*</span>
            </label>
            <input type="email" id="email" name="email" class="sign-up-form"/>
          </div>
          
          <div class="field-wrap">
            <label>
              비밀번호<span class="req">*</span>
            </label>
            <input type="password" id="passwd" name="passwd" class="sign-up-form"/>
          </div>
          
          <input type="submit" class="button button-block" value="확인" style="height:55px;"/>
          
          
          </form>

        </div>
        
		</div>
        </div>
        
      </div><!-- tab-content -->
      
	</div> <!-- /form -->

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

  	<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

    <script  src="resources/js/index.js"></script>
	<script>
	
	<%
	
	String id = (String)session.getAttribute("id");
	if(id != null) {
		if(!id.equals("test")) response.sendRedirect("nhome");
		else session.invalidate();
	}
	
	String email = request.getParameter("email");
	if(email != null){
		DAO dao = new DAO();
		UserDTO dto = new UserDTO();
		
		String firstName = request.getParameter("firstName");
		firstName = new String(firstName.getBytes("8859_1"),"UTF-8");
		String lastName = request.getParameter("lastName");
		lastName = new String(lastName.getBytes("8859_1"),"UTF-8");
		
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		dto.setEmail(request.getParameter("email"));
		dto.setPasswd(request.getParameter("passwd"));
		
		int r = dao.idCheck(dto);
		System.out.println(r);
		if(r == 0){
			System.out.println("사용 중인 이메일");
			//pop.Up(0);%>
			$("#tab1").removeClass("active");
			$("#login").css("display", "none");
			$("#tab2").addClass("active");
			$("#signup").css("display", "block");
			alert("이미 가입된 이메일입니다.");
			<%
		}
		else if(r == 1){
			Scheduler scheduler = new Scheduler(request.getParameter("email"));
			System.out.println("회원 가입 완료");
			//pop.Up(1);
			%>
			alert("회원가입이 완료되었습니다.");
			<%
		}
	}
	%>
	</script>
	
	<script>
	
	<%
	String loginEmail = request.getParameter("loginEmail");
	if(loginEmail != null){
		Pop pop = new Pop();
		DAO dao = new DAO();
		int r2 = dao.loginCheck(request.getParameter("loginEmail"), request.getParameter("loginPasswd"));
		System.out.println(request.getParameter("loginEmail") + " " + request.getParameter("loginPasswd"));
		System.out.println(r2);
	
		if(r2 == 0){
			System.out.println("로그인 불가능");
			//pop2.Up(2);
			%>
			alert("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
			<%
		}
		else if(r2 == 1){
			System.out.println("로그인 가능");
			//client2.sendFile(request.getParameter("loginEmail"));
			
			session.setAttribute("id", loginEmail);
			//session.setMaxInactiveInterval(30);
			
			%>
			document.getElementById('form').action = "<c:url value='/nhome'/>";;
			document.getElementById('loginEmail').value = '<%=request.getParameter("loginEmail")%>';
			document.getElementById('form').submit();
			<%
		}
	}
	%>
	</script>
</body>
</html>