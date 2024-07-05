package com.poscodx.mysite.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GuestbookLogRepository {
	
	@Autowired
    private SqlSession sqlSession;
	
	public int insert() {
		return sqlSession.update("guestbooklog.insert");
	}
	
	public int update() {
		return sqlSession.update("guestbooklog.update-increase");
	}
	
	public int update(Long no) {
		return sqlSession.update("guestbooklog.update-decrease", no);
	}
	
}
