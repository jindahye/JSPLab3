<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR" import="java.util.*,com.sist.dao.*,java.text.*"%>
<%
	//content.jsp?no=10

	String strNo = request.getParameter("no");
	int no = Integer.parseInt(strNo);

	BoardDAO dao = new BoardDAO();
	BoardVO vo = dao.boardContent(no, 1);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<style>
th, td {
	font-family: 맑은 고딕;
	font-size: 9pt;
	
	}
	a{
	text-decoration: none;
	color:black;
	}
	a:HOVER{
	text-decoration: none;
	color:red;
	}
	
	
</style>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<script type="text/javascript">
	var i=0;
$(function () {
	$('#delBtn').click(function(){ //토글
		if(i==0){
			$('#del').show();
			i=1;
		}else{
			$('#del').hide();
			i=0;
		}
	}) 
	
	$('#delButton').click(function(){
		var pwd=$('#pwd').val();
		if(pwd==""){
			$('#print').html("<font color=red> 비밀번호를 입력하세요.</font>");	
			$('#pwd').focus();
			
		}
		$('#delFrm').submit();
		
		
	});
	
});




</script>

<body>
	<center>
		<img src="image/content.jpg" height="80" width="500"> <br /> <br />
		<table border="0" width="500">
			<tr>
				<td align="center" width=20%
					style="background-color: #999; color: #fff">번호</td>
				<td align="center" width=30%><%=vo.getNo()%></td>
				<td align="center" width=20%
					style="background-color: #999; color: #fff">작성일</td>

				<td align="center" width=30%><%=vo.getRegdate().toString()%></td>
			</tr>

			<tr>
				<td align="center" width=20%
					style="background-color: #999; color: #fff">이름</td>
				<td align="center" width=30%><%=vo.getName()%></td>
				<td align="center" width=20%
					style="background-color: #999; color: #fff">조회수</td>

				<td align="center" width=30%><%=vo.getHit()%></td>
			</tr>
			<tr>
				<td align="center" width=20%
					style="background-color: #999; color: #fff">제목</td>
				<td colspan="3" align="left" width=30%><%=vo.getSubject()%></td>
			<tr>

				<td colspan="4" align="left" valign="top" width="500" height="200">
					<pre><%=vo.getContent()%>
					
					</pre>

				</td>
			</tr>
		</table>

		<table border="0" width="500" id="">
<tr>
			<td align="right"><a href="reply.jsp?no=<%=vo.getNo()%>"><img
					src="image/reply.gif"></a> <a
				href="update.jsp?no=<%=vo.getNo()%>"><img alt=""
					src="image/modify.gif"></a> <img src="image/delete.gif"
				id="delBtn"> <a href="list.jsp"><img src="image/list.gif"></a>

			</td></tr>
		<tr id="del" style="display:none">
			<td align="right">
				<span id="print"></span>
				<form method="post" action="delete.jsp" id="delFrm">
					비밀번호:<input type="password" name="pwd" size="10" id="pwd">
					<input type="hidden" name="no" value="<%=vo.getNo() %>">
					<input type="button" value="삭제" id="delButton">
					
				</form>
			</td>
		</tr>
	
		</table>
	</center>
</body>
</html>