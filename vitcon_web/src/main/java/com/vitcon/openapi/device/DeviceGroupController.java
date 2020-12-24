package com.vitcon.openapi.device;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.account.AccountService;
import com.vitcon.service.device.DeviceGroupVO;
import com.vitcon.service.device.DeviceService;
import com.vitcon.service.device.DeviceVO;
import com.vitcon.service.user.UserVO;



@RestController
@RequestMapping("/openapi/device/group/")
public class DeviceGroupController {
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceGroupController.class);
	
	@Autowired 
	private DeviceService deviceService;
	
	@Autowired 
	private AccountService accountService;

	/**
	 * 디바이스 그룹 리스트 출력
	 * 로그인한 사용자(구매자, 중간관리자, 사용자)가 볼 수 있는 디바이스들의 그룹 리스트를 출력한다. 
	 * @return ResponseObject 형태의 JSON 문자열
	 */
	@RequestMapping("list")
	public ResponseObject getGroupList() {
		List<DeviceGroupVO> vo= null;
		ResponseObject ret = new ResponseObject();
		
		// 향후 세션으로 넘어온 사용자 아이디로 대체한다.
		/*String userid = "A";*/ // tune
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

		try {
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}


			if (userVO.getGrade() == 10) { //  GRADE = 10 은 구매자 - 구매자 리스트뷰 select				
				vo = deviceService.getGroupList(userid);
			} else { //  구매자를 제외한 나머지 (중간관리자,사용자) 리스트뷰 select
				vo = deviceService.getGroupListUser(userid);
			}
		
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}
		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(vo);
		return ret;
	}
		
	/**
	 * 디바이스(들)의 그룹을 변경
	 * 디바이스(들)의 그룹아이디를 변경한다.
	 * 대상 디바이스는 인자 deviceid 에 해당하는 디바이스이며,
	 * 다수의 디바이스의 그룹아이디를 변경하고자 할 때는, deviceid 에 , 를 구분자로 하여 deviceid 를 받을 수 있다.
	 * 
	 * 예:
	 * 단일 디바이스의 그룹아이디를 3 으로 변경 : groupid=3&deviceid=abc
	 * 다수의 디바이스의 그룹아이디를 3으로 변경 : groupid=3&deviceid=abc,bcd,def 
	 * 
	 * @param groupid 변경하고자 하는 그룹아이디
	 * @param deviceid 변경하고자 하는 디바이스의 아이디(,를 구분자로 하여 여러대의 디바이스를 받을 수 있음)
	 * @return ResponseObject 형태의 JSON 문자열
	 */
	@RequestMapping("update")
	public ResponseObject updateGroup(String groupid, String deviceid) {
    	ResponseObject ret = new ResponseObject();
    	DeviceVO vo = new DeviceVO();
    	
    	// tune 필요
    	// 누군가가 groupid 를 자신이 소유한 그룹의 아이디가 아닌 강제적으로 타인의 그룹 아이디를 넣을 수 도 있을 수 있다.
    	// 그렇기에 향후 변경할 그룹이 내 소유의 그룹이 아닌지, 체크할 필요가 있을 수 있다.
    	
    	if (groupid == null || deviceid == null) {
    		ret.setReturnCode(StatusCode.ERROR_PARAMETER);
    		return ret;
    	}
    	    	    	
    	String [] arr = deviceid.split(",");
    	
    	int group = Integer.parseInt(groupid);
    	vo.setGroupid(group);
    	
    	for(int i = 0; i < arr.length; i++) {    		
    		vo.setDeviceid(arr[i]);
    		try {
    			deviceService.updateGroup(vo);		
			} catch (Throwable e) {
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				e.printStackTrace();
				return ret;
			}
    	}
    	
    	ret.setReturnCode(StatusCode.OK);
    	
    	return ret;
	}
	
	/**
	 * 디바이스 그룹 생성
	 * 
	 * 디바이스 그룹을 생성한다.
	 * 
	 * @param DBDeviceGroupVO 에 맞는 파라메터(groupname, defaultyn 필요)
	 * @return
	 */
	@RequestMapping("make")
    public ResponseObject insertDeviceGroup(DeviceGroupVO vo) {
    	// tune 필요
    	// 로그인한 사용자만 그룹을 생성할 수 있어야 한다(구매자만)
    	// 그렇기에 향후 변경할 그룹이 내 소유의 그룹이 아닌지, 체크할 필요가 있을 수 있다.
		
		// tune 필요
		// 현재 구매자의 세션 아이디를 userid 에 저장
		/*String userid = "A"; *///구매자 아이디
		ResponseObject ret = new ResponseObject();
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

    	vo.setUserid(userid);
    	
    	if (vo.getGroupname() == null || vo.getDefaultyn() == null) {
    		ret.setReturnCode(StatusCode.ERROR_PARAMETER);
    		return ret;
    	}

    	try {
    		deviceService.insertDeviceGroup(vo);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
		}
    	ret.setReturnCode(StatusCode.OK);
    	return ret;
    }
	// 디바이스 그룹 삭제 (delete 후 디바이스 업데이트 )
	/**
	 * 디바이스  그룹 삭제는 디바이스 그룹을 삭제하는 기능이다.
	 * 해당 그룹 삭제 시 해당 그룹 하위에 디바이스들이 남아있으면 디폴트그룹으로 변경(업데이트) 한다.  
	 * 디바이스의 그룹아이디를삭제  시 디폴트 그룹은 삭제가 되지 않으며 억지로 삭제할 경우  리턴 값을 401로 나타낸다. 
	 * 
	 * @param vo 삭제하려는 gruopid 이다. 
	 * @return
	 */
	@RequestMapping("delete")
    public ResponseObject deletetDeviceGroup(DeviceGroupVO vo) {
		// tune 필요
    	// 로그인한 사용자만 그룹을 삭제할 수 있어야 한다(구매자만)
    	// 그렇기에 향후 디바이스 그룹  삭제시 구매자인지 아닌지  체크할 필요가 있을 수 있다.
		ResponseObject ret = new ResponseObject();
		if (vo == null ) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}		
		DeviceGroupVO defaultgroupVO = new DeviceGroupVO();
		/*String userid = "A";*/ //구매자 아이디 (admin0이 아닐수도 있다. ) 
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
		defaultgroupVO.setUserid(userid);
		defaultgroupVO.setDefaultyn("Y");
		
		try {
			defaultgroupVO = deviceService.selectDeviceGroup(defaultgroupVO);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
			
		if (!deviceService.checkGroup(defaultgroupVO, vo)) {
			// 여기서 서비스 에러는  디폴트 그룹을 삭제할 수 없는것을 의미한다.
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
		List<DeviceVO> deviceids = null;
		
		// 2. 삭제할 디바이스 그룹 하위에 device들이 존재 하면 디바이스들을 기본 그룹으로 옮기 위한 작업 수행 시작.
		DeviceVO deviceVO = new DeviceVO();
		deviceVO.setGroupid(vo.getGroupid()); // 삭제할 그룹의 아이디
				
		// 2.1. 삭제할 그룹의 아이디를 가진 모든 디바이스를 조회한다.
		try {
			//device.selectDevice(deviceVO);
			deviceids = deviceService.selectDeviceList(deviceVO);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
		// 2.2. 삭제할 그룹의 아이디를 가진 모든 디바이스들의 그룹 아이디를 디폴트 그룹의 아이디로 변경한다. 
		// 디바이스의 그룹을 default 그룹으로 변경하기 위해 vo 를 재활용한다.
		deviceVO.setGroupid(defaultgroupVO.getGroupid());
	
		for (DeviceVO dvo : deviceids) {
			// 디폴트 그룹으로 변경할 디바이스를 선택한다. 
			deviceVO.setDeviceid(dvo.getDeviceid());			
			try {
				// 디바이스의 그룹을 디폴트 그룹으로 변경한다.
				deviceService.updateGroup(deviceVO);
			} catch (Throwable e) {
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}
		}
		
		// 삭제할 디바이스 그룹 하위에 device들이 존재 하면 디바이스들을 기본 그룹으로 옮기 위한 작업 수행 끝.
		// 3. 디바이스 그룹을 삭제한다.
		try {
			DeviceGroupVO deleteVO = new DeviceGroupVO();
			deleteVO.setUserid(userid);
			deleteVO.setGroupid(vo.getGroupid());
			deviceService.deleteDeviceGroup(deleteVO);
		} catch (Throwable e) {
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
		ret.setReturnCode(StatusCode.OK);
    	return ret;
    }	
	
	
    // 그룹관리페이지 리스트 - 리스트
    @RequestMapping("managementlist")
    public ResponseObject getDeviceGroup() {
    	ResponseObject ret = new ResponseObject();
    	// 향후 세션으로 넘어온 사용자 아이디로 대체한다.
    	/*String userid = "A";*/
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
    	/*if(userid == null) {
    		ret.setReturnCode(StatusCode.ERROR_PARAMETER);
    		return ret;
    	}*/
    	try {
			ret.setData(deviceService.getDeviceGroup(userid));
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
    	
    	ret.setReturnCode(StatusCode.OK);
    	return ret;
    }

    
    /**
     * 디바이스 그룹 수정
     * 해당 유저의 디바이스 그룹 관리 name명을 수정한다.
     * 
     * @param deviceGroupVO
     * @return
     */
    
    @RequestMapping("changegroup")
    public ResponseObject updateDeviceGroup(@ModelAttribute DeviceGroupVO deviceGroupVO) {
    	ResponseObject ret = new ResponseObject();
    	
    	// 추후 세션으로 대체한다.
    	/*String userid = "A";*/
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
    	deviceGroupVO.setUserid(userid);
    	if (deviceGroupVO.getGroupname() == null || deviceGroupVO.getGroupid() == null ) {
    		ret.setReturnCode(StatusCode.ERROR_PARAMETER);
    		return ret;
    	}
    	
    	try {
    		ret.setReturnCode(StatusCode.OK);
    		/*vo.setUserid(userid);
    		vo.setGroupname(groupname);
    		vo.setDefaultyn(defaultyn);
    		vo.setGroupid(group);*/
    		
			deviceService.updateDeviceGroup(deviceGroupVO);
		} catch (Throwable e) {
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
		}
    	ret.setReturnCode(StatusCode.OK);
    	return ret;
    }
    
}