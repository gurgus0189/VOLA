package com.vitcon.core.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.vitcon.core.rest.RestWriter;
import com.vitcon.openapi.code.StatusCode;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Autowired
	RestWriter restWriter;

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {

		return new AccessDeniedHandler() {
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				restWriter.writeCode(request, response, StatusCode.ERROR_ACCESSDENIED);
			}
		};
		
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {

		// 세션관리를 위해, 로그인이 되지 않은 사용자가 인증이 필요한 페이지에 접근을 하면 JSON 으로 에러를 리턴하도록 설정한다

		return new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				// TODO Auto-generated method stub
				if (request.getRequestURI().indexOf("/openapi") < 0) {
					//openapi 페이지 접근이 아닐 경우, 로그인 페이지로 이동하게 함					
					RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
					redirectStrategy.sendRedirect(request, response, "/auth/login.do?returnto=" + request.getServletPath());
					return;
				}
				restWriter.writeCode(request, response, StatusCode.ERROR_UNAUTHORIZED);
			}
		};
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 여기서는 사용자의 권한에 따라, 해당 페이지를 접근하게 할거야., 기본적으로 login 이 가능한 사용
		http.authorizeRequests() // 인가 관련 설정을 한다
				.antMatchers("/openapi/auth/login").permitAll()				
				.antMatchers("/auth/login.do").permitAll()				
				.antMatchers("/auth/member_join.do").permitAll()
				.antMatchers("/auth/member_add.do").permitAll()
				.antMatchers("/auth/member_id.do").permitAll()
				.antMatchers("/auth/member_pw.do").permitAll()
				.antMatchers("/openapi/account/insertuser").permitAll()
				.antMatchers("/openapi/account/getemail").permitAll()
				.antMatchers("/openapi/account/getuserid").permitAll()
				.antMatchers("/openapi/account/getpasswd").permitAll()
				.antMatchers("/openapi/account/getportalid").permitAll()
				.antMatchers("/portal/naverLoginCallBack.do").permitAll()				
				.antMatchers("/openapi/auth/logout").permitAll()
				.antMatchers("/auth/login.do").permitAll()
				.antMatchers("/auth/logout.do").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/images/**").permitAll()				
				/*
				.antMatchers("/sample/method1").access("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_USERADMIN')")
				.antMatchers("/sample/method2").access("hasRole('ROLE_CUSTOMER')")
				.antMatchers("/sample/method3").access("hasRole('ROLE_USER')")
				.antMatchers("/sample/method4").access("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_USERADMIN') or hasRole('ROLE_USER')")
				*/
				.antMatchers("/**").authenticated()
				.and()
				.formLogin()
				.loginPage("/auth/login.do")
				.permitAll()
				.and()
				.logout().permitAll()				
				.and().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler).and()
				.csrf().disable();				
	}

}