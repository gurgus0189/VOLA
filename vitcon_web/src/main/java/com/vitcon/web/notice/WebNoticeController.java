package com.vitcon.web.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notice/")

public class WebNoticeController {
	
	@RequestMapping("notice_list.do")
	public String notice(HttpSession session, Model model, HttpServletRequest request, @CookieValue(value = "localecd", defaultValue = "en") String localecode) {
		
		//String message = "hello";
		//model.addAttribute("message", message);
		
		Integer grade = (Integer) session.getAttribute("grade");
				
			if (grade != null && grade < 20) {
				//구매자
				model.addAttribute("mAddShow", true);			
			}
			model.addAttribute("grade", grade);
			
			if (request.getParameter("menu_listview2") == null) {
				return "redirect:/notice/notice_list.do?menu_listview2=1";
			}
			model.addAttribute("menu_listview2", 1);
			
		    /*if(localecode.equals("ko")) return "notice/notice_list";
		    else if(localecode.equals("en")) return "notice/notice_list_en";
		    else if(localecode.equals("ja")) return "notice/notice_list_ja";
		    else return "notice/notice_list_en"; */
		
		return "notice/notice_list";
		
	}
}
