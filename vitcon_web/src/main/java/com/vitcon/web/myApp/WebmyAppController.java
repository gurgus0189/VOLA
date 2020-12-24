package com.vitcon.web.myApp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage/")

public class WebmyAppController {

	@RequestMapping("myApp.do")
	public String mypage(HttpSession session, Model model, HttpServletRequest request, @CookieValue(value = "localecd", defaultValue = "en") String localecode) {
		
		/*String message = "hello";
		model.addAttribute("message", message);*/
		Integer grade = (Integer) session.getAttribute("grade");
				
			if (grade != null && grade < 20) {
				//구매자
				model.addAttribute("mAddShow", true);			
			}
			model.addAttribute("grade", grade);
			
			return "mypage/myApp";
	}
	
}
