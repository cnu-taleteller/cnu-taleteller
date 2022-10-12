<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap" rel="stylesheet">
<title>관리자 페이지 - 회원 관리</title>
	<style>
		*{
		font-family: 'Noto Sans KR', sans-serif;
		}
		.memberContents{
		display:flex;
		justify-content: center;
		}
		
		.memberMenuLeft{
		display:flex;
		flex-direction: column;
		width:16vw;
		margin-right: 5vh;
		}
		
		.menuStart {
		height:19vh;
		background-color: #314DA0;
		text-weight: bold;
		color: white;
		display:flex;
		flex-direction: column;
		align-items : center;
		justify-content : center;
		}
		
		.menuName {
		height: 10vh;
		font-size: 17px;
		border: 1px solid #EBEBEB;
		border-top: 0;
		background-color: white;
		}

		.memberAdjustRight {
		width: 60%;
		font-size: 14px;
		margin-bottom: 10vh;
		}
		
		.memberAdjustRight> h1{
		text-weight: bold;
		margin : 2vw;
		}
		
		.lookType {
		float: right;
		margin-bottom: 2vh;
		}
		
		.paging{
		margin-top: 5vh;
		text-align: center;
		}
		
		table{
		border-left: none;
		border-right: none; 
		}
		
		select {
		padding: .1em .5em; 
		border: 1px solid #CCCCCC;
		box-sizing: border-box;
		}
		select::-ms-expand {
		display: none;
		}

		.selectBtn{
		border: 1px solid #CCCCCC;
		background-color: white;
		}
		
		.pageBtn{
		border: 0;
		background-color: white;
		font-size: 12px;
		color: #999999;
		}
			
		.selectNum {
		font-weight: bold;
		background-color: #F9F9F9;
		border: 1px solid #D2D2D2;
		color: black;
		}		
	</style>

