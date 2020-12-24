package com.vitcon.service.device.manual;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vitcon.service.device.DeviceManualVO;

@Service
public class DeviceManualService implements DeviceManualMapper{
	
	@Autowired
	private DeviceManualMapper deviceManualMapper;

	@Override
	public List<HashMap<String, Object>> list() throws Throwable {
		return deviceManualMapper.list();
	}

	@Override
	public List<DeviceManualVO> manuallist(DeviceManualVO vo) throws Throwable {
		// TODO Auto-generated method stub
		return deviceManualMapper.manuallist(vo);
	}
	
	
}
