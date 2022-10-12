<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width", initial-scale="1">
<!-- 사용자의 해상도에 맞게 자동으로 디자인되는 메타태그 -->
<link rel="stylesheet" href="CSS/bootstrap.css">
<!-- CSS폴더의 부트스트랩.css를 쓰겠다는 링크 -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap" rel="stylesheet">

<style>
.container {
width: 60%;
margin: 0 auto;
}
.img_in {
	width: 100%;
/* 	background-size : cover; */
	position: relative;
}
.img_size {
	width: 100%;
}
/* 요소를 이동시킬때 사용되는 transform,translate(x축,y축) =>중앙정렬 */
.img_in h1 {
    position: absolute;
    top: 40%;
    left: 50%;
    color: white;
     transform: translate( -50%, -50% );
}
.img_in h3 {
    position: absolute;
    top: 55%;
    left: 50%;
    color: white;
     transform: translate( -50%, -50% );
}
.terms_of_service_scroll {
	max-height: 200px;
	overflow-y:scroll;
}
.terms_of_service_container{
	padding: 9.5px;
	border : 1px solid #C0C0C0;
	border-radius: 10px;
	background-color:#F8F8F8;
}
.container_scroll {
  padding : 9.5px;
}
.container_scroll hr{
	border : 1px dotted #C0C0C0
 } 

.terms_of_service_scroll p{
	font-size: 12.6px;
 	font-family: 'Noto Sans KR', sans-serif;
}
.signups {
  padding-right: 90px;
  padding-left: 90px;
  margin-right: auto;
  margin-left: auto;
}
.signups span{
    float: left;
    width : 120px;
    text-align: right;
	font-family: 'Noto Sans KR', sans-serif;
	text-align : right;
	/*왼쪽 정렬*/ 
	margin-right: 30px;
	
}
.signups b{
	color : red;
}
.selectBtn{
	border: 1px solid #CCCCCC;
	background-color: white;
}

.selectMenu{
	display:flex;
	justify-content: space-between;
}
</style>

