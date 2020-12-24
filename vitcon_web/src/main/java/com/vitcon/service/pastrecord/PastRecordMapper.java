package com.vitcon.service.pastrecord;

import java.util.HashMap;
import java.util.List;

public interface PastRecordMapper {

	/* 날짜별 과거 데이터 조회 */
	public List<HashMap<String,Object>> getTimeRecodList(HashMap<String, Object> map) throws Throwable;

	// 과거데이터조회 toast 그리드
	public List<HashMap<String, Object>> getTimeRecodGridList(HashMap<String, Object> map) throws Throwable;

	// 대시보드디테일 toast 그리드
	public List<HashMap<String, Object>> getDashBoardDetailGridlist(HashMap<String, Object> map) throws Throwable;
	
	
	
	
	public List<HashMap<String, Object>> getPastData(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getPastNextData(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getPastPreData(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getPastHomeData(HashMap<String, Object> map) throws Throwable;
	
}
