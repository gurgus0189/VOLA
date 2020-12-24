package com.vitcon.service.device;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceMapper {
	/*public List<MappingDeviceUser> getUserDevice(String userid) throws Throwable;*/
	
	
	// 디바이스 관리 페이지 - 디바이스타입 리스트 및 타입별 센서 리스트 출력 기능
	public List<DeviceTypeVO> getDeviceTypeList(HashMap<String, Object> map) throws Throwable;

	// 디바이스 관리 페이지 - 기기가 들어있는 디바이스 타입만 검색
	public List<DeviceTypeVO> getDeviceTypeListUser(HashMap<String, Object> map) throws Throwable;
	
	// 디바이스 관리 페이지- 전체 디바이스 출력 기능 
	public List<HashMap<String, Object>> getDeviceList(HashMap<String, Object> map) throws Throwable;

	// 과거데이터 내역 조회에서 구매자 외 회원의 디바이스 리스트 조회
	public List<HashMap<String, Object>> getDeviceListPast(HashMap<String, Object> map) throws Throwable;
	
	// 디바이스 정보 얻기
	public HashMap<String, Object> getDevice(String deviceid) throws Throwable;
	
	//디바이스 관리 페이지 - 그룹 리스트 가져오기
	public List<DeviceGroupVO> getGroupList(String userid) throws Throwable;

	//디바이스 관리 페이지 - 기기가 들어있는 그룹만 가져오기
	public List<DeviceGroupVO> getGroupListUser(String userid) throws Throwable;
	
	//디바이스 관리 페이지 - 그룹변경 
	public void updateGroup(DeviceVO vo) throws Throwable;
	
	//디바이스 관리 페이지 - 센서 설정
	public List<HashMap<String, Object>> getDeviceSetList(String deviceid) throws Throwable;
	
    //디바이스 관리 페이지>디바디이스 설정 - device의 범위설정값 저장
    public void setDeviceSet(HashMap<String, Object> map) throws Throwable;

    // 디바이스 관리 페이지 - 디바이스설정 - 일괄변경 select 
    public List<HashMap<String, Object>> getAllChange(HashMap<String, Object>map) throws Throwable;

    // 디바이스 관리 페이지 - 디바이스설정 - 일괄변경 DELETE
    public void deleteDeviceChannelRange(HashMap<String, String[]> map) throws Throwable;    
    
    /*
    // 디바이스 관리 페이지 - 디바이스설정 - 디바이스 아이디로 채널  range 삭제
    public void deleteDeviceChannelRangeByDeviceid(DeviceChannelRangeVO vo)throws Throwable;
    */
    
    // 디바이스 관리 페이지 - 디바이스설정 - 일괄변경 insert
    public void insertDeviceChannelRange(DeviceChannelRangeVO vo) throws Throwable;
    
    // 디바이스 관리 페이지 - 디바이스설정 - 일괄변경 select(디버깅용)
    public List<DeviceChannelRangeVO> selectDeviceChannelRange() throws Throwable;
    
    //구매자 제품 선택 및 가이드 
    public List<DeviceManualVO> selectDeviceManual(int devicetypecd) throws Throwable;
    
    //디바이스 그룹 리스트 select  
    public List<DeviceGroupVO> getDeviceGroup(String userid) throws Throwable;
    
    //디바이스 그룹  추가  
    public void insertDeviceGroup(DeviceGroupVO vo) throws Throwable;
    
    //디바이스 그룹 변경 
    public void updateDeviceGroup(DeviceGroupVO vo) throws Throwable;

    //디바이스 그룹 삭제     
    public void deleteDeviceGroup(DeviceGroupVO vo) throws Throwable;
    
   
    //디바이스 테이블 select 
    public DeviceVO selectDeviceCount(DeviceVO vo) throws Throwable;
  //디바이스 테이블 select 
    public DeviceVO selectDevice(String userid) throws Throwable;
    //디바이스 테이블 select 
    public List<DeviceVO> selectDeviceList(DeviceVO vo) throws Throwable;
    
    //디바이스그룹  테이블 select 
    public DeviceGroupVO selectDeviceGroup(DeviceGroupVO vo) throws Throwable;
    
    
    //디바이스 테이블 INSERT
    public void insertDevice(DeviceVO vo) throws Throwable;
    
    //유저 디바이스 테이블 - delete 
    public void deleteUserDevice(String deviceid) throws Throwable;
    
    //디바이스 테이블 - delete
    public void deleteDevice(String deviceid) throws Throwable;
    
    //디바이스 테이블 - update
    public void updateDevice(DeviceVO vo) throws Throwable;
    
    //리스트뷰 - 구매자 기기리스트 
    public List<HashMap<String, Object>> selectBuyer(HashMap<String, Object>map) throws Throwable;
    
    //리스트뷰 - 중간 관리자 기기리스트
    public List<HashMap<String, Object>> selectBuyerExcept(HashMap<String, Object>map) throws Throwable;
    
    public void replaceUserApp(UserAppVO vo)throws Throwable; 

    public void deleteUserApp(HashMap<String, Object> map)throws Throwable; 
    
    //유저 앱기기 리스트용 2019-02-20 김원태 추가    
    public void deleteUserAppRecentList(List<HashMap<String, Object>> map)throws Throwable;
    
    public List<HashMap<String, Object>> getUserAppRecentList(HashMap<String, Object>map) throws Throwable;
    
    // 통계 디바이스명 리스트 
    public List<DeviceNameVO> getNameList(String userid) throws Throwable;

    // 통계 하위계정 디바이스명 리스트
	public List<DeviceNameVO> getNameListUser(String userid) throws Throwable;

	// 네트워크(통신) 업데이트
	public void updateSysPush(DeviceVO vo) throws Throwable;
	
	// 네트워크(통신) 푸시 
	public HashMap<String, Object> getSystemPush(String deviceid) throws Throwable;

	// 플랜 가져오기
	public Integer getPlan(String deviceid) throws Throwable;

	// 플랜 추가
	public void insertPlan(DevicePlanVO devicePlanVO) throws Throwable;

	// 플랜 내역 추가
	public void planHistoryInsert(DevicePlanHistoryVO devicePlanHistoryVO) throws Throwable;
	
	
	
}