<title>회원가입</title>
</head>
<body>
   <%
   String driverName = "org.mariadb.jdbc.Driver";
   String url = "jdbc:mariadb://localhost/member_db";
   String user = "root";
   String passwd = "root";
   
   Class.forName(driverName);
      Connection con = DriverManager.getConnection(url,user,passwd);
      Statement st = con.createStatement();
      request.setCharacterEncoding("UTF-8");
      String sql;
      PreparedStatement pstmt;
      String mem_ids;
      String mem_emails;
      String mem_rrns;
   %>
   <%@ include file="./header.jsp"%>
	
	<div class="container"> <!-- 고정된 화면을 가졌기에 container를 사용 -->
   		<h1><b>회원가입</b></h1>
   		<hr>
  		<br>
 		<h4 style="border-bottom:solid 1px gray"><b>회원가입</b></h4>
 		<br>
 		<div class="terms_of_service_container">
 			<div class="terms_of_service_scroll">
  				<p>제 1 조(목적)</p>
  				<p>이 약관은 대구광역시 북구 복현로35 소재 영진전문대학교 컴퓨터정보계열(이하 "컴퓨터정보계열" 이라 한다)에서 제공하는 홈페이지 서비스에 대한 이용자의 절차 및 책임 사항을 규정함을 목적으로 합니다.</p>
				<p>제 2 조(용어의 정의)</p>
				<p>이 약관에서 사용하는 용어의 정의는 다음과 같습니다.</p>
				<p>① 회원 : 컴퓨터정보계열에 개인정보를 제공하는 회원등록을 한 이용자로서 아이디를 부여받은 자.</p>
				<p>② 아이디(ID) : 회원 식별과 회원의 서비스 이용을 위하여 회원이 선정하고 컴퓨터정보계열이 승인하는 문자와 숫자의 조합</p>
				<p>③ 비밀번호 : 회원의 비밀 보호를 위해 회원 자신이 설정한 문자와 숫자의 조합</p>
				<p>제 3 조(회원가입)</p>
				<p>① 이용자는 약관에 동의한다는 의사표시를 함으로써 컴퓨터정보계열의 가입양식에 따라 회원정보를 기입, 등록 후 회원이 될 수 있습니다.</p>
				<p>② 회원은 등록 정보의 변경이 있을 경우 온라인으로 수정해야 하고 회원의 정보를 변경 하지 않아서 발생되는 불이익이나 문제에 대한 책임은 회원 본인에게 있습니다.</p>
				<p>제 4 조(회원 탈퇴 및 자격 상실)</p>
				<p>① 컴퓨터정보계열의 회원 탈퇴는 언제든지 진행할 수 있습니다.</p>
				<p>② 회원이 다음 각 호의 사유에 해당하는 경우에 컴퓨터정보계열은 사전 통보 없이 회원자격을 상실 시키거나 정지할 수 있습니다.</p>
				<p>- 다른 회원의 ID 및 비밀번호를 도용한 경우</p>
				<p>- 서비스 운영을 고의로 방해한 경우</p>
				<p>- 가입한 이름이 실명이 아닌 경우</p>
				<p>- 같은 사용자가 다른 ID로 이중 등록한 경우</p>
				<p>- 타인의 ID 및 비밀번호를 부정하게 사용한 경우</p>
				<p>- 타인의 명예를 손상시키거나 불이익을 주는 행위를 한 경우</p>
				<p>- 컴퓨터정보계열을 통하여 얻은 서비스 정보를 컴퓨터정보계열의 사전 승낙 없이 복제 또는 유통하거나 상업적으로 이용한 경우</p>
				<p>제 5 조(컴퓨터정보계열의 의무)</p>
				<p>① 컴퓨터정보계열은 이용자가 안전하게 서비스를 이용할 수 있도록 이용자의 개인정보 보호를 위한 시스템을 갖추어야 합니다.</p>
				<p>② 컴퓨터정보계열은 취득한 회원 개인정보를 본인의 사전 승낙 없이 타인에게 누설, 공개 배포할 수 없으며 서비스 업무 이외</p>
				<p>상업적인 목적으로는 사용할 수 없습니다. 단, 다음 각 호의 사유에 해당하는 경우에는 예외입니다.</p>
				<p>- 법률이 규정에 의해 국가기관의 요구가 있는 경우</p>
				<p>- 범죄에 대한 수사상의 목적으로 요청이 있는 경우</p>
				<p>- 정보통신 윤리 위원회의 요청이 있는 경우</p>
				<p>- 기타 관계법령에서 정한 절차에 따른 요청이 있는 경우</p>
			</div>
		<div class="container_scroll">
		<hr>
		<input type="checkbox">약관을 모두 읽었으며 동의합니다.
		</div>
 		</div>
  		<br>
  		<br>


   <form name="signup" class="signup" method="post" action="./mem_models/member_dao.jsp" onsubmit="return blankCheck()">
    <div class="signups">
   	  <div style="line-height:150%">
      <span><b>* </b>이름</span>
      <input type="text" name="mem_name" size="30">
      </div>
      <br>
      
      <div style="line-height:150%">
      <span><b>* </b>아이디</span>
      <input type="text" name="mem_id" id="mem_id" size="30">
      <input type="button" class="selectBtn" name="OverlapCheck" id="check_id" onclick="checkId()" value="중복체크">
      <small id="lapcheck">중복체크 확인하세요!</small> 
      </div>
      <br>
      
      <div style="line-height:150%">
      <span><b>* </b>비밀번호</span>
      <input  type="password" name="mem_passwd" size="30">
      </div>
      <br>
      
      <div style="line-height:150%">
      <span><b>* </b>이메일 주소</span>
      <input type="text" name="mem_email" size="30"> 
      <input type="button" class="selectBtn" name="email_check" id="check_email" onclick="checkEmail()" value="중복체크">
      <small id="email_check"></small> 
      </div>
      <br>
      
      <div style="line-height:150%">
      <span><b>* </b>휴대폰 번호</span>
      <input type="text" name="mem_phone" size="30" placeholder="010-1234-5678">
      </div>
      <br>
      
      <div style="line-height:150%">
      <span><b>* </b>주민등록번호</span>
      <input type="text" name="mem_RRN1" id="userRrn1" size="12" maxlength="6">-
      <input type="password" name="mem_RRN2" id="userRrn2" size="12" maxlength="7">
      <input type="button" class="selectBtn" value="중복체크" id="Rrn" onclick="chkRrn();" />
      <small id="checkRrn"></small>
      </div>
      <br>
      </div>
	
	     
	<h4 style="border-bottom:solid 1px gray"></h4>
	<input type="hidden" name="actionType" value="SIGNUP">
      <div class="selectMenu"> 
     <button type="button" class="selectBtn"> <a href="/member_1조/index.jsp">취소</a></button> 
   		<input type="submit" class="selectBtn" value="등록" style="background-color:black; color: white;">
   </form>
   </div>
   

