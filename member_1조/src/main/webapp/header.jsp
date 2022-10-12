<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap" rel="stylesheet">


<style>
*{
	font-family: 'Noto Sans KR', sans-serif;
}

.navbar{
	float:right;
	width: 500px;
}
.navbar>div{
	display:inline-block;
}
.loginbar {
	height: 25px;
	border-bottom: 1px solid rgba(0, 0, 0, 0.2);
	margin-bottom: 10px;
	padding-bottom: 10px;
	padding-left: 80%;
}


.loginText>a {
	text-decoration: none;
	color: gray;
	font-weight: bold;
	font-size: 1em;
	line-height: 1.5em;
}
.snsLogo{
	margin-left: 5%;
}
.snsLogo>img{
	cursor:pointer;
}

.menubar {
	height: 60px;
	width: 80%;
	margin-left: 20%;
}

.menubar>* {
	display: inline-block;
}

.gnb>a {
	font-weight: bold;
	color: #222222;
	font-size: 1.2em;
	text-decoration: none;
	margin-left: 50px;
}
.gnb>a:hover{
	font-size: 1.22em;
	color: #24315b;
}
#managebutton{
	text-decoration:none;
	color: black;
	font-weight: bold;
}
</style>
</head>

<body>
	<%
	if (session.isNew()) {
		session.setAttribute("loginState", "logout");
		session.setAttribute("userid", null);
		session.setAttribute("userpw", null);
	}
	%>


	<div class="loginbar">
		<%
		if (session.getAttribute("loginState").equals("login")) {

			if (session.getAttribute("userclass").equals("30")) {
		%>


		<form name="logout" method="post"
			action="/member_1조/mem_models/member_dao.jsp">
			<%=session.getAttribute("userid")%>님 로그인 중 <input type="hidden"
				name="actionType" value="LOGOUT"> <input type="submit"
				value="로그아웃"> <a id="managebutton" href="/member_1조/manage_index_page.jsp">관리하기</a>
		</form>


		<%
		} else {
		%>


		<form name="logout" method="post"
			action="/member_1조/mem_models/member_dao.jsp">
			<%=session.getAttribute("userid")%>님 로그인 중 <input type="hidden"
				name="actionType" value="LOGOUT"> <input type="submit"
				value="로그아웃">
		</form>
		<%
		}
		} else {
		%>

		<div class="navbar">
			<div class="loginText">
				<a href="/member_1조/login_page.jsp">로그인</a> <a href="/member_1조/signup_page.jsp">회원
					가입</a>
			</div>
			<div class="snsLogo">
				<img src="/member_1조/images/snslogo/sns_facebook.png" onClick="location.href='https://www.facebook.com/yjcomp/'"/>
				<img src="/member_1조/images/snslogo/sns_insta.png" onClick="location.href='https://www.instagram.com/yju_computer_information/'" />
				<img src="/member_1조/images/snslogo/sns_kakao.png" onClick="location.href='https://pf.kakao.com/_XxmjHxb'" />
				<img src="/member_1조/images/snslogo/sns_blog.png" onClick="location.href='https://blog.naver.com/pescatore51'" />
				<img src="/member_1조/images/snslogo/sns_youtube.png" onClick="https://www.youtube.com/channel/UCIBpw77RP6q0NjkxpU_W13w'" />
				<img src="/member_1조/images/snslogo/sns_cafe.png" onClick="location.href='https://cafe.naver.com/ystory4u'" />
			</div>
		</div>
		<%
		}
		%>


	</div>
	<div class="menubar">
		<a href="/member_1조/index.jsp"> <img
			src="/member_1조/images/yju_logo_01.png"
			style="width: 25%; min-width: 400px;"></a>
		<%@ include file="/GNB.jsp"%>
	</div>
	<img class="bannerImg" src="/member_1조/images/banner.png" width=100% />
</body>
</html>