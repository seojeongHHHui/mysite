package com.poscodx.mysite.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.poscodx.mysite.security.Auth;
import com.poscodx.mysite.service.BoardService;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@RequestMapping("")
	public String index(
			@RequestParam(value="page", required=true, defaultValue="1") Integer page,
			@RequestParam(value="sectionDown", required=false, defaultValue="") Boolean sectionDown,
			@RequestParam(value="sectionUp", required=false, defaultValue="") Boolean sectionUp,
			@RequestParam(value="currentPageSection", required=false, defaultValue="") Integer currentPageSection,
			Model model) {
		//List<BoardVo> list = boardService.getContentsList();
		//model.addAttribute("list", list);
		
		Map map = boardService.getContentsList(page, sectionDown, sectionUp, currentPageSection);
		model.addAllAttributes(map); // list 바로 접근 가능
		//model.addAttribute("map", map); // map.list 식으로 접근해야함
		
		return "board/index";
	}
	
	@RequestMapping("/view/{no}")
	public String view(@PathVariable Long no, Model model) {
		BoardVo vo = boardService.getContents(no);
		model.addAttribute("vo", vo);
		return "board/view";
	}
	
	@RequestMapping("/delete/{no}")
	public String delete(HttpSession session, @PathVariable Long no) {
		// access control
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/";
		}
		////////////////////////////////////
		boardService.deleteContents(no, authUser.getNo());
		return "redirect:/board";
	}
	
	@Auth
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write() {
		return "board/write";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(BoardVo vo) {
		boardService.addContents(vo, null);
		return "redirect:/board";
	}
	
	@RequestMapping(value="/reply/{no}", method=RequestMethod.GET)
	public String reply(@PathVariable Long no, Model model) {
		model.addAttribute("parentNo", no);
		return "board/reply";
	}
	
	@RequestMapping(value="/reply/{parentNo}", method=RequestMethod.POST)
	public String reply(BoardVo vo, @PathVariable Long parentNo) {
		boardService.addContents(vo, parentNo);
		return "redirect:/board";
	}
	
	@RequestMapping(value="/modify/{no}", method=RequestMethod.GET)
	public String modify(@PathVariable Long no, Model model) {
		BoardVo vo = boardService.getContents(no);
		model.addAttribute("vo", vo);
		return "board/modify";
	}
	
	@RequestMapping(value="/modify/{no}", method=RequestMethod.POST)
	public String modify(HttpSession session, BoardVo vo, @PathVariable Long no) {
		// access control
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/";
		}
		////////////////////////////////////
		boardService.modifyContents(no, vo.getTitle(), vo.getContents(), authUser.getNo());
		return "redirect:/board";
	}
	
}
