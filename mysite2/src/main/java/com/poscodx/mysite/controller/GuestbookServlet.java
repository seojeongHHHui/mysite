package com.poscodx.mysite.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.action.guestbook.DeleteAction;
import com.poscodx.mysite.controller.action.guestbook.DeleteFormAction;
import com.poscodx.mysite.controller.action.guestbook.InsertAction;
import com.poscodx.mysite.controller.action.guestbook.ListAction;
import com.poscodx.mysite.dao.GuestbookDao;
import com.poscodx.mysite.dao.UserDao;
import com.poscodx.mysite.vo.GuestbookVo;
import com.poscodx.mysite.vo.UserVo;

public class GuestbookServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Action> mapAction = Map.of(
			"insert", new InsertAction(),
			"deleteform", new DeleteFormAction(),
			"delete", new DeleteAction()			
	);
	
	@Override
	protected Action getAction(String actionName) {
		return mapAction.getOrDefault(actionName, new ListAction());
	}

}
