package com.vitcon.service.kindrecord;

import java.util.HashMap;
import java.util.List;

public interface KindRecordMapper {

	/**
	 * 전체 통계 리스트 
	 * @param userid
	 * @param startdate
	 * @param enddate
	 * @param devicetypecd
	 * @return
	 * @throws Throwable
	 */
	public List<HashMap<String,Object>> getEntireStatisticList(HashMap<String,Object> map) throws Throwable;

	/**
	 * 그룹 통계 리스트
	 * @param userid
	 * @param startdate
	 * @param enddate
	 * @param devicetypecd
	 * @return
	 * @throws Throwable
	 */
	public List<HashMap<String,Object>> getGroupStatisticList(HashMap<String,Object> map) throws Throwable;
	
	/**
	 * 센서별 리스트
	 * @param userid
	 * @param startdate
	 * @param enddate
	 * @param devicetypecd
	 * @return
	 * @throws Throwable
	 */
	public List<HashMap<String,Object>> getSensorStatisticList(HashMap<String,Object> map) throws Throwable;
	
	/**
	 * 채널개수
	 * @param userid
	 * @param startdate
	 * @param enddate
	 * @param devicetypecd
	 * @return
	 * @throws Throwable
	 */
	public int getChannelCnt(HashMap<String,Object> map) throws Throwable;
	
	
	
	
	
	
	// T10 통계 차트
	public List<HashMap<String, Object>> getT10DayStatisticList(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getT10MonthStatisticList(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getT10YearStatisticList(HashMap<String, Object> map) throws Throwable;
	
	// H10 통계 차트
	public List<HashMap<String, Object>> getH10DayStatisticList(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getH10MonthStatisticList(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getH10YearStatisticList(HashMap<String, Object> map) throws Throwable;

	// T10 통계 Grid
	public List<HashMap<String, Object>> getT10GridDayStatisticList(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getT10GridMonthStatisticList(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getT10GridYearStatisticList(HashMap<String, Object> map) throws Throwable;
	
	// H10 통계 Grid
	public List<HashMap<String, Object>> getH10GridDayStatisticList(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getH10GridMonthStatisticList(HashMap<String, Object> map) throws Throwable;

	public List<HashMap<String, Object>> getH10GridYearStatisticList(HashMap<String, Object> map) throws Throwable;
	
	
	
	
	
	
}