</div>


   <script>
      var OverCheck = 0;
      var checkRRN = -1;
      var emailcheck = -1;
      
      function blankCheck() { //빈칸있는지, 중복체크 확인 했는지 함수
         <%
         sql = "SELECT mem_id FROM member ORDER BY mem_id";
         ResultSet rs = st.executeQuery(sql);
         %>
         var fun = document.signup;
         

         if (fun.mem_name.value == "") {
            alert("이름을 입력해주세요");
            fun.mem_name.focus();
            return false;
         }
         if (OverCheck == 0 && fun.mem_id.value != ""){
            alert("중복되는지 확인해주세요.")
            fun.mem_id.focus();
            return false;
         }
         if (fun.mem_id.value == "") {
            alert("아이디를 입력해주십시오");
            fun.mem_id.focus();
            return false;
         }
         if (fun.mem_passwd.value == "") {
            alert("비밀번호를 입력해주십시오");
            fun.mem_passwd.focus();
            return false;
         }
         if (fun.mem_email.value == "") {
            alert("이메일을 입력해주십시오");
            fun.mem_email.focus();
            return false;
         }
         if (emailcheck == -1 && fun.mem_email.value != ""){
             alert("이메일이 중복되는지 확인해주세요.")
             fun.mem_email.focus();
             return false;
          }
         if (fun.mem_phone.value == "") {
            alert("전화번호를 입력해주십시오");
            fun.mem_phone.focus();
            return false;
         }
         if (fun.mem_RRN1.value == "") {
            alert("주민등록번호 앞자리를 입력해주세요.");
            fun.mem_RRN1.focus();
            return false;
         }
         if (fun.mem_RRN2.value == "") {
             alert("주민등록번호 뒷자리를 입력해주세요.");
             fun.mem_RRN2.focus();
             return false;
          }
         if (checkRRN == -1 && fun.mem_RRN1.value != "" && fun.mem_RRN2.value != ""){
             alert("주민등록번호를 확인하여주십시오.")
             fun.mem_id.focus();
             return false;
          }
         
         
         if(OverCheck =! 0){ //혹시라도 중복아닌 아이디로 중복체크 확인 후 중복되는 아이디로 바꿨을 때의 경우
            <%
            while(rs.next()){
               mem_ids = rs.getString("mem_id");
             %> 
            if (fun.mem_id.value == "<%=mem_ids%>"){
               document.getElementById("lapcheck").innerHTML="중복되는 아이디입니다."
               fun.mem_id.focus();
               return false;
            }
            <%}%>
         }
         
         
      }

      function chkRrn(){
       	  var rrn1= document.getElementById("userRrn1");
       	  var rrn2= document.getElementById("userRrn2");
       	console.log(rrn2);
        <%
//          sql = "select mem_id from member where mem_id='?'";
//          pstmt = con.prepareStatement(sql);
//          pstmt.setString(1, mem_ids);
         sql = "SELECT SUBSTR(mem_RRN, -7) FROM member ORDER BY mem_id";
        rs = st.executeQuery(sql);
       	while(rs.next()){
            mem_rrns = rs.getString("SUBSTR(mem_RRN, -7)");
          %> 
         if (rrn2.value == "<%=mem_rrns%>"){
            document.getElementById("checkRrn").innerHTML="중복되는 주민등록번호입니다."
           	alert('중복되는 주민등록번호입니다.')
            rrn1.value = "";
       		rrn2.value = "";
            rrn1.focus();
            return false;
         }
         <%}%>
         var arrRrn1 = new Array();
      	  var arrRrn2 = new Array();
      	  
      	  for(var i=0; i<rrn1.value.length; i++){
      		  arrRrn1[i] = rrn1.value.charAt(i);
      	  }
      	  for(var i=0; i<rrn2.value.length; i++){
      		  arrRrn2[i] = rrn2.value.charAt(i);
      	  }
      	  
      	  var sum =0;
      	  
      	  for(var i=0; i<rrn1.value.length; i++){
      		  sum += arrRrn1[i]*(2+i);
      	  }
      	  for(var i=0; i<rrn2.value.length-1; i++){
      		  if(i>=2){
      			  sum+=arrRrn2[i] *i;
      		  }else{
      			  sum += arrRrn2[i] *(8+i);
      		  }
      	  }
       	  if((11-(sum%11))%10 != arrRrn2[6]){
       		  document.getElementById("checkRrn").innerHTML="X"
       		  alert('주민번호 검증실패')
       		  rrn1.value = "";
       		  rrn2.value = "";
       		  rrn1.focus();
       		  return false;
       	  }else{
       		  document.getElementById("checkRrn").innerHTML="O"
       		  checkRRN = 1;
       		  alert('주민번호 검증완료')
       	  }
         }

      function checkId() { //중복체크 함수
         var fun = document.signup;
         <%
          sql = "SELECT mem_id FROM member ORDER BY mem_id";
         rs = st.executeQuery(sql);
         
         while(rs.next()){
            mem_ids = rs.getString("mem_id");
          %> 
         if (fun.mem_id.value == "<%=mem_ids%>"){
            document.getElementById("lapcheck").innerHTML="중복되는 아이디입니다."
            fun.mem_id.focus();
            return false;
         }
         <%}%>
         else{
         document.getElementById("lapcheck").innerHTML="사용가능한 아이디입니다!"
         fun.mem_passwd.focus();
         OverCheck = 1;
         }
      }
      
      function checkEmail() { //중복체크 함수
          var fun = document.signup;
          var email= document.getElementById("user_email");
          <%
           sql = "SELECT mem_email FROM member ORDER BY mem_id";
          rs = st.executeQuery(sql);
          
          while(rs.next()){
             mem_emails = rs.getString("mem_email");
           %> 
          if (fun.mem_email.value == "<%=mem_emails%>"){
             document.getElementById("email_check").innerHTML="중복되는 이메일입니다."
             email.value = "";
             return false;
          }
          <%}%>
          else{
          document.getElementById("email_check").innerHTML="사용가능한 이메일입니다!"
          emailcheck=1;
          }
       }
      
      
   </script>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="JS/bootstrap.js"></script>


   <%@ include file="./footer.jsp"%>
</body>
</html>