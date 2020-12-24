package com.vitcon.service.device;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitcon.core.rabbitmq.RabbitProducer;
import com.vitcon.core.redis.RedisService;
import com.vitcon.openapi.response.ResponseObject;

@Service
public class DeviceService implements DeviceMapper {
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private RabbitProducer producer;

	@Autowired 
	private DeviceMapper mapper;

	@Override
	public List<DeviceTypeVO> getDeviceTypeList(HashMap<String, Object> map) throws Throwable {
		return mapper.getDeviceTypeList(map);
	}

	@Override
	public List<HashMap<String, Object>> getDeviceList(HashMap<String, Object> map) throws Throwable {
		return mapper.getDeviceList(map);
	}

	@Override
	public List<DeviceGroupVO> getGroupList(String userid) throws Throwable {
		return mapper.getGroupList(userid);
	}

	@Override
	public List<HashMap<String, Object>> getDeviceSetList(String deviceid) throws Throwable {
		
		return mapper.getDeviceSetList(deviceid);
	}

	@Override
	public void setDeviceSet(HashMap<String, Object> map) throws Throwable {
		mapper.setDeviceSet(map);
	}

	@Override
	public void updateGroup(DeviceVO vo) throws Throwable {
		mapper.updateGroup(vo);
	}

	@Override
	public List<HashMap<String, Object>> getAllChange(HashMap<String, Object> map) throws Throwable {
		return mapper.getAllChange(map);
	}
	
	public void deleteDeviceChannelRange(String[] deviceid) throws Throwable {
		if (deviceid == null)
			return;
		HashMap<String, String[]> map = new HashMap<>();
		map.put("deviceid", deviceid);
		
		mapper.deleteDeviceChannelRange(map);	
	}
	
	@Override
	public void deleteDeviceChannelRange(HashMap<String, String[]> map) throws Throwable {		
	}

	@Override
	public void insertDeviceChannelRange(DeviceChannelRangeVO vo) throws Throwable {
		mapper.insertDeviceChannelRange(vo);
	}
	
	@Override
	public List<DeviceChannelRangeVO> selectDeviceChannelRange() throws Throwable {
		return mapper.selectDeviceChannelRange();
	}

	@Override
	public List<DeviceManualVO> selectDeviceManual(int devicetypecd) throws Throwable {
		// TODO Auto-generated method stub
		return mapper.selectDeviceManual(devicetypecd);
	}
	
	@Override
	public List<DeviceGroupVO> getDeviceGroup(String userid) throws Throwable {
		// TODO Auto-generated method stub
		return mapper.getDeviceGroup(userid);
	}
	
	@Override
	public void insertDeviceGroup(DeviceGroupVO vo) throws Throwable {
		mapper.insertDeviceGroup(vo);
	}
	@Override
	public void updateDeviceGroup(DeviceGroupVO vo) throws Throwable {
		mapper.updateDeviceGroup(vo);  
	}
	
	@Override
	public void deleteDeviceGroup(DeviceGroupVO vo) throws Throwable {
		mapper.deleteDeviceGroup(vo);
	}
	
	@Override
	public List<DeviceVO> selectDeviceList(DeviceVO vo) throws Throwable {
		// TODO Auto-generated method stub
		return mapper.selectDeviceList(vo);
	}
	@Override
	public DeviceVO selectDevice(String userid) throws Throwable {
		// TODO Auto-generated method stub
		return mapper.selectDevice(userid);
	}
	@Override
	public DeviceVO selectDeviceCount(DeviceVO vo) throws Throwable {
		// TODO Auto-generated method stub
		return mapper.selectDeviceCount(vo);
	}

	@Override//반환 타입을 리스트 형식으로바꿔주어야 하나? 
	public DeviceGroupVO selectDeviceGroup(DeviceGroupVO VO) throws Throwable {
		// TODO Auto-generated method stub
		return mapper.selectDeviceGroup(VO);
	}
	 
	public DeviceGroupVO selectDeviceGroup(String userid) throws Throwable {
		DeviceGroupVO vo = new DeviceGroupVO();
		vo.setUserid(userid);
		return mapper.selectDeviceGroup(vo);
	}
	
	@Override
	public void insertDevice(DeviceVO vo) throws Throwable {
		mapper.insertDevice(vo);
	}
	
	@Override
	public void deleteUserDevice(String deviceid) throws Throwable {
		mapper.deleteUserDevice(deviceid);
	}

	@Override
	public void deleteDevice(String deviceid) throws Throwable {
		mapper.deleteDevice(deviceid);
	}
	
