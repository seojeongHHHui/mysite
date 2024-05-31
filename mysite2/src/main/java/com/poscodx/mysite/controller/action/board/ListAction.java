package com.poscodx.mysite.controller.action.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;

public class ListAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//List<BoardVo> list = new BoardDao().findAll();
		
		// 페이지 당 게시물 수 설정
		int viewCountPerPage = 5;
		
		// 페이지 섹션 변수
		int sectionSize = 5;
		int pageSection = 0;
		
		// 링크 설정 if문 변수
		int maxPage = 0;
		int maxSection = 0;
		
		
		// maxPage, maxSection 설정
		int totalViewCount = new BoardDao().getViewCount();
		maxPage = (totalViewCount/viewCountPerPage)+1;
		maxSection = (maxPage/sectionSize)+1;
		
		// 현재 페이지
		String stringPage = request.getParameter("page");
		int selectedPage = (stringPage!=null) ? Integer.parseInt(stringPage) : 1;
		
		// 페이지 섹션 up, down
		String currentPageSection = request.getParameter("currentPageSection");
		String sectionUp = request.getParameter("sectionUp");
		String sectionDown = request.getParameter("sectionDown");
		
		if(currentPageSection!=null) {
			int CurrentPageSectionNum = Integer.parseInt(currentPageSection);
			if(sectionUp!=null) {
				pageSection = CurrentPageSectionNum+1;
				selectedPage = (pageSection-1)*sectionSize+1;
				
			} else if (sectionDown!=null) {
				if(CurrentPageSectionNum > 1) {
					pageSection = CurrentPageSectionNum-1;
					selectedPage = pageSection*sectionSize;
					
				} else {
					pageSection = CurrentPageSectionNum;
				}
			}
			
		} else {
			if((selectedPage%sectionSize) == 0) {
				pageSection = selectedPage/sectionSize;
			} else {
				pageSection = (selectedPage/sectionSize)+1;
			}
		}
		
		// 현재 페이지의 게시물 리스트
		List<BoardVo> list = new BoardDao().findAllWithPaging(selectedPage, viewCountPerPage);
		
		// list.jsp에 전달
		request.setAttribute("maxPage", maxPage);
		request.setAttribute("maxSection", maxSection);
		request.setAttribute("sectionSize", sectionSize);
		request.setAttribute("selectedPage", selectedPage);
		request.setAttribute("pageSection", pageSection);
		request.setAttribute("list", list);
		
		request
			.getRequestDispatcher("/WEB-INF/views/board/list.jsp")
			.forward(request, response);
	}

}
