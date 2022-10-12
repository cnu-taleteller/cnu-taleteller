<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 수정 및 삭제</title>
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
     
     /* 라벨, 입력창 복합 영역 */
     .form-horizontal{
     	margin-top:25px;
     	margin-bottom:10px;
     }
     
	/* 라벨 및 입력창 영역 */
	.form-horizontal .control-group{
		margin-bottom:30px;
		*zoom:1;
		display:table;
		line-height:0;
		content:"";
		clear:both;
	}
	/* 라벨 영역 및 폰트 */
	.form-horizontal .control-label{
		float:left;
		width:160px;
		padding-top:12px;
		text-align:right;
		font-weight:bold;
		font-size:14px;
	}
	
	/* 입력창 주변 영역 */
	.form-horizontal .controls{
		*display:inline-block;
		*padding-left:20px;
		margin-left:180px;
		*margin-left:0}

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
	System.out.println(userID);
	
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
<div style="margin-top:40px; margin-bottom:20px;"><!-- 수정페이지 전체 영역 위아래 지정 -->
	<div>
		<h1 style="border-bottom:1px solid #ccc ; padding-bottom:20px; margin-bottom:50px ;" >컴퓨터정보계열</h1>
	</div>
	<h3 style="border-bottom:1px solid #ccc ; margin:0px" >회원정보 조회/수정</h3>

<form method="post" action="./mem_models/member_dao.jsp" class="form-horizontal">
    <input type="hidden" name="mem_num" value=<%=mem_num%>>
    
    <div class="control-group">
    	<label for="email_address" class="control-label"><em style="color:red">*</em>이름</label>
    	<div class="controls">
			<input type="text" class="input-search" name="mem_name" size="30" value=<%=mem_name%> disabled>
		</div>
	</div>
	
	<div class="control-group">
    	<label for="email_address" class="control-label"><em style="color:red">*</em>아이디</label>
    	<div class="controls">
			<input type="text" class="input-search" name="mem_id" size="30" value=<%=mem_id%> disabled>
		</div>
	</div>
	
	<div class="control-group">
    	<label for="email_address" class="control-label"><em style="color:red">*</em>패스워드</label>
    	<div class="controls">
			<input type="password" class="input-search" name="mem_passwd" size="30" value=<%=mem_passwd%>>
		</div>
	</div>
	
	<div class="control-group">
    	<label for="email_address" class="control-label">이메일</label>
    	<div class="controls">
			<input type="text" class="input-search" name="mem_email" size="30" value=<%=mem_email%>>
		</div>
	</div>
	
	<div class="control-group">
   		<label for="email_address" class="control-label">휴대폰</label>
    	<div class="controls">
			<input type="text" class="input-search" name="mem_phone" size="30" value=<%=mem_phone%>>
		</div>
	</div>
	
	<div class="control-group">
    	<label for="email_address" class="control-label"><em style="color:red">*</em>주민번호</label>
    	<div class="controls">
			<input type="text" class="input-search" name="mem_RRN" size="30" value=<%=mem_RRN%> disabled>
		</div>
	</div>
	
	<div style="border-top:1px solid #ccc;padding-top:10px">
		<button type="submit" id="confirm_u" name="actionType" value="mem_U" class="btn-inverse">수정</button>
		<button type="submit" id="confirm_d" name="actionType" value="mem_D" class="btn-inverse">회원탈퇴</button>
	</div>
	
</form>
	<button type="button" onclick="window.location.href='./index.jsp'" class="btn-inverse" style="margin-top:30px;">홈으로 돌아가기</button>
</div>
</div>

<%
	}
%>

<script>
	var check = document.getElementById("confirm_u");
	check.addEventListener("click", function(){ //수정 버튼을 누른경우
		var result = window.confirm("회원정보를 수정하시겠습니까?");
		
		if(result == true){
			//확인 클릭시 member_dao.jsp로 value속성값 "mem_U"가 전송되어 DB의 레코드 수정
			window.alert("회원정보가 수정되었습니다.");
		}else{
			document.getElementById("confirm_u").setAttribute("value","" );
			//취소 클릭시 value속성값이 ""가 되어 수정 X(id값이 confirm_u인 요소의 value속성값을 ""로 정함")
		}
	});
</script>
<script>
	var check = document.getElementById("confirm_d");
	check.addEventListener("click", function(){ //회원탈퇴 버튼을 누른경우
		var result = window.confirm("정말로 탈퇴하시겠습니까?");
		
		if(result == true){
			//확인 클릭시 member_dao.jsp로 value속성값 "mem_D"가 전송되어 DB의 레코드 삭제
			window.alert("회원탈퇴 되었습니다.");
		}else{
			document.getElementById("confirm_d").setAttribute("value","" );
			//취소 클릭시 value속성값이 ""가 되어 삭제 X
		}
	});
</script>
<%@ include file="../footer.jsp" %>
</body>
</html>