package com.vitcon.openapi.code;

public class StatusCode {
	
	// 성공
	public static final String OK = "200";

	// 잘못된 요청
	public static final String ERROR_BAD_REQUEST = "400";
	
	// Service 에러
	public static final String ERROR_SERVICE = "401";
	
	// 파라미터 에러
	public static final String ERROR_PARAMETER = "402";
	
	// 인증되지 않은 페이지 요청 혹은 인증 에러
	public static final String ERROR_UNAUTHORIZED = "501";
	
	// 권한이 없는 사용자의 페이지 요청
	public static final String ERROR_ACCESSDENIED = "502";
	
	// 경고 메시지 : 이미 존재
	public static final String WARNING_EXISTS = "801";
	
	// 경고 메시지 : 사용 불가
	public static final String WARNING_NOT_AVAILABLE = "802";
}