	public boolean removeDevice(DeviceVO vo) {
		boolean ret = false;
		
		String[] deviceid = new String[1];
		deviceid[0] = vo.getDeviceid();
		
		try {
			// delete devicechannelrange
			deleteDeviceChannelRange(deviceid);
			
			// delete userdevice
			deleteUserDevice(vo.getDeviceid());
			
			// delete device
			deleteDevice(vo.getDeviceid());
			
			//delete redis key 
			redisService.deleteKey(vo.getDeviceid());
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		ret = true;
		
		return ret;
	}
	
	@Override
	public void updateDevice(DeviceVO vo) throws Throwable {
		// TODO Auto-generated method stub
		mapper.updateDevice(vo);
	}

	// HashMap 으로 변경 해줌
	public void updateDevice(String deviceid, String wifissid) throws Throwable {
		DeviceVO vo = new DeviceVO();
				
		vo.setWifissid(wifissid);
		vo.setDeviceid(deviceid);
		
		this.updateDevice(vo);
	}
	
	public Integer devicegroupcheck (String userid) throws Throwable {
		ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<>();
		DeviceGroupVO devicegroupcheck = selectDeviceGroup(userid);
		DeviceGroupVO devicegroup = new DeviceGroupVO();
	    DeviceVO device = new DeviceVO();
	    int groupid ;
		if(devicegroupcheck.getCount() != 0) {
			groupid = devicegroupcheck.getGroupid();
		} else {			
			devicegroup.setUserid(userid);
			devicegroup.setGroupname("default");
			devicegroup.setDefaultyn("Y");
			try {
				mapper.insertDeviceGroup(devicegroup);	
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		    selectDeviceGroup(devicegroup.getUserid());
		    groupid = devicegroup.getGroupid();
		}
		return groupid;
	}
	
	public String encoder(String list) throws UnsupportedEncodingException {		
		String encodeResult = URLDecoder.decode( list,"UTF-8");
		return encodeResult;	
	}
	
	
	public boolean updateChannelRange(DeviceVO devicevo, DeviceChannelRangeVO[] channelRangeListVO) throws Throwable {
		ArrayList<DeviceVO> deviceids = new ArrayList<>();
		deviceids.add(devicevo);
		return updateChannelRange(deviceids, channelRangeListVO);
	}

	public boolean updateChannelRange(ArrayList<DeviceVO> devicevoList, DeviceChannelRangeVO[] channelRangeListVO) throws Throwable {
		
    	// 디바이스별 채널 range 데이터 update(파라메터로 넘어왔을때만)
		if (channelRangeListVO == null)
			return false;
		if (devicevoList == null || devicevoList.size() < 1) {
			return false;
		}
		String[] deviceids = new String[devicevoList.size()];
		int i = 0;
		for (DeviceVO vo : devicevoList) {
			if (vo.getDeviceid() == null || vo.getDevicetypecd() == null) {
				return false;
			}
			deviceids[i++] = vo.getDeviceid();
		}
		
		// 디바이스 아이디들의 channelrange 테이블 삭제
		deleteDeviceChannelRange(deviceids);
		for (DeviceVO vo : devicevoList) {
    		DeviceChannelRangeVO channelRangeVO = new DeviceChannelRangeVO();
    		channelRangeVO.setDeviceid(vo.getDeviceid());
	    	// 디바이스별 채널 range 데이터 적재
	    	for (DeviceChannelRangeVO v : channelRangeListVO) {
	    		channelRangeVO.setChannelid(v.getChannelid());
	    		channelRangeVO.setChannelrangedata(v.getChannelrangedata());
	    		channelRangeVO.setDevicetypecd(vo.getDevicetypecd());
	    		
	    		// DB 에 데이터 insert
	    		insertDeviceChannelRange(channelRangeVO);
	    	}
    	}
		
		// RabbitMQ 를 통해 데이터 insert
		insertDeviceChannelRangeRabbitMQ(devicevoList, channelRangeListVO);
    	return true;
	}
	
	/**
	 * rabbitmq 를 통해 데이터 적재
	 * @param channelRangeVO
	 */
	public void insertDeviceChannelRangeRabbitMQ(ArrayList<DeviceVO> devicevoList, DeviceChannelRangeVO[] channelRangeListVO) {
		if (devicevoList == null) {
			return;
		}
		
		HashMap<String, Object> configMap = new HashMap<>();
		
		ObjectMapper mapper = new ObjectMapper();
		
		// cf json 을 위한 값 생성
		for (DeviceChannelRangeVO v : channelRangeListVO) {
			String channelid = String.valueOf(v.getChannelid());
			HashMap<String, Object> data;
			try {
				data = mapper.readValue(v.getChannelrangedata(), HashMap.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("insertDeviceChannelRangeRabbitMQ:mapper.readvalue failed");
				return;
			}
			HashMap<String, String> newdata = new HashMap<String, String>();
			
			String min = String.valueOf(data.get("MIN"));
			String max = String.valueOf(data.get("MAX"));
			
			newdata.put("1", min);
			newdata.put("2", max);
			
			configMap.put(channelid, newdata);
		}
		
		for (DeviceVO vo : devicevoList) {
			HashMap<String, Object> root = new HashMap<>();
			
			String deviceid = vo.getDeviceid();
			String routingKey = String.format("vitcon.control.%s", deviceid);
			
			root.put("id", deviceid);
			root.put("cf", configMap);
			
			boolean pushEnableResult = false;
			
			if(vo.getPushenable().equals("Y")) {
				pushEnableResult = true;
			}
				
			root.put("pe", pushEnableResult);
			root.put("pi", vo.getPushinterval());
			root.put("pc", vo.getPushrepeat());
			root.put("si", vo.getSaveinterval());
			
			
			String json;
			try {
				json = mapper.writeValueAsString(root);
				producer.sendMessage(routingKey, json);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean checkGroup(DeviceGroupVO defaultgroup,DeviceGroupVO deletegroup) {
		if (defaultgroup.getGroupid().equals(deletegroup.getGroupid())) {
			return false;
		}
		return true;
	}
	
	 public DeviceGroupVO getDefaultGroupId(DeviceVO vo) throws Throwable {
		 DeviceGroupVO deviceGroupVO = new DeviceGroupVO();
		 deviceGroupVO.setDefaultyn("Y");
		 deviceGroupVO.setUserid(vo.getUserid());
		 return selectDeviceGroup(deviceGroupVO);
	 }

	@Override
	public List<HashMap<String, Object>> selectBuyer(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		return mapper.selectBuyer(map);
	}

	@Override
	public List<HashMap<String, Object>> selectBuyerExcept(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		return mapper.selectBuyerExcept(map);
	}

	@Override
	public void replaceUserApp(UserAppVO vo) throws Throwable {
		mapper.replaceUserApp(vo); 
	}

	@Override
	public void deleteUserApp(HashMap<String, Object> map) throws Throwable {
		mapper.deleteUserApp(map); 
	}

	@Override
	public HashMap<String, Object> getDevice(String deviceid) throws Throwable {
		// TODO Auto-generated method stub
		return mapper.getDevice(deviceid);
	}
	
	@Override
	public List<HashMap<String, Object>> getDeviceListPast(HashMap<String, Object> map) throws Throwable {
		return mapper.getDeviceListPast(map);
	}

	@Override
	public List<DeviceGroupVO> getGroupListUser(String userid) throws Throwable {
		return mapper.getGroupListUser(userid);
	}

	@Override
	public List<DeviceTypeVO> getDeviceTypeListUser(HashMap<String, Object> map) throws Throwable {
		return mapper.getDeviceTypeListUser(map);
	}
	
	//2019-02-20 앱기기 리스트용 김원태 추가
	@Override
	public void deleteUserAppRecentList(List<HashMap<String, Object>> map) throws Throwable {
		// TODO Auto-generated method stub
		mapper.deleteUserAppRecentList(map);
	}

	@Override
	public List<HashMap<String, Object>> getUserAppRecentList(HashMap<String, Object> map) throws Throwable {
		
		return mapper.getUserAppRecentList(map);
	}
	
	@Override
	public List<DeviceNameVO> getNameList(String userid) throws Throwable {
		return mapper.getNameList(userid);
	}
	@Override
	public List<DeviceNameVO> getNameListUser(String userid) throws Throwable {
		return mapper.getNameListUser(userid);
	}

	@Override
	public void updateSysPush(DeviceVO vo) throws Throwable {
		mapper.updateSysPush(vo);
	}
	
	@Override
	public HashMap<String, Object> getSystemPush(String deviceid) throws Throwable {
		return mapper.getSystemPush(deviceid);
	}
	
	@Override
	public Integer getPlan(String deviceid) throws Throwable {
		return mapper.getPlan(deviceid);
	}

	@Override
	public void insertPlan(DevicePlanVO devicePlanVO) throws Throwable {
		mapper.insertPlan(devicePlanVO);
	}

	@Override
	public void planHistoryInsert(DevicePlanHistoryVO devicePlanHistoryVO) throws Throwable {
		mapper.planHistoryInsert(devicePlanHistoryVO);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
