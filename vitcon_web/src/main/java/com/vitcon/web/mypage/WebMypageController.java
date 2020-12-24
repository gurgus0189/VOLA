package com.vitcon.web.mypage;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vitcon.service.account.AccountService;
import com.vitcon.service.user.UserVO;

@Controller
@RequestMapping("/mypage/")
public class WebMypageController {
	
	@Autowired 
	private AccountService accountService;
		
	@RequestMapping("user_sensor.do")
	public String userSensor(HttpSession session, Model model, String muserid) {
		
		Integer grade = (Integer) session.getAttribute("grade");
		
		if (grade != null && grade < 20) {
			//구매자
			model.addAttribute("mAddShow", true);			
		}
		model.addAttribute("grade", grade);
		
		UserVO uservo = new UserVO();
		uservo.setUserid(muserid);
		
		try {
			uservo = accountService.getUser(uservo);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 에러 페이지로 이동
			return "error/error";
		}
		
		model.addAttribute("user", uservo);	
		
		return "mypage/user_sensor";
	}
	
}
