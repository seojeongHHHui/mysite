package com.poscodx.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poscodx.mysite.service.SiteService;

@Controller
public class MainController {
	
	@RequestMapping({"/", "/main"})
	public String index() {
		
		return "main/index";
	}
	
}
