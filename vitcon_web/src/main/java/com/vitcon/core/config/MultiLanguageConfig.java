package com.vitcon.core.config;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.vitcon.core.interceptor.MultiLanguageInterceptor;

@Configuration
public class MultiLanguageConfig implements WebMvcConfigurer {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private MultiLanguageInterceptor multiLanguageInterceptor;
	
	private static final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

	@Bean
	public MessageSource messageSource() {		
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("static/js/messages/messages");
		messageSource.setDefaultEncoding("UTF-8");		
		return messageSource;
	}
	

/*	@Bean(name = "localeResolver")
	public LocaleResolver sessionLocaleResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();		
		localeResolver.setDefaultLocale(new Locale("ko"));
		return localeResolver;
	}	*/
	
	
/*	@Bean(name = "localeResolver")
	public LocaleResolver cookieLocaleResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
				
		localeResolver.setCookieName("vitcon-lang");
		localeResolver.setCookieMaxAge(86400 * 365 * 10); //10년
		localeResolver.setCookiePath("/");
		localeResolver.setCookieDomain("vitcon.co.kr");
		//localeResolver.setDefaultLocale(Locale.KOREAN);
		return localeResolver;
	}
*/
	@Bean(name = "localeResolver")
	public LocaleResolver cookieLocaleResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		
//		localeResolver.setCookieName("lang");
		localeResolver.setCookieName("localecd");
		localeResolver.setCookieMaxAge(86400 * 365 * 10); //10년
		localeResolver.setCookiePath("/");
//		localeResolver.setCookieDomain("vitcon.co.kr");
//		localeResolver.setDefaultLocale(Locale.KOREAN);
		return localeResolver;
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		//WebMvcConfigurer.super.addInterceptors(registry);

		// 언어 설정값을 DB 에서 읽어와 locale 을 설정하기 위한 목적으로 인터셉터 등록
		registry.addInterceptor(multiLanguageInterceptor)
				.addPathPatterns("/**/*.do"); // 모든 URL 에 대하여 패턴 설정
/*				.addPathPatterns("/**") // 모든 URL 에 대하여 패턴 설정
				.excludePathPatterns("/openapi/auth/*") // 제외할 URL 에 대한 패턴 설정				
				.excludePathPatterns("/web/auth/*"); // 제외할 URL 에 대한 패턴 설정
*/		
		/*LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("language");
		 		
		// 파라메터를 이용하여 다국어 변경을 할 수 있도록 인터셉터 등록
		registry.addInterceptor(lci);	*/
	}
	
}