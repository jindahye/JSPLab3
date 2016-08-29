package com.sist.dao;

//crud
import java.util.*;
import java.sql.*;

public class BoardDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL = "jdbc:oracle:thin:@211.238.142.87:1521:ORCL";

	// 드라이버 등록

	public BoardDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 오라클해제

	public void disConnection() {
		try {
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 오라클 연결
	public void getConnection() {
		try {
			conn = DriverManager.getConnection(URL, "scott", "tiger");

		} catch (Exception e) {
			e.getMessage();
		}

	}

	// 기능들
	/*
	 * <답변형 게시판 제작에 필요한 변수>
	 * 
	 * group_id(gi) : 게시물과 답변을 구분해 주는 변수 group_step(gs) : 게시물의 답변과 답변에 대한 답변을
	 * 구분하기 위해 사용하는 변수 group_tab(gt) : 위치 변수.
	 * 
	 * 
	 * gi gs gt AAAAAAAAAAAAA 2 0 0 ㄴ댓글KKKKKKK 2 1 1 ㄴ댓글DDDDDDD 2 1 1 ㄴ대댓글OOOO 2
	 * 2 2 ㄴ대댓글JJJJ 2 2 2
	 * 
	 * QQQQQQQQQQQQQQ 1 0 0 ㄴ댓글SSSSSSS 1 1 1
	 * 
	 * 
	 * <게시판의 전체적인 프로그램 구조> 1. 게시물 목록 조회 2. 게시판 입력폼 3. 게시판 게시물 수정 4. 게시물 답변 5.
	 * 게시물 삭제
	 * 
	 * 
	 */

	// 게시판 목록 조회(list.jsp)
	/*
	 * 게시판 목록 조회는 게시판이 실행되는 초기 프로그램에 해당됨 게시글 목록을 조회할 때, 페이지 당 출력 목록을 제한하는 페이지
	 * 네비게이션 사용함 =>변수 지정, 초기값 부여
	 * 
	 * 
	 */

	public List<BoardVO> boardListData(int page) {
		List<BoardVO> list = new ArrayList<>();
		try {
			getConnection();
			String sql = "SELECT no,subject,name,regdate,hit,group_tab FROM replyBoard ORDER BY group_id DESC, group_step ASC";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			int i = 0;
			int j = 0;
			int pagecnt = (page * 10) - 10;
			/*
			 * 13 => 0~9, 10~12
			 * 
			 *
			 * 1page 2page 3page 0 10 20 9 19 19
			 * 
			 */

			while (rs.next()) {
				if (i < 10 && j >= pagecnt) {
					BoardVO vo = new BoardVO();
					vo.setNo(rs.getInt(1));
					vo.setSubject(rs.getString(2));
					vo.setName(rs.getString(3));
					vo.setRegdate(rs.getDate(4));
					vo.setHit(rs.getInt(5));
					vo.setGroup_tab(rs.getInt(6));
					list.add(vo);
					i++;
				}
				j++;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			disConnection();
		}
		return list;
	}

	public int boardTotalPage() {

		int total = 0;
		try {
			getConnection();
			String sql = "SELECT CEIL(COUNT(*)/10) FROM replyBoard";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			total = rs.getInt(1);
			rs.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			disConnection();
		}
		return total;
	}

	public int boardRowCount() {
		int total = 0;
		try {
			getConnection();
			String sql = "SELECT COUNT(*) FROM replyBoard";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			rs.next();
			total = rs.getInt(1);
			rs.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			disConnection();
		}
		return total;

	}

	/*
	 * INSERT INTO replyBoard(no,name,email,subject,content,pwd,group_id) VALUES
	 * (rb_no_sec.nextval,'오헤영','','답변형 게시판','게시판만들기','1234',1);
	 * 
	 */
	public void boardInsert(BoardVO vo) {
		try {
			getConnection();
			String sql = "INSERT INTO replyBoard(no,name,email,subject,content,pwd,group_id)"
					+ " VALUES (rb_no_sec.nextval,?,?,?,?,?,(SELECT NVL(MAX(group_id)+1,1) FROM replyBoard))";
			ps = conn.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setString(2, vo.getEmail());
			ps.setString(3, vo.getSubject());
			ps.setString(4, vo.getContent());
			ps.setString(5, vo.getPwd());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			disConnection();
		}
	}

	// 내용보기

	public BoardVO boardContent(int no, int type) {
		BoardVO vo = new BoardVO();

		try {
			//조회수 증가
			getConnection();
			if(type==1){	//내용보기 타입으로 지정
				String sql="UPDATE replyboard SET hit=hit+1 WHERE no=?";				
				ps= conn.prepareStatement(sql);
				ps.setInt(1, no);
				ps.executeUpdate(); //오토커밋가능
				ps.close();
				
			}
			
			//read
			String sql="SELECT no,name,NVL(email,' '),subject,content,regdate,hit FROM replyboard WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);;
			
			ResultSet rs=ps.executeQuery();
			rs.next();
			
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setEmail(rs.getString(3));
			vo.setSubject(rs.getString(4));
			vo.setContent(rs.getString(5));
			vo.setRegdate(rs.getDate(6));
			vo.setHit(rs.getInt(7));
			rs.close();
			
			
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}finally {
			disConnection();
		}

		return vo;
	}

}
