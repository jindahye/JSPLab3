<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" import="com.sist.dao.*"%>
    
    
    <%
    
    request.setCharacterEncoding("EUC-KR");
    String name=request.getParameter("name");
    String email=request.getParameter("email"); 
    String subject=request.getParameter("subject");
    String content=request.getParameter("content");
    String pwd=request.getParameter("pwd");
    
    BoardVO vo = new BoardVO();
    vo.setName(name);
    vo.setEmail(email);
    vo.setSubject(subject);
    vo.setContent(content);
    vo.setPwd(pwd);
    
    BoardDAO dao = new BoardDAO();
    dao.boardInsert(vo);
    
    //목록으로 이동
     response.sendRedirect("list.jsp");
    
    
    %>
