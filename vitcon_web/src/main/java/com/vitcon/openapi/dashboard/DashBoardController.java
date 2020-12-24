package com.vitcon.openapi.dashboard;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.device.DeviceController;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.account.AccountService;
import com.vitcon.service.device.DeviceService;
import com.vitcon.service.user.UserVO;

@RestController
@RequestMapping("/openapi/dashboard/")
public class DashBoardController {
	private static final Logger logger = LoggerFactory.getLogger(DashBoardController.class);
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired 
	private AccountService accountService;
	
	/**
	 * 
	 * 해당 유저가 보는 기기리스트 이다.
	 * 기기리스트란 각 디바이스의 채널(센서)데이터를 실시간으로 보는것을 의미한다.
	 * 해당 유저가 관리하는 디바이스에 따라 기기리스트 를 볼수있다. 
	 * 구매자는 전체 기기 리스트를 본다. 
	 * 구매자를 제외한(사용자,중간관리자)는 디바이스관리에 따라 기기리스트를 본다. 
	 * @return
	 */
	@RequestMapping("list")
	public ResponseObject list() {
		ResponseObject ret = new ResponseObject();
		//추후 세션값으로 대체한다.
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
		
		UserVO userVO = new UserVO();
		userVO.setUserid(userid);
		
		try {
			//1. 유저 아이디를 받아서 select * from user 함으로 user의 grade컬럼을 참조하여 권한을 알아낸다.
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}
			HashMap<String, Object> map = new HashMap<>();
			map.put("userid", userVO.getUserid());
			
			if (userVO.getGrade() == 10) { //  GRADE = 10 은 구매자 - 구매자 리스트뷰 select				
				ret.setData(deviceService.selectBuyer(map));
			} else { //  구매자를 제외한 나머지 (중간관리자,사용자) 리스트뷰 select
				ret.setData(deviceService.selectBuyerExcept(map));
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
		}
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}
}
