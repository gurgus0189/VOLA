package com.vitcon.openapi.device;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.account.AccountService;
import com.vitcon.service.device.AllDeviceListVO;
import com.vitcon.service.device.DeviceGroupVO;
import com.vitcon.service.device.DeviceService;
import com.vitcon.service.device.DeviceTypeVO;
import com.vitcon.service.device.DeviceVO;
import com.vitcon.service.user.UserVO;

@RestController
@RequestMapping("/openapi/device/type")
public class DeviceTypeController {
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceTypeController.class);
	
	@Autowired 
	private DeviceService device;
	
	@Autowired 
	private AccountService accountService;

	/**
	 * 디바이스 기기 타입 리스트 출력
	 * 온습도계, 온도계, 습도계와 같은 것을 디바이스 타입이라 부르며, 이 메소드를 통해 	
	 * 로그인한 사용자(구매자, 중간관리자, 사용자)가 볼 수 있는 디바이스들의 
	 * 기기 타입 리스트를 출력한다.
	 *   
	 * @return ResponseObject 형태의 JSON 문자열
	 */
	@RequestMapping("list")
	public ResponseObject getDeviceTypeList(@RequestParam(value = "groupid", required = false) Integer groupid) {
		ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<DeviceTypeVO> vo = null;
		
		// 향후 세션으로 넘어온 사용자 아이디로 대체한다.
		/*String userid = "A"*/; // tune
		UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

		UserVO userVO = new UserVO();
		userVO.setUserid(userid);

	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return ret;
	    }

	    map.put("userid", userid);
		if (groupid != null) {
			map.put("groupid", groupid.intValue());
		}

		try {			
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}
			
			if (userVO.getGrade() == 10) { //  GRADE = 10 은 구매자 - 구매자 리스트뷰 select				
				vo = device.getDeviceTypeList(map);			
			} else { //  구매자를 제외한 나머지 (중간관리자,사용자) 리스트뷰 select
				vo = device.getDeviceTypeListUser(map);			
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