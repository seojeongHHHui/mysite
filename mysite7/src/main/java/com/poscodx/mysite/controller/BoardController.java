package com.poscodx.mysite.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
			Authentication authentication,
			Model model) {
		//List<BoardVo> list = boardService.getContentsList();
		//model.addAttribute("list", list);
		
		Map map = boardService.getContentsList(page, sectionDown, sectionUp, currentPageSection);
		//model.addAllAttributes(map); // list 바로 접근 가능
		model.addAttribute("map", map); // map.list 식으로 접근해야함
		
		model.addAttribute("principal", authentication != null ? authentication.getPrincipal() : null);
		
		return "board/index";
	}
	
	@RequestMapping("/view/{no}")
	public String view(@PathVariable Long no, Model model, Authentication authentication) {
		BoardVo vo = boardService.getContents(no);
		model.addAttribute("vo", vo);
		model.addAttribute("principal", authentication != null ? authentication.getPrincipal() : null);
		
		return "board/view";
	}
	
	@RequestMapping("/delete/{no}")
	public String delete(Authentication authentication, @PathVariable Long no) {
		UserVo authUser = (UserVo)authentication.getPrincipal();
		boardService.deleteContents(no, authUser.getNo());
		return "redirect:/board";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write() {
		return "board/write";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(Authentication authentication, BoardVo vo) {
		UserVo authUser = (UserVo)authentication.getPrincipal();
		vo.setUserNo(authUser.getNo());
		boardService.addContents(vo, null);
		return "redirect:/board";
	}
	
	@RequestMapping(value="/reply/{no}", method=RequestMethod.GET)
	public String reply(@PathVariable Long no, Model model) {
		model.addAttribute("parentNo", no);
		return "board/reply";
	}
	
	@RequestMapping(value="/reply/{parentNo}", method=RequestMethod.POST)
	public String reply(Authentication authentication, BoardVo vo, @PathVariable Long parentNo) {
		UserVo authUser = (UserVo)authentication.getPrincipal();
		vo.setUserNo(authUser.getNo());
		boardService.addContents(vo, parentNo);
		return "redirect:/board";
	}
	
	@RequestMapping(value="/modify/{no}", method=RequestMethod.GET)
	public String modify(Authentication authentication, @PathVariable Long no, Model model) {
		UserVo authUser = (UserVo)authentication.getPrincipal();
		BoardVo vo = boardService.getContents(no);
		model.addAttribute("vo", vo);
		return "board/modify";
	}
	
	@RequestMapping(value="/modify/{no}", method=RequestMethod.POST)
	public String modify(Authentication authentication, BoardVo vo, @PathVariable Long no) {
		UserVo authUser = (UserVo)authentication.getPrincipal();
		boardService.modifyContents(no, vo.getTitle(), vo.getContents(), authUser.getNo());
		return "redirect:/board";
	}
	
}
