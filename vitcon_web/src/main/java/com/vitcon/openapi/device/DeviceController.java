package com.vitcon.openapi.device;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.core.redis.RedisService;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.account.AccountService;
import com.vitcon.service.device.DeviceGroupVO;
import com.vitcon.service.device.DevicePlanHistoryVO;
import com.vitcon.service.device.DevicePlanVO;
import com.vitcon.service.device.DeviceService;
import com.vitcon.service.device.DeviceVO;
import com.vitcon.service.device.UserAppVO;
import com.vitcon.service.user.UserVO;



@RestController
@RequestMapping("/openapi/device/")
public class DeviceController {
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
	
	@Autowired 
	private DeviceService deviceService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	/**
	 * 디바이스의 실시간 데이터 출력 	
	 * 디바이스들의 실시간 데이터를 출력한다
	 * 인자 deviceid 는 , 를 구분자로 하여 여러개의 디바이스를 받을 수 있다
	 *  
	 * @param deviceid 디바이스 아이디들(,를 구분자로 하여) 
	 * @return ResponseObject 형태의 JSON 문자열
	 */
	
	@RequestMapping("getdata")
	public ResponseObject getData(@RequestParam(value = "deviceid", required = false) String deviceid) {
		ResponseObject ret = new ResponseObject();
		
		// tune 필요
		// 로그인한 사용자만 가능
		
		// 파라메터 체크
		if (deviceid == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}
		
		String[] deviceidlist = deviceid.split(",");
		
		Set<String> keys = new HashSet<String>();		
		
		for (String id : deviceidlist) {
			keys.add(id);
		}
		
		List<String> list = redisService.multiGet(keys);
		
		Iterator<String> e = list.iterator();
		while (e.hasNext()) {
			if (e.next() == null) {
				e.remove();
			}
		}		
		
		ret.setData(list);		
		
		
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}
	
	
	@RequestMapping("list")
	public ResponseObject getDeviceList(@RequestParam(value = "devicetypecd", required = false) Integer devicetypecd
			                           ,@RequestParam(value = "groupid", required = false) Integer groupid) {

		ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<String, Object>();
	
		// 향후 세션으로 넘어온 사용자 아이디로 대체한다.
		/*String userid = "A";*/ // tune
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
		
		map.put("userid", userid);
		
		if (devicetypecd != null) {
			map.put("devicetypecd", devicetypecd.intValue());
		}
		map.put("groupid", groupid);
		
		List<HashMap<String, Object>> vo = null;
		//System.out.println(vo);
		try {
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}

			if (userVO.getGrade() == 10) { //  GRADE = 10 은 구매자 - 구매자 리스트뷰 select				
				vo = deviceService.getDeviceList(map);
			} else { //  구매자를 제외한 나머지 (중간관리자,사용자) 리스트뷰 select
				vo = deviceService.getDeviceListPast(map);
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}
		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(vo);
		//System.out.println("vo : "+vo);
		return ret;
	} 
    /**
     * 디바이스를 삭제한다. 
     * 이는 AccountController.java  파일에 deleteBuyer() 함수에서 사용된다. 
     * 구매자가 회원 탈퇴시 디바이스 테이블을 지우는 용도로 사용된다. 
     * 
     * @param deviceids 디바이스 아이디를 ,를 구분자로하여 받는다.  
     * @return ResponseObject
     */
    @RequestMapping("remove")
    public ResponseObject removeDevice(String deviceids, HttpSession session) {
    	ResponseObject ret = new ResponseObject();
    	
    	Integer grade = (int) session.getAttribute("grade");
    	
    	if (deviceids == null) {
    		ret.setReturnCode(StatusCode.ERROR_PARAMETER);
    		return ret;
    	}
    	
    	if (grade == null || grade >= 20) {
    		// 중간관리자 이상은 기기 삭제 못함
    		ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
    		return ret;
    	}
    	
    	String[] ids = deviceids.split(",");
    	
    	DeviceVO vo = new DeviceVO();
    	for (String id : ids) {
    		vo.setDeviceid(id);
    		if (!deviceService.removeDevice(vo)) {
    			ret.setReturnCode(StatusCode.ERROR_SERVICE);
    			return ret;
    		}
		}
    	
    	ret.setReturnCode(StatusCode.OK);
    	return ret;
    }
    
