<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
   href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap"
   rel="stylesheet">

<style>
body {
   font-family: 'Noto Sans KR', sans-serif;
}
.container {
width: 60%;
margin: 0 auto;
}
.loginContents{
   display: flex;
   align-items: center;
   justify-content: center;
}

.loginBox {
   width: 400px;
   height: 200px;
   border: 1px solid gray;
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   border-radius:20px;
   margin: 5vh;
   padding: 10px;
}
.loginText > input{
  background:transparent;
  border:none;
  border-bottom: solid 1px #ccc;
  padding:20px 0px 5px 0px;
  font-size:14pt;
  width:100%;
  
}
.loginBtn{
text-align:center;
}

.loginSubmit  {
 padding: 8px;
 background-color: white;
border-radius:10px;
 border: 1px solid #CCCCCC;
 text-align: center;
margin-top:10px;
}
</style>
<title>Insert title here</title>
</head>
<body>

   <%@ include file="../header.jsp"%>

   <%
   if (session.isNew()) {
      session.setAttribute("loginState", "logout");
      session.setAttribute("userid", null);
      session.setAttribute("userpw", null);
   }
   %>
   <div class="container"> 
   <h2>로그인</h2>
   <hr>
   <div class="loginContents">
      
      <div class="loginBox">   
      <h3>로그인</h3>
         <form name="login" method="post" action="/member_1조/mem_models/member_dao.jsp">
            <div class="loginText">
               <input type="text" name="userid" placeholder="아이디" /> <br> 
               <input type="password" name="passwd" placeholder="비밀번호" />
            </div>
            <div class="loginBtn">
               <input type="hidden" name="actionType" value="LOGIN"> <input
                  type="submit" class="loginSubmit"  value="로그인">
                <input type="button" class="loginSubmit" value="회원가입" onClick="location.href='./signup_page.jsp'" />
            </div>
         </form>
      </div>
   </div>
   </div>
   </div>
   </div>
   </div>
   <%@ include file="../footer.jsp"%>
</body>
</html>