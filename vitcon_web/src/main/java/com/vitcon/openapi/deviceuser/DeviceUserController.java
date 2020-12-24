package com.vitcon.openapi.deviceuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.device.DeviceService;
import com.vitcon.service.device.UserAppVO;
import com.vitcon.service.device.UserDeviceVO;
import com.vitcon.service.deviceuser.DeviceUserService;
import com.vitcon.service.tui.grid.CommonMapper;
import com.vitcon.service.tui.grid.Data;
import com.vitcon.service.tui.grid.Pagination;
import com.vitcon.service.tui.grid.ResponseGridVO;
import com.vitcon.service.user.UserVO;


@RestController
@RequestMapping("/openapi/deviceuser/")
public class DeviceUserController {
	
	@Autowired
	private DeviceService deviceService;

	@Autowired 
	private DeviceUserService deviceUserService;
	
	@Autowired
	private CommonMapper commonmapper;

	// 제윤씨 한테서 해당 유저를 파라미터로 받음
	// 파라미터 인자는 uservo의 userid로 받는다. ,인자로 자른다.
	@RequestMapping("deleteUserDevice")
	public ResponseObject deleteUserDevice (UserVO userVO) {
		ResponseObject ret = new ResponseObject();
		
		String[] userid = userVO.getUserid().split(",");
			try {
				HashMap<String, Object> map = new HashMap<>();
				List<String> list = new ArrayList<>();
				for(int i=0; i<userid.length; i++) {
					list.add(userid[i]);
				}
				map.put("userid", list);
				deviceUserService.deleteUserDevice(map);
				
				// System.out.println("list : "+list);
				// System.out.println("userid.length : "+userid.length);
				// System.out.println("userid : "+userid);
				
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}		
		return ret;
	}
	
	// 계정관리 -구매자 삭제 (재윤씨 사용) 
	// 파라미터를 제윤씨한테 받는다. userid는 제윤씨가 줌
	// 추후에 세션으로 대체한다. 파라미터	
	@RequestMapping("removedevice")
	public ResponseObject removeDevice (String userid) {
		ResponseObject ret = new ResponseObject();
		
		if(deviceUserService.removeDevice(userid) == false) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}
	// 종욱씨와 상이가 필요하다 파라미터관련 
	// 계정 관리 : 중간 관리자  등록 디바이스 선택 
	@RequestMapping("create")
	public ResponseObject create (@ModelAttribute UserDeviceVO requestVO) {		
		ResponseObject ret = new ResponseObject();
		// 파라메터 체크 우선순위
		if (requestVO.getUserid() == null) {			
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}		
		UserDeviceVO userVO = new UserDeviceVO();
		try {
			String[] deviceidList = requestVO.getDeviceid().split(",");			
			userVO.setUserid(requestVO.getUserid());			
			// 중간관리자 혹은 사용자가 관리하는 디바이스들 모두 삭제
			deviceUserService.deleteUserDeviceByWhere(userVO);
			
			if(requestVO.getDeviceid() == "") {
				// 관리할 디바이스가 없는 경우 리턴한다.
				ret.setReturnCode(StatusCode.OK);
				return ret;
			}
			
			for (String deviceid : deviceidList) {
				userVO.setDeviceid(deviceid);
				deviceUserService.insertUserDevice(userVO);
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
		
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}	
	
	// 계정관리 리스트 // 파라미터 middleid = 중간관리자 id
	@RequestMapping("getdevicelist")
	public ResponseObject getDeviceList(HttpSession session, UserVO middleid) {
		ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<>();
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
	    map.put("middleid",middleid.getUserid());  // 중간관리자 id
		map.put("userid", session.getAttribute("userid"));
		
		try {
			List<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();
			List<HashMap<String, Object>> devicearr = null;
			HashMap<String, Object> group = null;
			HashMap<String, Object> device = null;
			int pastGroupid = 0;
			int groupid = 0;
			List<HashMap<String, Object>> list = deviceUserService.getDeviceListByMiddleId(map);
									
			for (int i = 0; i < list.size(); i++) {
				
				groupid = (int)list.get(i).get("groupid");
				
				//그룹아이디가 바뀔때만 새로 선언함
				if (groupid != pastGroupid) {
					
					if (group != null) {
						group.put("devices", devicearr);
						arr.add(group);
					}
					
					group = new HashMap<String, Object>();
					group.put("groupid", list.get(i).get("groupid"));
					group.put("groupname", list.get(i).get("groupname"));
					devicearr = new ArrayList<HashMap<String, Object>>();
					pastGroupid = groupid;
				}
				
				device = new HashMap<String, Object>();
				device.put("deviceid", list.get(i).get("deviceid"));
				device.put("devicename", list.get(i).get("devicename"));
				device.put("defaultyn", list.get(i).get("defaultyn"));
				device.put("createdate", list.get(i).get("createdate"));
				device.put("devicetypecd", list.get(i).get("devicetypecd"));
				device.put("devicetypename", list.get(i).get("devicetypename"));
				device.put("subuser", list.get(i).get("subuser"));
				
				devicearr.add(device);
			}
			
			if (group != null) {
				group.put("devices", devicearr);
				arr.add(group);
			}
			
			//obj.put("data", arr);
			//ret.setData(obj.toString());
			ret.setData(arr);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
		
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}
	
	/* userapp 조회  2019-02-20 추가 toast grid 용 데이터 */
	@RequestMapping(value = "/getuserapp", method = RequestMethod.GET)
	public ResponseGridVO getUserApp( 
			@RequestParam("page") int page,
			@RequestParam("perPage") int perPage) {
		ResponseGridVO result = new ResponseGridVO();
		int total =0;
		
		//ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<String, Object>();

		List<UserDeviceVO> list = null;

		UserAuthentication user = null;
		String userid = null;

		try {
			user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			userid = user.getName(); // 로그인한 사용자의 id
		} catch (Exception e) {
		}

		if (userid == null) {
			// 유저아이디 존재하지 않음.
			result.setResult(false);
			result.setMessage("error");
			result.setData(null);
			return result;
		}
		
		map.put("userid", userid);
		if(page <=0)
		{
			page = 1;
		}
		map.put("page", ((page-1)*perPage));
		map.put("perPage", perPage);
		
		
		List<UserAppVO> result2;
		try {
			result2 = deviceUserService.getUserApp(map);
			total = commonmapper.pagingTotal();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setResult(false);
			result.setMessage("getUserApp error");
			result.setData(null);
			return result;
		}
		
		//리턴시키는 값들
		result.setResult(true);
		Data data2 = new Data();
		Pagination pagination2 = new Pagination();
		data2.setContents(result2);
		pagination2.setPage(page);
		pagination2.setTotalCount(total);
		data2.setPagination(pagination2);
		
		result.setData(data2);

		return result;
	}
	
}