<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap" rel="stylesheet">
<style>
*{
font-family: 'Noto Sans KR', sans-serif;
}
.contents{
width: 50%;
margin: auto;
}

.menu{
margin-top: 5vh;
display:flex;
justify-content: space-around;
align-items: center;
}
.adjustBtn {
width: 15vw;
border: none;
background-color: white;
padding-top: 3vh; 
margin-bottom: 5vh;
font-size: 20px;
border-radius: 10px;
}

.adjustBtn:hover{
background-color: #C0C0C0;
}

.menuIcon {
width: 10vw;
}
</style>
</head>
<body>
<%@ include file="./header.jsp" %>
<%
 session.setAttribute("searchClass", "0");
 session.setAttribute("pageRange", "10");
 %>
<div class="contents">

 <h1 style ="text-align:center">관리자 페이지</h1>
 <div class="menu">
 <button type="button" class="adjustBtn" onclick="location.href='/member_1조/manage_page.jsp?currentPageNo=0&pageRange=10&searchClass=0'">
  <img class="menuIcon" src="https://cdn-icons-png.flaticon.com/512/456/456283.png">
  <br> 
 <p>회원 관리</p>
 </button> 
 
 <button type="button" class="adjustBtn" onclick = "alert('준비 중인 기능입니다.')" />
  <img class="menuIcon" src="https://cdn-icons-png.flaticon.com/512/1719/1719454.png">
  <br> 
  <p>게시물 관리</p>
 </button> 
</div>

</div>

<%@ include file="./footer.jsp" %>
</body>
</html>