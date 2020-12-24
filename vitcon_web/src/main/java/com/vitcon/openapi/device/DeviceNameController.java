package com.vitcon.openapi.device;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.account.AccountService;
import com.vitcon.service.device.DeviceNameVO;
import com.vitcon.service.device.DeviceService;
import com.vitcon.service.user.UserVO;

@RestController
@RequestMapping("/openapi/device/")
public class DeviceNameController {

	@Autowired 
	private DeviceService device;
	
	@Autowired 
	private AccountService accountService;
	
	
	
	@RequestMapping("name/list")
	public ResponseObject getNameList() {
		ResponseObject ret = new ResponseObject();
	
		List<DeviceNameVO> vo = null;
		
		UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

		UserVO userVO = new UserVO();
		userVO.setUserid(userid);
		
		// 파라메터 체크
		if (userid == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}
		try {			
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}
			
			if (userVO.getGrade() == 10) { //  GRADE = 10 은 구매자 - 구매자 리스트뷰 select				
				vo = device.getNameList(userid);			
			} else { //  구매자를 제외한 나머지 (중간관리자,사용자) 리스트뷰 select
				vo = device.getNameListUser(userid);			
			}

		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;			
		}
		
		ret.setData(vo);
		ret.setReturnCode(StatusCode.OK);
		
		return ret;
		
	}
	
}
