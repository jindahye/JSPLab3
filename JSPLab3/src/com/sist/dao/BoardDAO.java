package com.sist.dao;

//crud
import java.util.*;
import java.sql.*;

public class BoardDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL = "jdbc:oracle:thin:@211.238.142.87:1521:ORCL";

	// ����̹� ���

	public BoardDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// ����Ŭ����

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

	// ����Ŭ ����
	public void getConnection() {
		try {
			conn = DriverManager.getConnection(URL, "scott", "tiger");

		} catch (Exception e) {
			e.getMessage();
		}

	}

	// ��ɵ�
	/*
	 * <�亯�� �Խ��� ���ۿ� �ʿ��� ����>
	 * 
	 * group_id(gi) : �Խù��� �亯�� ������ �ִ� ���� group_step(gs) : �Խù��� �亯�� �亯�� ���� �亯��
	 * �����ϱ� ���� ����ϴ� ���� group_tab(gt) : ��ġ ����.
	 * 
	 * 
	 * gi gs gt AAAAAAAAAAAAA 2 0 0 �����KKKKKKK 2 1 1 �����DDDDDDD 2 1 1 ������OOOO 2
	 * 2 2 ������JJJJ 2 2 2
	 * 
	 * QQQQQQQQQQQQQQ 1 0 0 �����SSSSSSS 1 1 1
	 * 
	 * 
	 * <�Խ����� ��ü���� ���α׷� ����> 1. �Խù� ��� ��ȸ 2. �Խ��� �Է��� 3. �Խ��� �Խù� ���� 4. �Խù� �亯 5.
	 * �Խù� ����
	 * 
	 * 
	 */

	// �Խ��� ��� ��ȸ(list.jsp)
	/*
	 * �Խ��� ��� ��ȸ�� �Խ����� ����Ǵ� �ʱ� ���α׷��� �ش�� �Խñ� ����� ��ȸ�� ��, ������ �� ��� ����� �����ϴ� ������
	 * �׺���̼� ����� =>���� ����, �ʱⰪ �ο�
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
	 * (rb_no_sec.nextval,'���쿵','','�亯�� �Խ���','�Խ��Ǹ����','1234',1);
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

	// ���뺸��

	public BoardVO boardContent(int no, int type) {
		BoardVO vo = new BoardVO();

		try {
			//��ȸ�� ����
			getConnection();
			if(type==1){	//���뺸�� Ÿ������ ����
				String sql="UPDATE replyboard SET hit=hit+1 WHERE no=?";				
				ps= conn.prepareStatement(sql);
				ps.setInt(1, no);
				ps.executeUpdate(); //����Ŀ�԰���
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