    /**
    * 구매자가 디바이스 등록 
 	    시나리오 : 구매자가 디바이스를 등록한다.(앱에서) 
           등록시에는 두가지 경우가 존재한다. 
	   1. 구매자가 디폴트 그룹을 제외한 나머지 그룹을 가지고 있는 경우 
	   2. 구매자가 디폴트 그룹만 가지고 있는 경우 
	 
	   1. 구매자가 디폴트 그룹을 제외한 나머지 그룹을 가지고 있는 경우 
	   url : /openapi/device/add?deviceid=29&devicetypecd=1&devicegroupid=29
	     파라미터 : deviceid(고유값)  = 추가할 디바이스 기기 
	           devicetypecd (디바이스 타입) = 디비에 값이 존재해야함 
		                                                           상대 회사에서 디바이스 타입에 대한 값을 넣어줌 
		       groupid 가 없는 경우는 디폴트 그룹에 디바이스를 등록한다.		
	           groupid는 구매자가 회원가입 시 디폴트 그룹을 자동으로 생성한다.
	   2. 구매자가 디폴트 그룹만 가지고 있는 경우 
	   url : /openapi/device/add?deviceid=14&devicetypecd=1
	     파라미터 : deviceid(고유값)  = 추가할 디바이스 기기 
	           devicetypecd (디바이스 타입) = 디비에 값이 존재해야함 
                                                                         상대 회사에서 디바이스 타입에 대한 값을 넣어줌 
		       groupid 가 있는 경우는 해당 디바이스 그룹을에 기기를 등록한다. 
			   groupid는 디비에 값이 존재해야한다. 
     * @param vo
     * @return
     */
    
