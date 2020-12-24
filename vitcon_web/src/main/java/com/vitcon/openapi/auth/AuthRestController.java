package com.vitcon.openapi.auth;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.core.password.Password;
import com.vitcon.core.rest.RestWriter;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.auth.AuthService;
import com.vitcon.service.user.UserVO;

@RestController
public class AuthRestController {

	@Autowired
	private RestWriter restWriter;

	@Autowired
	private AuthService authService;

	private static final Logger logger = LoggerFactory.getLogger(AuthRestController.class);
	
	@RequestMapping(value = "/openapi/auth/login")
	public ResponseObject login(@ModelAttribute UserVO userVO, HttpSession session) {
		/* Rest Controller 를 통해 로그인 인증을 하기 위한 페이지 */		
		ResponseObject ret = new ResponseObject();
		
		if (userVO.getPortalid() == null && (userVO.getUserid() == null || userVO.getPasswd() == null)) {				
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		String passwd = Password.password(userVO.getPasswd());
		userVO.setPasswd(passwd);
		
		if (authService.login(userVO, session) == false) {		   
		   ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
		   return ret;
		}
		
		/*if (!authService.isServiceTime(userVO)) {
			ret.setReturnCode(StatusCode.WARNING_NOT_AVAILABLE);
			return ret;
		}*/
		
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}

	@RequestMapping(value = "/openapi/auth/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response, Model model) {
		/* 로그아웃 처리 */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}		
		
		/*Cookie [] cookies = request.getCookies();
		
		for (Cookie cookie : cookies) {
			System.out.println(cookie.getName());
			System.out.println(cookie.getValue());
			if (cookie.getName().equals("userid") || cookie.getName().equals("passwd")) {				
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0);				
				response.addCookie(cookie);
			}
		}*/
		
/*		Locale localecd = response.getLocale();
		model.addAttribute("localecd", localecd);*/
				
		try {
			restWriter.writeCode(request, response, StatusCode.OK);
		} catch (IOException e) {
			logger.error("write code error", e);
		}
	}

	@RequestMapping(value = "/openapi/auth/loginfo")
	public void loginfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		UserAuthentication user = null;
		String userid = null;

		try {
			user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			userid = user.getName(); // 로그인한 사용자의 id
		} catch (Exception e) {
		}

		ResponseObject ret = new ResponseObject();

		int grade = 0;

		try {
			grade = (int) session.getAttribute("grade");
		} catch (Exception e) {
		}

		ret.setReturnCode(StatusCode.OK);
		ret.setData("userid=>" + userid + ",grade=" + grade);

		try {
			restWriter.writeObject(request, response, ret);
		} catch (IOException e) {
			logger.error("write code error", e);
		}
	}

}