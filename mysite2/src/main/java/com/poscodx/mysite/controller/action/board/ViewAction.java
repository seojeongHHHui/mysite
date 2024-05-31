package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String stringNo = request.getParameter("no");
		Long no = Long.parseLong(stringNo); // 글 번호
		new BoardDao().hitCountUp(no);
		
		BoardVo vo = new BoardDao().findView(no);
		request.setAttribute("vo", vo);
		
		request
		.getRequestDispatcher("/WEB-INF/views/board/view.jsp")
		.forward(request, response);
	}

}
