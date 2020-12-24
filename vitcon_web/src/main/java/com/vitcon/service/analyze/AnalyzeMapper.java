package com.vitcon.service.analyze;

import java.util.HashMap;
import java.util.List;

public interface AnalyzeMapper {

	/*현재 시간 문제생긴 디바이스 리스트를 가져온다*/
	public List<HashMap<String,Object>> getProblemDeviceList(String userid) throws Throwable;
	
	/*구매일자가 오래된 디바이스 리스트를 가져온다*/
	public List<HashMap<String,Object>> getOldDeviceList(String userid) throws Throwable;
	
}
