<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 확인</title>
<style>
/* 입력창 디자인 및 폰트 */    
	.input-search {        
		display: block;
		float: left;

		background-color: #FFFFFF;
		border: 1px solid #CCCCCC;
		border-radius: 5px;
		box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.07);

		width: 180px; height: 24px;
		padding: 0 0 0 7px;
		font-weight:bold;
		font-size: 13px;
		color: #555555;
     }
     
   	/* 입력창 활성화 시 */   
    /* shadow : x축크기, y축크기, 흐림정도, 색깔 */
	.input-search:focus {
            border-color: rgba(82, 168, 236, 1.0);
            outline: 0;
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
            box-shadow: 0 0 10px #B2CCFF;
            
    }
    
    /* 라벨 및 입력창 영역 */
	.control-group{
		margin-bottom:9px;
		*zoom:1;
		display:table;
		line-height:0;
		content:"";
		clear:both;
	}
     
	/* 입력창 비활성화 색상 */
	input:disabled {
		background: #F0F0F0;
	}
	
	/* 버튼 디자인 */
	.btn-inverse{
		color:#ffffff!important;
		text-shadow:0 -1px 0 rgba(0, 0, 0, 0.25);
		background-color:#363636;
		border-radius: 5px;
		border-color:#222222 #222222 #000000;
		border-color:rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
	}
</style>
</head>
<body>
<%@ include file="../header.jsp" %>
<%
	String driverName = "org.mariadb.jdbc.Driver";
	String url = "jdbc:mariadb://localhost/member_db";
	String user = "root";
	String passwd = "root";

	Class.forName(driverName);
	Connection con = DriverManager.getConnection(url,user,passwd);
	Statement stmt = con.createStatement();
	request.setCharacterEncoding("utf-8");
%>

<%
	String userID = (String)session.getAttribute("userid");
	//세션객체에서 로그인 상태(해당 id),로그아웃 상태(null)반환
	
	int mem_num;
	String mem_name;
	String mem_id;
	String mem_passwd;
	String mem_email;
	String mem_phone;
	String mem_RRN;
	
	String sql = "select * from member where mem_id='"+userID+"';"; //where조건이 String타입이므로 값 양쪽에 작은따옴표 
	ResultSet rs = stmt.executeQuery(sql);
	
	while(rs.next()){
		mem_num = rs.getInt("mem_num");
		mem_name = rs.getString("mem_name");
		mem_id = rs.getString("mem_id");
		mem_passwd = rs.getString("mem_passwd");
		mem_email = rs.getString("mem_email");
		mem_phone = rs.getString("mem_phone");
		mem_RRN = rs.getString("mem_RRN");
		
%>
<div style="width:1200px; margin:0 auto;"><!-- 수정페이지 전체 영역 좌우 지정 -->
<div style="margin-top:40px; margin-bottom:100px;"><!-- 수정페이지 전체 영역 위아래 지정 -->
	<div>
		<h1 style="border-bottom:1px solid #ccc ; padding-bottom:20px; margin-bottom:40px ;" >컴퓨터정보계열</h1>
	</div>
	<h3 style="margin-bottom:0;" >비밀번호 재확인</h3>
	<p style="font-size:14px; font-weight:bold;">회원의 정보를 안전하게 보호하기 위해 비밀번호를 다시 한번 확인 합니다.</p>
	<form method="post" action="./mem_models/member_dao.jsp">
	<input type="hidden" name="mem_num" value=<%=mem_num%> readonly>
	
	<div class="control-group">
	<input type="text" class="input-search" name="mem_id" size="30" value=<%=mem_id%> disabled>
	</div>
	
	<div class="control-group">
	<input type="password" class="input-search" name="passwdCK" size="30" value="">
	<button type="submit" class="btn-inverse" id="check_pw" name="actionType" value="mem_check_pw">확인</button>
	</div>
	</form>
<%
	}
%>
</div>
</div>
<% 
	//입력한 패스워드 불일치 시 member_dao.jsp에서 세션객체 passwdCheck의 값을 false로 바꿈 
	if((String)session.getAttribute("passwdCheck")=="false"){
%>
<script>
		window.alert("패스워드가 일치하지 않습니다.");
</script>
<%
		session.setAttribute("passwdCheck", null); //경고후 값 초기화
	}
%>
<%@ include file="../footer.jsp" %>
</body>
</html>