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

public class ReplyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String contents = request.getParameter("content");
		int hit = 0;
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String regDate = dateFormat.format(now);
		// groupNo, orderNo, depth는 parentNo 활용해서 서브쿼리 ㄱㄱ
		Long userNo = Long.parseLong(request.getParameter("authUserNo"));
		
		Long parentNo = Long.parseLong(request.getParameter("parentNo"));
		
		BoardVo vo = new BoardVo();
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setHit(hit);
		vo.setRegDate(regDate);
		vo.setUserNo(userNo);
		
		new BoardDao().insertReply(vo, parentNo);
		
		response.sendRedirect(request.getContextPath() + "/board");
	}

}
