package com.vitcon.service.kindrecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class KindRecordService implements KindRecordMapper {

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<HashMap<String,Object>> getEntireStatisticList(HashMap<String,Object> map) throws Throwable {
		// TODO Auto-generated method stub
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getEntireStatisticList(map);
	}

	@Override
	public List<HashMap<String,Object>> getGroupStatisticList(HashMap<String,Object> map) throws Throwable {
		// TODO Auto-generated method stub
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getGroupStatisticList(map);
	}

	@Override
	public List<HashMap<String,Object>> getSensorStatisticList(HashMap<String,Object> map) throws Throwable {
		// TODO Auto-generated method stub
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getSensorStatisticList(map);
	}
	
	@Override
	public int getChannelCnt(HashMap<String,Object> map) throws Throwable {
		// TODO Auto-generated method stub
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getChannelCnt(map);
	}
	
	
	// 일별 통계
	public List<HashMap<String, Object>> getT10DayStatisticList(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getT10DayStatisticList(map);
	}
	
	// 월별 통계
	public List<HashMap<String, Object>> getT10MonthStatisticList(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getT10MonthStatisticList(map);
	}
	
	// 년별 통계
	public List<HashMap<String, Object>> getT10YearStatisticList(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getT10YearStatisticList(map);
	}
	
	public List<HashMap<String, Object>> getT10StatisticList(String deviceid, Date startdate, Date enddate, String datetypecd) throws Throwable {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		
		map.put("deviceid", deviceid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);

		if(datetypecd.equals("day")) {
			list = getT10DayStatisticList(map);
		} else if (datetypecd.equals("month")) {
			list = getT10MonthStatisticList(map);
		} else if (datetypecd.equals("year")) {
			list = getT10YearStatisticList(map);
		}
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			
			String minData = (String) ohm.get("min");
			String avgData = (String) ohm.get("avg");
			String maxData = (String) ohm.get("max");
			
			String[] channelidList = null;
			String[] minList = null;
			String[] avgList = null;
			String[] maxList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (minData != null)
				minList = minData.split("\\|");
			
			if (avgData != null)
				avgList = avgData.split("\\|");
			
			if (maxData != null)
				maxList = maxData.split("\\|");
			
			HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
			
			if (channelidList != null && minList != null && avgList != null && maxList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					
					nhm.put("min_channel"+channelidList[i], Double.parseDouble(minList[i]));
					nhm.put("avg_channel"+channelidList[i], Double.parseDouble(avgList[i]));
					nhm.put("max_channel"+channelidList[i], Double.parseDouble(maxList[i]));
					
				}
			}
			
			nhm.put("regdate", ohm.get("regdate"));
		
			channelDataMap.add(nhm);			
		
		}
				
		return channelDataMap;
	}
	
	// 일별 통계
	public List<HashMap<String, Object>> getH10DayStatisticList(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getH10DayStatisticList(map);
	}
	
	// 월별 통계
	public List<HashMap<String, Object>> getH10MonthStatisticList(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getH10MonthStatisticList(map);
	}
	
	// 년별 통계
	public List<HashMap<String, Object>> getH10YearStatisticList(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getH10YearStatisticList(map);
	}
	
	public List<HashMap<String, Object>> getH10StatisticList(String deviceid, Date startdate, Date enddate, String datetypecd) throws Throwable {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		
		map.put("deviceid", deviceid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
	    
		if(datetypecd.equals("day")) {
			list = getH10DayStatisticList(map);
		} else if (datetypecd.equals("month")) {
			list = getH10MonthStatisticList(map);
		} else if (datetypecd.equals("year")) {
			list = getH10YearStatisticList(map);
		}
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			
			String minData = (String) ohm.get("min");
			String avgData = (String) ohm.get("avg");
			String maxData = (String) ohm.get("max");
			
			String[] channelidList = null;
			String[] minList = null;
			String[] avgList = null;
			String[] maxList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (minData != null)
				minList = minData.split("\\|");
			
			if (avgData != null)
				avgList = avgData.split("\\|");
			
			if (maxData != null)
				maxList = maxData.split("\\|");
			
			HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
			
			if (channelidList != null && minList != null && avgList != null && maxList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					
					nhm.put("min_channel"+channelidList[i], Double.parseDouble(minList[i]));
					nhm.put("avg_channel"+channelidList[i], Double.parseDouble(avgList[i]));
					nhm.put("max_channel"+channelidList[i], Double.parseDouble(maxList[i]));
					
				}
			}
			
			nhm.put("regdate", ohm.get("regdate"));
		
			channelDataMap.add(nhm);			
		
		}
				
		return channelDataMap;
	}

	@Override
	public List<HashMap<String, Object>> getT10GridDayStatisticList(HashMap<String, Object> map) throws Throwable {
		
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getT10GridDayStatisticList(map);
	}
	
	@Override
	public List<HashMap<String, Object>> getT10GridMonthStatisticList(HashMap<String, Object> map) throws Throwable {
		
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getT10GridMonthStatisticList(map);
	}
	
	@Override
	public List<HashMap<String, Object>> getT10GridYearStatisticList(HashMap<String, Object> map) throws Throwable {
		
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getT10GridYearStatisticList(map);
	}

	public List<HashMap<String, Object>> getT10GridDayStatisticList(String deviceid, Date startdate, Date enddate, String datetypecd, int channelid_start) throws Throwable {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		
		map.put("deviceid", deviceid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("datetypecd", datetypecd);
		map.put("channelid_start", channelid_start);
		
		if(datetypecd.equals("day")) {
			list = getT10GridDayStatisticList(map);
		} else if (datetypecd.equals("month")) {
			list = getT10GridMonthStatisticList(map);
		} else if (datetypecd.equals("year")) {
			list = getT10GridYearStatisticList(map);
		}
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			
			String minData = (String) ohm.get("min");
			String avgData = (String) ohm.get("avg");
			String maxData = (String) ohm.get("max");
			
			String[] channelidList = null;
			String[] minList = null;
			String[] avgList = null;
			String[] maxList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (minData != null)
				minList = minData.split("\\|");
			
			if (avgData != null)
				avgList = avgData.split("\\|");
			
			if (maxData != null)
				maxList = maxData.split("\\|");
			
			
			//HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
			
			if (channelidList != null && minList != null && avgList != null && maxList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					
					nhm.put("min_channel"+channelidList[i], Double.parseDouble(minList[i]));
					nhm.put("avg_channel"+channelidList[i], Double.parseDouble(avgList[i]));
					nhm.put("max_channel"+channelidList[i], Double.parseDouble(maxList[i]));
					
				}
			}
			
			nhm.put("regdate", ohm.get("regdate"));
		
			channelDataMap.add(nhm);			
		
		}
				
		return channelDataMap;
	}
	
	@Override
	public List<HashMap<String, Object>> getH10GridDayStatisticList(HashMap<String, Object> map) throws Throwable {
		
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getH10GridDayStatisticList(map);
	}
	
	@Override
	public List<HashMap<String, Object>> getH10GridMonthStatisticList(HashMap<String, Object> map) throws Throwable {
		
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getH10GridMonthStatisticList(map);
	}
	
	@Override
	public List<HashMap<String, Object>> getH10GridYearStatisticList(HashMap<String, Object> map) throws Throwable {
		
		KindRecordMapper mapper = sqlSession.getMapper(KindRecordMapper.class);
		
		return mapper.getH10GridYearStatisticList(map);
	}

	public List<HashMap<String, Object>> getH10GridDayStatisticList(String deviceid, Date startdate, Date enddate, String datetypecd, int channelid_start, int channelid_end) throws Throwable {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		
		map.put("deviceid", deviceid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("datetypecd", datetypecd);
		map.put("channelid_start", channelid_start);
		map.put("channelid_end", channelid_end);
		
		if(datetypecd.equals("day")) {
			list = getH10GridDayStatisticList(map);
		} else if (datetypecd.equals("month")) {
			list = getH10GridMonthStatisticList(map);
		} else if (datetypecd.equals("year")) {
			list = getH10GridYearStatisticList(map);
		}
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			
			String minData = (String) ohm.get("min");
			String avgData = (String) ohm.get("avg");
			String maxData = (String) ohm.get("max");
			
			String[] channelidList = null;
			String[] minList = null;
			String[] avgList = null;
			String[] maxList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (minData != null)
				minList = minData.split("\\|");
			
			if (avgData != null)
				avgList = avgData.split("\\|");
			
			if (maxData != null)
				maxList = maxData.split("\\|");
			
			
			//HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
			
			if (channelidList != null && minList != null && avgList != null && maxList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					
					nhm.put("min_channel"+channelidList[i], Double.parseDouble(minList[i]));
					nhm.put("avg_channel"+channelidList[i], Double.parseDouble(avgList[i]));
					nhm.put("max_channel"+channelidList[i], Double.parseDouble(maxList[i]));
					
				}
			}
			
			nhm.put("regdate", ohm.get("regdate"));
		
			channelDataMap.add(nhm);			
		
		}
				
		return channelDataMap;
	}

	public List<HashMap<String, Object>> getT10StatisticExcelList(String deviceid, String startdate, String enddate, String datetypecd, Integer channelid_start) throws Throwable {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
	
		map.put("deviceid", deviceid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("datetypecd", datetypecd);
		map.put("channelid_start", channelid_start);
		
		if(datetypecd.equals("day")) {
			list = getT10GridDayStatisticList(map);
		} else if (datetypecd.equals("month")) {
			list = getT10GridMonthStatisticList(map);
		} else if (datetypecd.equals("year")) {
			list = getT10GridYearStatisticList(map);
		}
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			
			String minData = (String) ohm.get("min");
			String avgData = (String) ohm.get("avg");
			String maxData = (String) ohm.get("max");
			
			String[] channelidList = null;
			String[] minList = null;
			String[] avgList = null;
			String[] maxList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (minData != null)
				minList = minData.split("\\|");
			
			if (avgData != null)
				avgList = avgData.split("\\|");
			
			if (maxData != null)
				maxList = maxData.split("\\|");
			
			
			//HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
			
			if (channelidList != null && minList != null && avgList != null && maxList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					
					nhm.put("min_channel"+channelidList[i], Double.parseDouble(minList[i]));
					nhm.put("avg_channel"+channelidList[i], Double.parseDouble(avgList[i]));
					nhm.put("max_channel"+channelidList[i], Double.parseDouble(maxList[i]));
					
				}
			}
			
			nhm.put("regdate", ohm.get("regdate"));
		
			channelDataMap.add(nhm);			
		
		}
		
		
		return channelDataMap;
	}
	
	public List<HashMap<String, Object>> getH10StatisticExcelList(String deviceid, String startdate, String enddate, String datetypecd, Integer channelid_start, Integer channelid_end) throws Throwable {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
	
		map.put("deviceid", deviceid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("datetypecd", datetypecd);
		map.put("channelid_start", channelid_start);
		map.put("channelid_end", channelid_end);
		
		if(datetypecd.equals("day")) {
			list = getH10GridDayStatisticList(map);
		} else if (datetypecd.equals("month")) {
			list = getH10GridMonthStatisticList(map);
		} else if (datetypecd.equals("year")) {
			list = getH10GridYearStatisticList(map);
		}
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			
			String minData = (String) ohm.get("min");
			String avgData = (String) ohm.get("avg");
			String maxData = (String) ohm.get("max");
			
			String[] channelidList = null;
			String[] minList = null;
			String[] avgList = null;
			String[] maxList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (minData != null)
				minList = minData.split("\\|");
			
			if (avgData != null)
				avgList = avgData.split("\\|");
			
			if (maxData != null)
				maxList = maxData.split("\\|");
			
			
			//HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
			
			if (channelidList != null && minList != null && avgList != null && maxList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					
					nhm.put("min_channel"+channelidList[i], Double.parseDouble(minList[i]));
					nhm.put("avg_channel"+channelidList[i], Double.parseDouble(avgList[i]));
					nhm.put("max_channel"+channelidList[i], Double.parseDouble(maxList[i]));
					
				}
			}
			
			nhm.put("regdate", ohm.get("regdate"));
		
			channelDataMap.add(nhm);			
		
		}
		
		
		return channelDataMap;
	}
	
	
	

}
