package com.vitcon.service.controlrecord;

import java.util.HashMap;
import java.util.List;

public interface ControlRecordMapper {

	public List<HashMap<String,Object>> getControlRecord(HashMap<String, Object> map) throws Throwable;
	
}
