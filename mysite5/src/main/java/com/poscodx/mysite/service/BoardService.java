package com.poscodx.mysite.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poscodx.mysite.repository.BoardRepository;
import com.poscodx.mysite.vo.BoardVo;

@Service
public class BoardService {
	// 페이지 당 게시물 수 설정
	private static final int LIST_SIZE = 5;
	// 페이지 섹션 변수
	private static final int SECTION_SIZE = 5;
		
	@Autowired
	private BoardRepository boardRepository;
	
	@Transactional
	public void addContents(BoardVo vo, Long parentNo) {
		if(parentNo != null) {
			boardRepository.updateOrderNo(parentNo);
		}
		boardRepository.insert(vo, parentNo);
	}
//	public void addContents(BoardVo vo) {		
//		boardRepository.insertNew(vo);
//	}
//	public void addReply(BoardVo vo, Long parentNo) {
//		
//		boardRepository.insertReply(vo, parentNo);
//	}
	
	@Transactional
	public BoardVo getContents(Long no) { // 글 보기
		BoardVo vo = boardRepository.findByNo(no);
		if(vo != null) {
			boardRepository.updateHit(no);
		}
		return vo;
	}
	
	public void modifyContents(Long no, String title, String contents, Long userNo) {
		boardRepository.updateTitleAndContentsByNo(no, title, contents, userNo);
	}
	
	public void deleteContents(Long boardNo, Long userNo) {
		boardRepository.deleteByNo(boardNo, userNo);
	}
	
	@Transactional
	public Map<String, Object> getContentsList(Integer selectedPage, Boolean sectionDown, Boolean sectionUp, Integer currentPageSection) {
		//return boardRepository.findAll();
		
		List<BoardVo> list = null;
		Map<String, Object> map = null;
		
		// 페이지 섹션 변수
		int pageSection = 0;
		
		// 링크 설정 if문 변수
		int maxPage = 0;
		int maxSection = 0;
		
		// maxPage, maxSection 설정
		int totalViewCount = boardRepository.getViewCount();
		maxPage = (totalViewCount/LIST_SIZE)+1;
		maxSection = (maxPage/SECTION_SIZE)+1;
				
		// 페이지 섹션 up, down			
		
		if(sectionUp!=null && sectionUp==true) {
			pageSection = currentPageSection+1;
			selectedPage = (pageSection-1)*SECTION_SIZE+1;
			
		} else if (sectionDown!=null && sectionDown==true) {
			if(currentPageSection > 1) {
				pageSection = currentPageSection-1;
				selectedPage = pageSection*SECTION_SIZE;
				
			} else {
				pageSection = currentPageSection;
			}
			
		} else {
			if((selectedPage%SECTION_SIZE) == 0) {
				pageSection = selectedPage/SECTION_SIZE;
			} else {
				pageSection = (selectedPage/SECTION_SIZE)+1;
			}
		}
				
		// 현재 페이지의 게시물 리스트
		list = boardRepository.findAllWithPaging(selectedPage, LIST_SIZE);
		
		map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("maxPage", maxPage);
		map.put("maxSection", maxSection);
		map.put("sectionSize", SECTION_SIZE);
		map.put("selectedPage", selectedPage);
		map.put("pageSection", pageSection);
		
		return map;
	}
	
}
