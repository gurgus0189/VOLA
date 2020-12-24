package com.vitcon.service.deviceuser;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.vitcon.service.device.DeviceGroupVO;
import com.vitcon.service.device.DeviceVO;
import com.vitcon.service.device.UserAppVO;
import com.vitcon.service.device.UserDeviceVO;
import com.vitcon.service.user.UserVO;

@Mapper
public interface DeviceUserMapper {
	// delete DeviceChannelRange테이블
	public void deleteDeviceChannelRange(HashMap<String, Object> map) throws Throwable;
	
	public void deleteUserDevice(HashMap<String, Object> map) throws Throwable;
	// foreach 문으로 돌아가는 deleteUserDevice
	public void deleteDevice(DeviceVO deviceVO) throws Throwable;
    ///selectDeviceUser
	public List<HashMap<String, Object>> selectDeviceUser(String userid) throws Throwable;
	
	public List<UserVO> selectUser(UserVO vo) throws Throwable;
	
	public void deleteDeviceGroup(DeviceGroupVO vo);
	
	public List<UserDeviceVO> selectUserDevice(UserDeviceVO vo) throws Throwable;
	// where 조건으로 돌아가는 deleteUserDevice
	public void deleteUserDeviceByWhere(UserDeviceVO vo) throws Throwable;
	
	public void insertUserDevice(UserDeviceVO vo) throws Throwable;

	//getDeviceListByMiddleId
	public List<HashMap<String, Object>> getDeviceListByMiddleId(HashMap<String, Object> map) throws Throwable;
	
	public void deleteUserApp(UserAppVO vo) throws Throwable;
	
	//userApp 부분 추가 19-02-20 김원태
	public List<UserAppVO> getUserApp(HashMap<String, Object> map) throws Throwable;
}


