package com.poscodx.mysite.controller.action.board;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;


public class WriteAction implements Action { // 새 글

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String contents = request.getParameter("content");
		int hit = 0;
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String regDate = dateFormat.format(now);
		//int groupNo = 
		int orderNo = 1;
		int depth = 0;
		Long userNo = Long.parseLong(request.getParameter("authUserNo"));
		
		BoardVo vo = new BoardVo();
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setHit(hit);
		vo.setRegDate(regDate);
		//vo.setGroupNo(groupNo);
		vo.setOrderNo(orderNo);
		vo.setDepth(depth);
		vo.setUserNo(userNo);
		
		new BoardDao().insertNew(vo);
		
		response.sendRedirect(request.getContextPath() + "/board");
	}

}
