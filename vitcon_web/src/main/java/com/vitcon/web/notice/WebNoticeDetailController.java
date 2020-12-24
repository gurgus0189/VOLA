package com.vitcon.web.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notice/")

public class WebNoticeDetailController {
	
	@RequestMapping("notice_list_detail.do")
	public String notice(HttpSession session, Model model, HttpServletRequest request, @CookieValue(value = "localecd", defaultValue = "en") String localecode) {
		
		Integer grade = (Integer) session.getAttribute("grade");
			
			if (grade != null && grade < 20) {
				//구매자
				model.addAttribute("mAddShow", true);			
			}
			model.addAttribute("grade", grade);
			
			/*if (request.getParameter("menu_listview") == null) {
				return "redirect:/dashboard/listview.do?menu_listview=1";
			}*/
			/*model.addAttribute("menu_listview", 1);*/
			
		    return "notice/notice_list_detail";
	}
}