    //경로: openapi/device/add
    @RequestMapping("add")
    @Transactional
    public ResponseObject add(@ModelAttribute DeviceVO vo) {
    	// tune 구매자인지 중관 관리자인지 판단하는 기능 추가 (추후에 작성하기로함)
    	ResponseObject ret = new ResponseObject();
        UserAuthentication user = null;
        String userid = null;

        TransactionStatus transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        
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
        /*if (vo.getDevicename() == null) {
        	ret.setReturnCode(StatusCode.ERROR_PARAMETER);
        	return ret; 
        }*/
    	
    	java.util.Date dt = new java.util.Date();
    	vo.setCreatedate(dt);
    	vo.setUpdatedate(dt);
    	vo.setUserid(userid);
    	
    	if (vo.getUserid() == null || vo.getDeviceid() == null) {
    		// userid 와 deviceid 는 필수값임으로 null 이면 에러 발생 
    		ret.setReturnCode(StatusCode.ERROR_PARAMETER);
    		return ret;
    	}       	
    	
    	// 이미 존재하는 기기인지 확인
		try {
			DeviceVO rvo = new DeviceVO();
			rvo.setDeviceid(vo.getDeviceid());
			
			List<DeviceVO> deviceListVO = deviceService.selectDeviceList(rvo);
			if (deviceListVO != null && deviceListVO.size() > 0) {
				ret.setReturnCode(StatusCode.WARNING_EXISTS);
				return ret;
	    	}
		} catch (Throwable e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    	if (vo.getGroupid() == null) {
    		// 디폴트 그룹  id 를 얻어와서 vo 에 세팅한다.
    		try {
				DeviceGroupVO deviceGroupVO = deviceService.getDefaultGroupId(vo);
				if (deviceGroupVO == null) {				
					ret.setReturnCode(StatusCode.ERROR_SERVICE);
					return ret;
				}
				vo.setGroupid(deviceGroupVO.getGroupid());
			} catch (Throwable e) {
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}
    	}
    	// Default 값 세팅
    	vo.setDeleteflag("N"); // Default 값으로 N 이 들어감
    	try {
			deviceService.insertDevice(vo);
			
			// 요금제 추가
			// plan에 디바이스 검사
			Integer deviceCount = 0;
			deviceCount = deviceService.getPlan(vo.getDeviceid());
			
			if(deviceCount == 0) {
				
				DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Date nowDate = new Date();
				Date planexp = new Date();
				
				Calendar cal = Calendar.getInstance();
		        cal.setTime(planexp);
		        cal.add(Calendar.MONTH, 12);
		        cal.add(Calendar.DATE, 1);
			    
		     	DevicePlanVO devicePlanVO = new DevicePlanVO();
				DevicePlanHistoryVO devicePlanHistoryVO = new DevicePlanHistoryVO();
		     	
		     	String nowDateStr = dateFormat.format(nowDate);
		     	String planexpStr = dateFormat.format(cal.getTime());
		     	
		     	devicePlanVO.setDeviceid(vo.getDeviceid());
		     	devicePlanVO.setRegdate(nowDateStr);
		     	devicePlanVO.setPlantype(1);
		     	devicePlanVO.setPlanexp(planexpStr);
		     	
		     	devicePlanHistoryVO.setDeviceid(vo.getDeviceid());
		     	devicePlanHistoryVO.setRegdate(nowDateStr);
		     	devicePlanHistoryVO.setPlantype(1);
		     	devicePlanHistoryVO.setPlanexp(planexpStr);
		     	devicePlanHistoryVO.setNote("첫 등록");
		     	
				deviceService.insertPlan(devicePlanVO);
				deviceService.planHistoryInsert(devicePlanHistoryVO);
				
			}
			
			this.transactionManager.commit(transactionStatus);
			
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.transactionManager.rollback(transactionStatus);
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
    	
    	ret.setReturnCode(StatusCode.OK);
    	return ret;
    }
    
    @RequestMapping("replaceUserApp")
    public ResponseObject replaceUserApp (@ModelAttribute UserAppVO vo) {
    	ResponseObject ret = new ResponseObject();
    	UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }
	    vo.setUserid(userid);
	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return ret;
	    }
    	
	    //등급과 관계없이 데이터 넣기
    	try {
			deviceService.replaceUserApp(vo);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}
    	
    	
    	//등급에 따라 데이터 삭제
    	//구매자는 최근 3개만 남기고 삭제 , 하위유저는 최근 1개만 남기고 삭제
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	UserVO userVO = new UserVO();
		userVO.setUserid(userid);
		
		map.put("userid", userid);
		
		try {
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}

			map.put("grade", userVO.getGrade());
			List<HashMap<String, Object>> resultVO = deviceService.getUserAppRecentList(map);
			logger.info("테스트");
			if(resultVO.size()>0) {
				deviceService.deleteUserAppRecentList(resultVO);
			}
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}
    	
    	
    	ret.setReturnCode(StatusCode.OK);
    	return ret;
    	
    	    	
    }
     
    // 공지사항
    @RequestMapping("getnoticedata")
	public ResponseObject getNoticeData(@RequestParam(value = "notice", required = false) String notice) {
		ResponseObject ret = new ResponseObject();
		
		// tune 필요
		// 로그인한 사용자만 가능
		
		// 파라메터 체크
		if (notice == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}
	
		Set<String> keys = new HashSet<String>();		
		
		keys.add(notice);
		
		List<String> list = redisService.noticeGet(keys);
		
		Iterator<String> e = list.iterator();
		while (e.hasNext()) {
			if (e.next() == null) {
				e.remove();
			}
		}	
		
		if(list.isEmpty()) {
			ret.setData("null");
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		} 
		
		ret.setData(list);		
		
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}
    
    // 팝업 광고
    @RequestMapping("getpopupdata")
	public ResponseObject getPopupData(@RequestParam(value = "popup", required = false) String popup) {
		ResponseObject ret = new ResponseObject();
		
		// 파라메터 체크
		if (popup == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}
	
		Set<String> keys = new HashSet<String>();		
		
		keys.add(popup);
		
		List<String> list = redisService.popupGet(keys);
		
		Iterator<String> e = list.iterator();
		while (e.hasNext()) {
			if (e.next() == null) {
				e.remove();
			}
		}	
		
		if(list.isEmpty()) {
			ret.setData("null");
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		} 
		
		ret.setData(list);		
		
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}
    
    
    
    
}