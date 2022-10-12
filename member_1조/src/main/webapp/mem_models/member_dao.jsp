<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.swing.JOptionPane"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script>

	function gotoIndex(){
		var result = confirm("[!]회원 정보가 맞지 않습니다");
		if(result){
			location.href="/member_1조/index.jsp";
		}
	}
</script>

</head>
<body>

<%
   String driverName = "org.mariadb.jdbc.Driver";
   String url = "jdbc:mariadb://localhost/member_db";
   String user = "root";
   String passwd = "root";
   
   Class.forName(driverName);
   Connection con = DriverManager.getConnection(url, user, passwd);
   Statement stmt = con.createStatement();
   request.setCharacterEncoding("utf-8");   
   PreparedStatement pstmt = null;
   ResultSet rs = null;
   String sql;
   int result;
   
    String actionType = request.getParameter("actionType");

   int mem_num;
   String mem_name;
   String mem_id="";
   String mem_passwd="";
   String mem_email;
   String mem_phone;
   String mem_RRN1;
   String mem_RRN2;
   String mem_class;
   
   String userID;
   String userPW;

   int loginState;

   switch(actionType){
   case "LOGIN":
       userID = request.getParameter("userid");
        userPW = request.getParameter("passwd");
        
        pstmt = con.prepareStatement("select mem_id, mem_passwd, mem_class from member where mem_id = ?");
       pstmt.setString(1, userID);
       rs = pstmt.executeQuery();
       System.out.print(userID + " " + userPW);
       if(rs.next()){
          String userID_DUMMY = rs.getString("mem_id");
          String userPW_DUMMY = rs.getString("mem_passwd");
          String userclass = rs.getString("mem_class");
          
            if(userID.equals(userID_DUMMY)){
                 if(userPW.equals(userPW_DUMMY)){
                    session.setAttribute("loginState", "login");
                    session.setAttribute("userid", userID);
                    session.setAttribute("userpw", userPW);
                    session.setAttribute("userclass", userclass);
                    response.sendRedirect("../index.jsp");
                 }
              } 
       }
      
       %>
       <script>
			var result = confirm("[!]회원 정보가 맞지 않습니다");
			if(result){
				location.href="/member_1조/login_page.jsp";
			}
		</script>
       
       <%

        break;
          
      case "LOGOUT":
         session.setAttribute("userid", null);
         session.setAttribute("userpw", null);
         session.setAttribute("loginState", "logout");
         response.sendRedirect("../index.jsp");
         break;
      
      case "SIGNUP":
         mem_name=request.getParameter("mem_name");
         mem_id=request.getParameter("mem_id");
         mem_passwd=request.getParameter("mem_passwd");
         mem_email=request.getParameter("mem_email");
         mem_phone=request.getParameter("mem_phone");
         mem_RRN1=request.getParameter("mem_RRN1");
         mem_RRN2=request.getParameter("mem_RRN2");
         
         sql="insert into member (mem_name, mem_id, mem_passwd, mem_email, mem_phone, mem_RRN)";
         sql+="values (?, ?, ?, ?, ?, ?)";
         pstmt = con.prepareStatement(sql);
         pstmt.setString(1, mem_name);
         pstmt.setString(2, mem_id);
         pstmt.setString(3, mem_passwd);
         pstmt.setString(4, mem_email);
         pstmt.setString(5, mem_phone);
         pstmt.setString(6, mem_RRN1 + "-" + mem_RRN2);
         rs = pstmt.executeQuery();
         
         //session.setAttribute("loginState", "logout");
         response.sendRedirect("../index.jsp");
         break;
         
      case "MANAGE_ADJUST":
         mem_class=request.getParameter("mem_class");
         mem_num=Integer.parseInt(request.getParameter("mem_num"));
         
         sql="update member set mem_class = ? where mem_num = ?";
         pstmt = con.prepareStatement(sql);
         pstmt.setString(1, mem_class);
         pstmt.setInt(2, mem_num);
         rs = pstmt.executeQuery();
         
         session.setAttribute("loginState", "login");
%>
         <script type="text/javascript">
            alert("변경 완료되었습니다.");
            location.href="/member_1조/manage_page.jsp?currentPageNo=0&pageRange=10&searchClass=0";
         </script>
<%      
         break;
   
      case "DELETE" :
         mem_num = Integer.parseInt(request.getParameter("mem_num"));
         sql = "DELETE FROM member WHERE mem_num = ";
         sql += mem_num;
         
         result = stmt.executeUpdate(sql);

         if (result == 1) {
            System.out.println("삭제성공");
         } else {
            System.out.println("삭제실패");
         }
%>
         <script>
            alert("탈퇴 완료되었습니다.");
            location.href="/member_1조/manage_page.jsp?currentPageNo=0&pageRange=10&searchClass=0";
         </script>
<%         break;
         
      case "mem_check_pw":
    	  session.setAttribute("passwdCheck", null);
			
			String userPW_check = request.getParameter("passwdCK"); //입력한 비밀번호 가져오기
			
			sql = "SELECT * FROM member WHERE mem_num = " + Integer.parseInt(request.getParameter("mem_num"));
			ResultSet rs2 = stmt.executeQuery(sql);
			
			System.out.println(sql);
			
			while(rs2.next()){
				mem_passwd = rs2.getString("mem_passwd");
			}
			if(userPW_check.equals(mem_passwd) && mem_passwd!=""){
				System.out.println("일치");
				session.setAttribute("passwdCheck", "true");
				response.sendRedirect("../member_U&D.jsp"); //패스워드 일치 시 수정페이지로 리다이렉트
				
			}else{
				System.out.println("불일치");
				session.setAttribute("passwdCheck", "false");
				response.sendRedirect("../member_U&D_pwCheck.jsp"); //불일치 시 확인창으로 리다이렉트(+경고창)
			}
			break;
			
		//테이블 레코드 수정(회원정보 수정)
		case "mem_U":
			mem_id = request.getParameter("mem_id");
			mem_passwd = request.getParameter("mem_passwd");
			
			sql = "UPDATE member ";
			sql += "SET mem_passwd = '" + request.getParameter("mem_passwd") + "' ";
			sql += ", mem_email = '" + request.getParameter("mem_email") + "' ";
			sql += ", mem_phone = '" + request.getParameter("mem_phone") + "' ";
			sql += "WHERE mem_num = " + Integer.parseInt(request.getParameter("mem_num"));
			
			System.out.println(sql);
			result = stmt.executeUpdate(sql);//SQL문 실행
				
			if(result == 1){
				System.out.println("회원정보 수정 성공");
				response.sendRedirect("../index.jsp");
			}
			else{
				System.out.println("회원정보 수정 실패");
				response.sendRedirect("../member_U&D.jsp");	
			}
			break;
		
		
		//테이블 레코드 삭제(회원탈퇴)
		case "mem_D":
			sql = "DELETE FROM member ";
			sql += "WHERE mem_num = " + Integer.parseInt(request.getParameter("mem_num"));
			
			System.out.println(sql);
			
			result = stmt.executeUpdate(sql);//SQL문 실행

			if(result == 1){
				System.out.println("회원 탈퇴 성공");
				session.setAttribute("userid", null);
				session.setAttribute("userpw", null);
				session.setAttribute("loginState", "logout");
				session.invalidate();//설정된 세션의 값 삭제
			}
			else {
				System.out.println("회원 탈퇴 실패");
			}
			response.sendRedirect("../index.jsp");
			break;
			
		default:
			response.sendRedirect("../index.jsp");
			break;
	}
%>

<%-- <jsp:forward page="../index.jsp"/> --%>

</body>
</html>