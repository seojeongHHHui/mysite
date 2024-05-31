package com.poscodx.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.poscodx.mysite.vo.BoardVo;

public class BoardDao {
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.0.196:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
		
		return conn;
	}
	
	public List<BoardVo> findAll() {
		List<BoardVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select a.no, a.title, a.contents, a.hit, a.reg_date, a.g_no, a.o_no, a.depth, a.user_no, b.name"
															+ " from board a, user b"
															+ " where a.user_no=b.no"
															+ " order by g_no desc, o_no asc");
			ResultSet rs = pstmt.executeQuery();
		) {
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				int hit = rs.getInt(4);
				String regDate = rs.getString(5);
				int groupNo = rs.getInt(6);
				int orderNo = rs.getInt(7);
				int depth = rs.getInt(8);
				Long userNo = rs.getLong(9);
				String userName = rs.getString(10);
				
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setUserNo(userNo);
				vo.setUserName(userName);
				
				result.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
	
	public List<BoardVo> findAllWithPaging(int page, int viewCountPerPage) {
		List<BoardVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select a.no, a.title, a.contents, a.hit, a.reg_date, a.g_no, a.o_no, a.depth, a.user_no, b.name"
															+ " from board a, user b"
															+ " where a.user_no=b.no"
															+ " order by g_no desc, o_no asc"
															+ " limit ?, ?");
		) {
			pstmt.setInt(1, (page-1)*5);
			pstmt.setInt(2, viewCountPerPage);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				int hit = rs.getInt(4);
				String regDate = rs.getString(5);
				int groupNo = rs.getInt(6);
				int orderNo = rs.getInt(7);
				int depth = rs.getInt(8);
				Long userNo = rs.getLong(9);
				String userName = rs.getString(10);
				
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setUserNo(userNo);
				vo.setUserName(userName);
				
				result.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}

	public int getViewCount() {
		int count = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select count(*) from board");
				ResultSet rs = pstmt.executeQuery();
				
			) {
				rs.next();
				count = rs.getInt(1);
		
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		
		return count;
	}
	
	public BoardVo findView(Long no) {
		BoardVo vo = new BoardVo();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select title, contents, user_no from board where no=?");
			
		) {
			pstmt.setLong(1, no);
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			String title = rs.getString(1);
			String contents = rs.getString(2);
			Long userNo = rs.getLong(3);
			
			vo.setNo(no);
			vo.setTitle(title);
			vo.setUserNo(userNo);
			vo.setContents(contents);
				
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return vo;
	}
	
	public int insertNew(BoardVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into board values(null,?,?,?,?, ifnull((select max(g_no) from board a)+1,1), ?,?,?)");
			
			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			pstmt1.setString(1, vo.getTitle());
			pstmt1.setString(2, vo.getContents());
			pstmt1.setInt(3, vo.getHit());
			pstmt1.setString(4, vo.getRegDate());
			/* groupNo의 최댓값은 서브쿼리에서 구한 후 바로 넣기! */
			//pstmt1.setInt(5, vo.getGroupNo());
			pstmt1.setInt(5, vo.getOrderNo());
			pstmt1.setInt(6, vo.getDepth());
			pstmt1.setLong(7, vo.getUserNo());
			result = pstmt1.executeUpdate();
			
			ResultSet rs = pstmt2.executeQuery();
			vo.setNo(rs.next() ?  rs.getLong(1) : null);
			rs.close();			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
	
	public int insertReply(BoardVo vo, Long parentNo) {
		int result1 = 0;
		int result2 = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("update board set o_no = o_no+1 where g_no=(select g_no from board a where no=?) and o_no>=(select o_no+1 from board b where no=?)");
			PreparedStatement pstmt2 = conn.prepareStatement("insert into board select null,?,?,?,?, g_no, (o_no+1), (depth+1), ? from board a where no=?");
			PreparedStatement pstmt3 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			pstmt1.setLong(1, parentNo);
			pstmt1.setLong(2, parentNo);
			result1 = pstmt1.executeUpdate();
			
			pstmt2.setString(1, vo.getTitle());
			pstmt2.setString(2, vo.getContents());
			pstmt2.setInt(3, vo.getHit());
			pstmt2.setString(4, vo.getRegDate());
			pstmt2.setLong(5, vo.getUserNo());
			pstmt2.setLong(6, parentNo);
			result2 = pstmt2.executeUpdate();
			
			ResultSet rs = pstmt3.executeQuery();
			vo.setNo(rs.next() ?  rs.getLong(1) : null);
			rs.close();			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result1;
	}
	
	public int deleteByNo(Long no) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from board where no = ?");
		) {
			pstmt.setLong(1, no);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;	
	}

	public int updateTitleAndContentsByNo(Long no, String title, String content) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("update board set title=?, contents=? where no=?");
		) {
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setLong(3, no);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
				
		return result;
	}
	
	public int hitCountUp(Long no) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("update board set hit=hit+1 where no=?");
		) {
			pstmt.setLong(1, no);
			result = pstmt.executeUpdate();
				
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
					
		return result;
	}

}
