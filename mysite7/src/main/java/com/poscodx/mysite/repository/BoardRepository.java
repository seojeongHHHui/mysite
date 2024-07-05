package com.poscodx.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscodx.mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	
	@Autowired
    private SqlSession sqlSession;
	
	public List<BoardVo> findAll() {
		return sqlSession.selectList("board.findAll");
	}
	
	public List<BoardVo> findAllWithPaging(int page, int viewCountPerPage) {
		return sqlSession.selectList("board.findAllWithPaging",
									Map.of("startIndex", (page-1)*5, "viewCountPerPage", viewCountPerPage));
	}

	public int getViewCount() {
		return sqlSession.selectOne("board.getViewCount");
	}
	
	public BoardVo findByNo(Long no) {
		return sqlSession.selectOne("board.findByNo", no);
	}
	
	public int insert(BoardVo vo, Long parentNo) {
		if(parentNo == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("vo", vo);
			map.put("parentNo", null);
			return sqlSession.insert("board.insert", map);
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("vo", vo);
			map.put("parentNo", parentNo);
			return sqlSession.insert("board.insert", map);
		}
	}
//	public int insertNew(BoardVo vo) {
//		return sqlSession.insert("board.insert", Map.of("vo", vo, "parentNo", null));
//	}
//	public int insertReply(BoardVo vo, Long parentNo) {
//		return sqlSession.insert("board.insert", Map.of("vo", vo, "parentNo", parentNo));
//		// updateOrderNo
//	}
	
	public int updateOrderNo(Long parentNo) {
		return sqlSession.update("board.updateOrderNo", parentNo);
	}
	
	public int deleteByNo(Long boardNo, Long userNo) {
		if(sqlSession.selectOne("board.getWriterNo",boardNo) == userNo) {
			return sqlSession.delete("board.deleteByNo", boardNo);
		} else {
			return 0;
		}
	}

	public int updateTitleAndContentsByNo(Long no, String title, String content, Long userNo) {
		if(sqlSession.selectOne("board.getWriterNo",no) == userNo) {
			return sqlSession.update("board.updateTitleAndContentsByNo", Map.of("no", no, "title", title, "contents", content));
		} else {
			return 0;
		}
	}
	
	public int updateHit(Long no) {
		return sqlSession.update("board.updateHit", no);
	}
	
}
