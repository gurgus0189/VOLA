package com.vitcon.service.deviceuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.device.DeviceGroupVO;
import com.vitcon.service.device.DeviceMapper;
import com.vitcon.service.device.DeviceVO;
import com.vitcon.service.device.UserAppVO;
import com.vitcon.service.device.UserDeviceVO;
import com.vitcon.service.tui.grid.CommonMapper;
import com.vitcon.service.user.UserVO;


@Service
public class DeviceUserService implements DeviceUserMapper{
	
	@Autowired
	private DeviceUserMapper mapper;
	
	@Autowired DeviceMapper deviceMapper;
	
	@Autowired
	private CommonMapper commonmapper;

	@Override
	public void deleteDeviceChannelRange(HashMap<String, Object> map) throws Throwable {
		mapper.deleteDeviceChannelRange(map);
	}

	@Override
	public void deleteUserDevice(HashMap<String, Object> map) throws Throwable{
		mapper.deleteUserDevice(map);
	}

	@Override
	public void deleteDevice(DeviceVO deviceVO) throws Throwable {
		mapper.deleteDevice(deviceVO);
	}
	
	@Override
	public List<HashMap<String, Object>> selectDeviceUser(String userid) throws Throwable {
		return mapper.selectDeviceUser(userid);
	}
	
	@Override
	public List<UserVO> selectUser(UserVO vo) throws Throwable {
		return mapper.selectUser(vo);
	}

	@Override
	public void deleteDeviceGroup(DeviceGroupVO vo) {
		mapper.deleteDeviceGroup(vo);
	}
	
	// select * from userdevice 
	@Override
	public List<UserDeviceVO> selectUserDevice(UserDeviceVO vo) throws Throwable {
		return mapper.selectUserDevice(vo);
	}

	@Override
	public void insertUserDevice(UserDeviceVO vo) throws Throwable {

		mapper.insertUserDevice(vo);
	}

	@Override
	public void deleteUserDeviceByWhere(UserDeviceVO vo) throws Throwable {
		mapper.deleteUserDeviceByWhere(vo);
	}	
	
	public boolean removeDevice(String userid) {
		ResponseObject ret = new ResponseObject();
		// 추후에 세션으로 대체한다.
		//String userid = "Buyer3@";
		try {
			List<HashMap<String, Object>> result = null ;
			List<String> deviceid = new ArrayList<>();	
			HashMap<String, Object> deviceidMap = new HashMap<>();
			// 디바이스 유저를 셀렉트 하여 값을참조할때 null 인경우 에러
			result=selectDeviceUser(userid); // 해당 유저의 deviceid, groupid 가져옴 
			for(int i=0; i<result.size(); i++) {
				deviceid.add((String) result.get(i).get("deviceid"));
			}
			deviceidMap.put("deviceid",deviceid);  
			if(deviceidMap.get(deviceid) != null) {
				// null인경우는 제외
				deleteDeviceChannelRange(deviceidMap);// 해당 유저의 Deviceid를 참조하여 deviceChannelRange테이블 삭제				
			}
			UserAppVO userAppVO = new UserAppVO();
			userAppVO.setUserid(userid);
			deleteUserApp(userAppVO);  //userApp 삭제
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return false;
		}
		try {
			UserVO puserid = new UserVO();
			puserid.setPuserid(userid);
			List<UserVO> result = selectUser(puserid); // 구매자 하위소속인  중간 관리자 리스트 뽑아냄
			List<String> useridIn = new ArrayList<>();
			for(int i=0; i<result.size(); i++) {
				useridIn.add(result.get(i).getUserid());
			}
			HashMap<String, Object> useridMap = new HashMap<>();
			useridMap.put("userid", useridIn);
			if(useridIn.size() != 0) {//중간 관리자 삭제할 유저가있는 경우에만 처리한다. 
				deleteUserDevice(useridMap); // 중간 관리자 삭제 기능					
			}
			DeviceVO deviceVO = new DeviceVO();
			deviceVO.setUserid(userid);
			deleteDevice(deviceVO); // 해당 구매자의 deviceID를 참조하여 device테이블 삭제
			DeviceGroupVO deviceGroupVO = new DeviceGroupVO();
			deviceGroupVO.setUserid(userid);
			deleteDeviceGroup(deviceGroupVO);
		} catch (Throwable e) {			
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return false;
		}
		ret.setReturnCode(StatusCode.OK);
		return true;
	}

	@Override
	public List<HashMap<String, Object>> getDeviceListByMiddleId(HashMap<String, Object> map) throws Throwable {
		return mapper.getDeviceListByMiddleId(map); 
	}

	@Override
	public void deleteUserApp(UserAppVO vo) throws Throwable {
		mapper.deleteUserApp(vo);
	}
	
	//getUserApp 2019-02-20 김원태 추가
	@Override
	public List<UserAppVO> getUserApp(HashMap<String, Object> map) throws Throwable {
		return mapper.getUserApp(map);
	}	
}
