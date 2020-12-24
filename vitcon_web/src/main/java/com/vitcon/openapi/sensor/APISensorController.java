package com.vitcon.openapi.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.user.UserVO;

//@RestController
public class APISensorController {
/*	
	//이 Controller는 예제로 작성되었고 아직까지는 쓰이지 않는다.
	private static final Logger logger = LoggerFactory.getLogger(APISensorController.class);
	
	@Autowired
	UserService user;
	
	@RequestMapping("openapi/user/get")
	public ResponseObject getUserElement(String username, String mobile) {
		ResponseObject ret = new ResponseObject();
		
		logger.info("username=" + username);

		UserVO vo = null;
		//db 조회
		try {
			vo = (UserVO) user.getUserID(username, mobile);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			ret.setData(e.getMessage());
			logger.error("error service=" + e.getMessage());
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);
		ret.setData(vo);
		
		return ret;
	}
*/
}