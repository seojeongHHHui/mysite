package com.poscodx.mysite.controller;

import java.util.Map;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.controller.action.board.DeleteAction;
import com.poscodx.mysite.controller.action.board.ListAction;
import com.poscodx.mysite.controller.action.board.ModifyAction;
import com.poscodx.mysite.controller.action.board.ModifyViewAction;
import com.poscodx.mysite.controller.action.board.ReplyAction;
import com.poscodx.mysite.controller.action.board.ReplyViewAction;
import com.poscodx.mysite.controller.action.board.ViewAction;
import com.poscodx.mysite.controller.action.board.WriteAction;
import com.poscodx.mysite.controller.action.board.WriteViewAction;

public class BoardServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Action> mapAction = Map.of(
			"view", new ViewAction(),
			"writeView", new WriteViewAction(),
			"write", new WriteAction(),
			"replyView", new ReplyViewAction(),
			"reply", new ReplyAction(),
			"modifyView", new ModifyViewAction(),
			"modify", new ModifyAction(),
			"delete", new DeleteAction(),
			"list", new ListAction()
	);

	@Override
	protected Action getAction(String actionName) {
		return mapAction.getOrDefault(actionName, new ListAction());
	}


}
