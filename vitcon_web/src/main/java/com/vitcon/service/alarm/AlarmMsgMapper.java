package com.vitcon.service.alarm;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AlarmMsgMapper {

	public List<HashMap<String,Object>> getAlarmMsg(HashMap<String, Object> map) throws Throwable;
	public List<HashMap<String,Object>> getAlarmMsgChildUser(HashMap<String, Object> map) throws Throwable;
/*
	public List<HashMap<String,Object>> getAlarmMsg(@Param("userid") String userid
												   ,@Param("startdate") Date startdate
												   ,@Param("enddate") Date enddate) throws Throwable;
*/	
	
	
	public List<HashMap<String, Object>> getAlarmData(HashMap<String, Object> map) throws Throwable;
	
	public List<HashMap<String, Object>> getAlarmNextData(HashMap<String, Object> map) throws Throwable;
	
	public List<HashMap<String, Object>> getAlarmPreData(HashMap<String, Object> map) throws Throwable;
	
	public List<HashMap<String, Object>> getAlarmHomeData(HashMap<String, Object> map) throws Throwable;
	
	public List<HashMap<String, Object>> getAlarmDataMsgChildUser(HashMap<String, Object> map) throws Throwable;
	
	public List<HashMap<String, Object>> getAlarmNextMsgChildUser(HashMap<String, Object> map) throws Throwable;
	
	public List<HashMap<String, Object>> getAlarmPreMsgChildUser(HashMap<String, Object> map) throws Throwable;
	
	public List<HashMap<String, Object>> getAlarmHomeMsgChildUser(HashMap<String, Object> map) throws Throwable;
	
}