</head>
<body>
	<%@ include file="./header.jsp"%>
	
	<div class="memberContents">
	
	<div class="memberMenuLeft">
		<div class="menuStart">
		<h2>관리자페이지</h2>
		<p style="color: #657AB8;">YEUNGJIN UNIVERSITY</p>
		</div>
		<button style="font-weight: bold; background-color: #EAEDF5;"type="button" class="menuName" onclick="location.href='/member_1조/manage_page.jsp?currentPageNo=0&pageRange=10&searchClass=0'">회원관리</button>
		<button type="button" class="menuName" onclick = "alert('준비 중인 기능입니다.')" />게시물관리</button>
	</div>
	
	<div class="memberAdjustRight">
	<h1>회원관리</h1>
	<hr>
	<div class="lookType">
	<form method = "post" action = "./manage_page.jsp" id = "combo">
			<select name="pageRange" >
				<option value="10" selected>10개씩 보기</option>
				<option value="20">20개씩 보기</option>
				<option value="50">50개씩 보기</option>
				<option value="100">100개씩 보기</option>
			</select>
			<select name="searchClass">
				<option value="0" selected>전체</option>
				<option value="100">학생</option>
				<option value="50">교수</option>
				<option value="30">관리자</option>
			</select> 
			<input class="selectBtn" type="hidden" name="currentPageNo" value="0">
			<input class="selectBtn" type="submit" value="확인">
	</form>
	</div>
	
	<table border="1" style="text-align: center" class="table">
		<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col">이름</th>
				<th scope="col">아이디</th>
				<th scope="col">이메일</th>
				<th scope="col">휴대폰번호</th>
				<th scope="col">주민등록번호</th>
				<th scope="col">등급</th>
				<th scope="col">관리</th>
			</tr>
		</thead>
		<tbody>
			
	<%
	String driverName = "org.mariadb.jdbc.Driver";
	String url = "jdbc:mariadb://localhost/member_db";
	String user = "root";
	String passwd = "root";

	Class.forName(driverName);
	Connection con = DriverManager.getConnection(url, user, passwd);
	request.setCharacterEncoding("utf-8");
	Statement stmt = con.createStatement();
	PreparedStatement pstmt;
	String sql2 = "select * from member";
	ResultSet rs2 = stmt.executeQuery(sql2);

	int mem_num;
	String mem_name;
	String mem_id;
	String mem_email;
	String mem_phone;
	String mem_RRN;
	String mem_class;
	String mem_class_name = "";
	
	session.setAttribute("searchClass",request.getParameter("searchClass"));
	String searchClass=(String)session.getAttribute("searchClass");

	session.setAttribute("pageRange",request.getParameter("pageRange"));
	String pageRange_String=(String)session.getAttribute("pageRange");
	
	int pageRange=Integer.parseInt(pageRange_String);	
	int recordCnt = 0;
	while (rs2.next()) {
		recordCnt++;
	}

	int pageCnt = recordCnt / pageRange;
	if (recordCnt % pageRange != 0)
		pageCnt++;

	int startRecord = 0;
	int limitCnt = pageRange;
	int currentPageNo = Integer.parseInt(request.getParameter("currentPageNo"));

	startRecord = currentPageNo * pageRange;

	int pageN = 0;
	if (currentPageNo >= 10) {
		pageN = currentPageNo / 10 * 10;
	}
	
	
	if(searchClass.equals("0")) { 
		String sql = "select * from member order by mem_num limit ?, ? ";
		pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, startRecord);
		pstmt.setInt(2, limitCnt);
	}
	else {
		String sql = "select * from member where mem_class = ? order by mem_num limit ?, ? ";
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, searchClass);
		pstmt.setInt(2, startRecord);
		pstmt.setInt(3, limitCnt);
	}
		
		ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				mem_num = rs.getInt("mem_num");
				mem_name = rs.getString("mem_name");
				mem_id = rs.getString("mem_id");
				mem_email = rs.getString("mem_email");
				mem_phone = rs.getString("mem_phone");
				mem_RRN = rs.getString("mem_RRN");
				mem_class = rs.getString("mem_class");

				if (mem_class.equals("30"))
					mem_class_name = "관리자";
				else if (mem_class.equals("50"))
					mem_class_name = "교수";
				else if (mem_class.equals("100"))
					mem_class_name = "학생";
			%>
			<tr>
				<td ><%=mem_num%></td>
				<td><%=mem_name%></td>
				<td><%=mem_id%></td>
				<td><%=mem_email%></td>
				<td><%=mem_phone%></td>
				<td><%=mem_RRN%></td>
				<td><%=mem_class_name%></td>
				<td>

					<form method="post" action="/member_1조/mem_models/member_dao.jsp">
						<%
						if (!mem_class_name.equals("관리자")) {
						%>
						<select name="mem_class">
							<option value=100>학생</option>
							<option value=50>교수</option>
							<option value=30>관리자</option>
						</select> <input type="hidden" name="mem_num" value="<%=mem_num%>">
						<input type="hidden" name="actionType" value="MANAGE_ADJUST">
						<input class="selectBtn" type="submit" value="수정">
						<input class="selectBtn" type="button" value="삭제" onclick="manageDelete('<%=mem_num%>')">
						<%
						}
						%>
					</form>
				</td>
		
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
	<div class="paging">
	<input type="button" class="pageBtn" value="<First" onClick="gotoPage(0,<%=pageRange%>,<%=searchClass%>,<%=pageCnt%>)"/>
	<input type="button" class="pageBtn" value="Prev" onClick="gotoPage(<%=(currentPageNo -1)%>,<%=pageRange%>,<%=searchClass%>,<%=pageCnt%>)"/>
	<%

	for (int i = pageN; i < pageN + 10; i++) {

		if (i == currentPageNo) {%> 
			<input type="button" class="pageBtn selectNum" value="<%=i+1%>" onClick="gotoPage(<%=i%>,<%=pageRange%>,<%=searchClass%>,<%=pageCnt%>)"/><%;
		} else if (i < pageCnt) {%>
			<input type="button" class="pageBtn" value="<%=i+1%>" onClick="gotoPage(<%=i%>,<%=pageRange%>,<%=searchClass%>,<%=pageCnt%>)"/><%;
		} 
	}%>
	
	<input type="button" class="pageBtn" value="Next" onClick="gotoPage(<%=(currentPageNo +1)%>,<%=pageRange%>,<%=searchClass%>,<%=pageCnt%>)"/>
	<input type="button" class="pageBtn" value="End>" onClick="gotoPage(<%=(pageCnt - 1)%>,<%=pageRange%>,<%=searchClass%>,<%=pageCnt%>)"/>
	</div>
	
	</div>
	</div>
<script>
		function manageDelete(userNum){
			
		var result = confirm("탈퇴 하시겠습니까?");
		if(result){
			location.href="/member_1조/mem_models/member_dao.jsp?mem_num="+userNum+"&actionType=DELETE";
		}
		}
		
		function gotoPage(currentPageNo, pageRange, searchClass, pageCnt){
			console.log(currentPageNo + "-" + pageCnt)
			if (parseInt(currentPageNo) >= 0 && parseInt(currentPageNo) < parseInt(pageCnt)){
				location.href = "./manage_page.jsp?currentPageNo="+currentPageNo+"&pageRange="+pageRange+"&searchClass="+searchClass;
			}
			else{
				alert("[!]페이지의 시작이나 끝입니다");
			}
		}
	
	
</script>
	<%@ include file="./footer.jsp" %>
</body>
</html>