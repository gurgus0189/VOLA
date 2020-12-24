package com.vitcon.openapi.statis.analyze;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.analyze.AnalyzeService;

@RestController
@RequestMapping("/openapi")
public class AnalyzeController {

	private static final Logger logger = LoggerFactory.getLogger(AnalyzeController.class);

	@Autowired
	private AnalyzeService service;
	
	/**
	 * 현재 시간 문제생긴 디바이스 리스트
	 * @return
	 */
	@RequestMapping("/problem/devicelist")
	public ResponseObject getProblemDeviceList() {
		
		ResponseObject ret = new ResponseObject();
		
		List<HashMap<String,Object>> list = null;

        UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return ret;
	    }

		try {
			list = service.getProblemDeviceList(userid);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			
			return ret;
		}
	
		ret.setReturnCode(StatusCode.OK);
		ret.setData(list);
		
		return ret;
	}
	
	/**
	 * 구매일자 오래된 것 
	 * @return
	 */
	@RequestMapping("/old/devicelist")
	public ResponseObject getOldDeviceList() {
		
		ResponseObject ret = new ResponseObject();
		List<HashMap<String,Object>> list = null;

        UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return ret;
	    }
		
		try {
			list = service.getOldDeviceList(userid);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);
		ret.setData(list);

		return ret;
	}

}
