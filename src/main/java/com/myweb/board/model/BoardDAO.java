package com.myweb.board.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.myweb.user.model.UserDAO;
import com.myweb.util.JdbcUtil;

public class BoardDAO {
	
	private static BoardDAO instance = new BoardDAO();
	//2. 직접 객체를 생성할 수 없도록 생성자에도 private를 붙임
	private BoardDAO() {
		
		//커넥션 풀에 사용할 객체를 얻어옴
		try {
			InitialContext init = new InitialContext();
			 ds = (DataSource)init.lookup("java:comp/env/jdbc/oracle");
			

		} catch (Exception e) {
			System.out.println("커넥션 풀 에러");
		}
		
		
	}
	//3.객체 생성을 요구할 때 getter 메서드를 사용해서 1번의 객체를 반환
	public static BoardDAO getInstance() {
		return instance;
	}
	///////////////////////////////
	//커넥션 풀 계정정보
	private DataSource ds;
	
	//글 등록
	public void regist(String writer, String title , String content) {
		
		String sql = "INSERT INTO BOARD(BNO, WRITER, TITLE,CONTENT) "
				+ "VALUES(BOARD_SEQ.NEXTVAL, ?,  ? ,? )";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, writer);
			pstmt.setString(2, title);
			pstmt.setString(3, content);
			
			pstmt.executeUpdate(); //
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, pstmt, null);
		}
		
	}
	
	
	public ArrayList<BoardDTO> getList() {
		
		ArrayList<BoardDTO> list = new ArrayList<>();
		
		String sql = "SELECT * FROM BOARD ORDER BY BNO DESC";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn=ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery(); //select는 query
			
			while(rs.next()) {
				//1행에 대한 처리
				//컬럼을 DTO에 담고 , DTO를 list에 추가
				
				int bno =rs.getInt("bno");
				String writer = rs.getString("writer");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Timestamp regdate =rs.getTimestamp("regdate");
				int hit = rs.getInt("hit");
				
				//dto 생성
				BoardDTO dto = new BoardDTO(bno, writer, title, content, regdate, hit);
				
				//list 추가
				list.add(dto);
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, pstmt, rs);
		}
		
		
		return list;
	}

}
