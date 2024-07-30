package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;

//DAO (Data Access Object) : DB에 직접 접근해서 사용자의 요청에 맞는 sql문 실행 후 결과 반환(=> JDBC 사용)
public class MemberDao {
	private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private final String USER_NAME = "C##JDBC";
	private final String PASSWORD = "JDBC";
	/*
	 * * JDBC용 객체
	 * 	- Connection : DB 연결정보를 담고있는 객체
	 *  - Statement : 연결된 DB에 sql문을 전달해서 실행하고 결과를 받아주는 객체
	 *  - ResultSet : SELECT문(DQL) 실행 후 조회된 결과물을 담고있는 객체
	 * 
	 * * JDBC 과정 (순서*)
	 *   [1] jdbc driver 등록 : 사용할 DBMS(오라클)에서 제공하는 클래스 등록
	 *   [2] Connection 객체 생성 : (url, 사용자명, 비밀번호)를 통해 해당 DB와 연결하면서 생성됨(연결 정보를 담고 있음)
	 *   [3] Statement 객체 생성 : Connection 객체를 이용해서 생성 sql문을 실행하고 결과를 받아줌
	 *   [4] sql문 전달해서 실행 후 결과 받기
	 *   	 - SELECT문 실행 시 ResultSet 객체로 조회 결과를 받음
	 *   	 - DML(INSERT/UPDATE/DELETE) 실행 시 int 타입으로 처리 결과를 받음 (처리된 행 수)
	 *   [5] 결과에 대한 처리
	 *   	 - ResultSet 객체에서 데이터를 하나씩 추출하여 vo객체로 옮겨 담기(저장)
	 *   	 - DML의 경우 트랜잭션 처리 (성공했을 때는 commit, 실패했을 때는 rollback )
	 *    [6] 자원 반납 (close) => 생성 역순으로!
	 */
	
	/**
	 * 사용자가 입력한 정보들을 DB에 추가하는 메소드 (=> 회원 정보 추가)
	 * 
	 * @param m 사용자가 입력한 값들이 담겨있는 Member 객체
	 * @return Insert문 실행 후 처리된 행 수
	 */
	public int insertMember(Member m) {
		// insert문 --> int (처리된 행 수) --> 트랜잭션 처리
		int result = 0;
		
		String sql = "INSERT INTO MEMBER VALUES (SEQ_USERNO.NEXTVAL, "
						+ "'" + m.getUserId() + "', "
						+ "'" + m.getUserPw() + "', "
						+ "'" + m.getUserName() + "', "
						+ "'" + m.getGender() + "', "
							  + m.getAge() + ", "
						+ "'" + m.getEmail() + "', "
						+ "'" + m.getAddress() + "', "
						+ "'" + m.getPhone() + "', "
						+ "'" + m.getHobby() + "', SYSDATE)";
		
		System.out.println("-----------------------------------");
		System.out.println(sql);
		System.out.println("-----------------------------------");
		
		// JDBC용 객체 선언
		Connection conn = null;
		Statement stmt = null;
		
		try {
			// 1) jdbc driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성 => DB 연결
			conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			conn.setAutoCommit(false);
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4) 실행 후 결과 받기
			result = stmt.executeUpdate(sql);
			
			// 5) 트랜잭션 처리
			if(result > 0) {
				// DAO의 결과 : 성공했을 경우 성공화면
				conn.commit();
				new MemberMenu().displaySuccess("회원 추가 성공!");
			} else {
				// DAO의 결과 : 실패했을 경우 실패화면 표시
				conn.rollback();
				new MemberMenu().displaySuccess("회원 추가 실패!");
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
						
		
		
		
		return result;
	}
	
	public ArrayList<Member> selectList() {
		// SELECT문 (여러행 조회) --> ResultSet 객체 --> ArrayList<Member>에 담기
		ArrayList<Member> list = new ArrayList<>();;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM MEMBER";
		
		try {
			// 1단계
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2단계
			conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			
			// 3단계
			stmt = conn.createStatement();
			
			// 4단계
			rset = stmt.executeQuery(sql);
			
			// 5단계
			while(rset.next()) { // next() : 데이터가 있을 경우 true
				Member m = new Member(
					rset.getInt("USERNO"),
					rset.getString("USERID"),
					rset.getString("USERPW"),
					rset.getString("USERNAME"),
					rset.getString("GENDER").charAt(0),
					rset.getInt("AGE"),
					rset.getString("EMAIL"),
					rset.getString("ADDRESS"),
					rset.getString("PHONE"),
					rset.getString("HOBBY"),
					rset.getDate("ENROLLDATE")
				);
				list.add(m);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	};
	
	
	
	public Member searchID(String ID) {
		// SELECT문 --> ResultSet (한 행 | X) --> Member 객체에 저장
		Member m = null;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM MEMBER WHERE USERID =" + "'" + ID + "'";
		
		System.out.println("---------------------------------");
		System.out.println(sql);
		System.out.println("---------------------------------");
		
		
		try {
			// 1단계
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2단계
			conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			
			// 3단계
			stmt = conn.createStatement();
			
			// 4단계
			rset = stmt.executeQuery(sql);
			
			// 5단계
			if(rset.next()) { // next() : 데이터가 있을 경우 true
				m = new Member(
								rset.getInt("USERNO"),
								rset.getString("USERID"),
								rset.getString("USERPW"),
								rset.getString("USERNAME"),
								rset.getString("GENDER") == null ? ' ' : rset.getString("GENDER").charAt(0),
								rset.getInt("AGE"),
								rset.getString("EMAIL"),
								rset.getString("ADDRESS"),
								rset.getString("PHONE"),
								rset.getString("HOBBY"),
								rset.getDate("ENROLLDATE")
							);
			} 
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return m;
	};
	
	public int deleteMember(String id) {
		Connection conn = null;
		Statement stmt = null;
		int result = 0;
		
		String sql = "DELETE FROM MEMBER WHERE USERID =" + "'" + id + "'";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "C##JDBC", "JDBC");
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			
			result = stmt.executeUpdate(sql);
			
			if(result >0 ) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
			
		} catch (ClassNotFoundException e) {
			
		} catch (SQLException e) {
			
		} finally {
			
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		}
		
		return result;
		
	}

}
