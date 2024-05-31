package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;

public class DeleteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String stringNo = request.getParameter("no");
		Long no = Long.parseLong(stringNo);
		
		new BoardDao().deleteByNo(no);
		
		response.sendRedirect(request.getContextPath() + "/board");
	}

}
