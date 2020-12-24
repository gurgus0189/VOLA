package com.vitcon.core.interceptor;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.service.account.AccountService;
import com.vitcon.service.user.UserVO;

@Component
public class MultiLanguageInterceptor extends HandlerInterceptorAdapter {
	
	@Qualifier("localeResolver")
	@Autowired	
	private LocaleResolver localeResolver;
	
	@Autowired
	private AccountService accountService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		//return super.preHandle(request, response, handler);

		UserAuthentication user = null;
		String userid = null;
		
		//url에 language 파리미터가 있는 경우
		String language = request.getParameter("language");
		
		
		if (language != null) {
			
			if(language.equals("ko")| language.equals("en") | language.equals("ja") ) {
		
			//한글, 영어, 일본어 가 아닌경우 에는 아무것도 하지 않는다.
			Cookie cookie = new Cookie("localecd", language);
			cookie.setMaxAge(90 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			request.setAttribute("localecd", language);
			
			//실제 로케일 변경
			Locale l1 = new Locale(language);
			localeResolver.setLocale(request, response, l1);
			}
			return true;
			
		} else {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				int i =0;
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("localecd") && cookies[i].getValue() != "") {
						language = cookie.getValue();
						request.setAttribute("localecd", language);
						Locale l2 = new Locale(language);
						localeResolver.setLocale(request, response, l2);
						break;
					}
					else {
						//쿠키가 아예 없고 language도 없어서 브라우저 기본으로 쿠키 만들자
						String language2 = request.getLocale().getLanguage();
						
						if(language2.equals("ko")| language2.equals("en") | language2.equals("ja") )
						{
							
						}
						else
						{
							language2 = "en";
						}

						//한국어, 영어,  일본어 제외하고 글로벌 언어는 영어로 하자
						Cookie cookie2 = new Cookie("localecd", language2);
						cookie2.setMaxAge(90 * 24 * 60 * 60);
						cookie2.setPath("/");
						response.addCookie(cookie2);
						
						request.setAttribute("localecd", language2);
						
						Locale l3 = new Locale(language2);
						localeResolver.setLocale(request, response, l3);
						
					}
					i++;
				}
			}
		}

	/*	try {
			user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			userid = user.getName(); // 로그인한 사용자의  id 
		} catch(Exception e) {
			return true;
		}

		if (userid == null)
			return true;

		UserVO uservo = new UserVO();
		uservo.setUserid(userid);
		
		language = request.getParameter("language");
		if (language != null) {			
			uservo.setLocalecd(language);
			try {
				accountService.updateUser(uservo);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			uservo.setLocalecd(null);
		}
		
		try {
			uservo = accountService.getUser(uservo);
			if (uservo != null && uservo.getLocalecd() != null) {
				//locale 설정				
				Locale l = new Locale(uservo.getLocalecd());
				localeResolver.setLocale(request, response, l);
				
				Cookie ncookie = new Cookie("localecd", uservo.getLocalecd());
				response.addCookie(ncookie);
				
				request.setAttribute("localecd", uservo.getLocalecd());
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}

		//localeResolver.setLocale(request, response, Locale.ENGLISH);
		//localeResolver.setLocale(request, response, l);
		*/
		return true;
	}
	
	/*@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		//return super.preHandle(request, response, handler);

		UserAuthentication user = null;
		String userid = null;
		
		String language = request.getParameter("language");
		if (language != null) {
			Cookie cookie = new Cookie("localecd", language);
			cookie.setMaxAge(90 * 24 * 60 * 60);
			response.addCookie(cookie);
		} else {
			//쿠키를 통해서 자동적으로 언어설정을 변경하도록 수정
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("localecd")) {
						language = cookie.getValue();
						request.setAttribute("localecd", language);
						Locale l1 = new Locale(language);
						localeResolver.setLocale(request, response, l1);
						break;
					}
				}
			}
		}

		try {
			user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			userid = user.getName(); // 로그인한 사용자의  id 
		} catch(Exception e) {
			return true;
		}

		if (userid == null)
			return true;

		UserVO uservo = new UserVO();
		uservo.setUserid(userid);
		
		language = request.getParameter("language");
		if (language != null) {			
			uservo.setLocalecd(language);
			try {
				accountService.updateUser(uservo);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			uservo.setLocalecd(null);
		}
		
		try {
			uservo = accountService.getUser(uservo);
			if (uservo != null && uservo.getLocalecd() != null) {
				//locale 설정				
				Locale l = new Locale(uservo.getLocalecd());
				localeResolver.setLocale(request, response, l);
				
				Cookie ncookie = new Cookie("localecd", uservo.getLocalecd());
				response.addCookie(ncookie);
				
				request.setAttribute("localecd", uservo.getLocalecd());
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}

		//localeResolver.setLocale(request, response, Locale.ENGLISH);
		//localeResolver.setLocale(request, response, l);
		
		return true;
	}*/
}
