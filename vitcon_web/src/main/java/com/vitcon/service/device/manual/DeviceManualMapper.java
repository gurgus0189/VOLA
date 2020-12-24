package com.vitcon.service.device.manual;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.vitcon.service.device.DeviceManualVO;

@Mapper
public interface DeviceManualMapper {
	public List<HashMap<String,Object>> list() throws Throwable;
	
	public List<DeviceManualVO> manuallist(DeviceManualVO vo) throws Throwable;
}
