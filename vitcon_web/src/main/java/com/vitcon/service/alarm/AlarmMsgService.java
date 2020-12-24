package com.vitcon.service.alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmMsgService implements AlarmMsgMapper {

	@Autowired
	private SqlSession sqlSession;
		
	@Override
	public List<HashMap<String,Object>> getAlarmMsg(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = (String)map.get("startdate") + " 00:00:00";
		String regdateEnd = (String)map.get("enddate") + " 23:59:00";
		
		Date date = format.parse(regdateStart);
		
		//date + 1일
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		 
		//String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);
		
		AlarmMsgMapper mapper = sqlSession.getMapper(AlarmMsgMapper.class);

		return mapper.getAlarmMsg(map);
	}

	@Override
	public List<HashMap<String, Object>> getAlarmMsgChildUser(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = (String)map.get("startdate") + " 00:00:00";
		String regdateEnd = (String)map.get("enddate") + " 23:59:00";
		
		Date date = format.parse(regdateStart);
		
		//date + 1일
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		 
		//String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);
		
		AlarmMsgMapper mapper = sqlSession.getMapper(AlarmMsgMapper.class);

		return mapper.getAlarmMsgChildUser(map);
	}

	
	
	
	
	
	
	
	public List<HashMap<String, Object>> getAlarmData(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = (String)map.get("startdate") + " 00:00:00";
		String regdateEnd = (String)map.get("enddate") + " 23:59:00";
		
		Date date = format.parse(regdateStart);
		
		//date + 1일
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		 
		//String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);
		
		AlarmMsgMapper mapper = sqlSession.getMapper(AlarmMsgMapper.class);

		return mapper.getAlarmData(map);
	}
	
	public List<HashMap<String, Object>> getAlarmNextData(HashMap<String, Object> map) throws Throwable {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = (String)map.get("startdate") + " 00:00:00";
		String regdateEnd = (String)map.get("enddate") + " 23:59:00";
		
		Date date = format.parse(regdateStart);
		
		//date + 1일
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		 
		//String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);
		
		AlarmMsgMapper mapper = sqlSession.getMapper(AlarmMsgMapper.class);

		return mapper.getAlarmNextData(map);
	}

	public List<HashMap<String, Object>> getAlarmPreData(HashMap<String, Object> map) throws Throwable {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = (String)map.get("startdate") + " 00:00:00";
		String regdateEnd = (String)map.get("enddate") + " 23:59:00";
		
		Date date = format.parse(regdateStart);
		
		//date + 1일
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		 
		//String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);
		
		AlarmMsgMapper mapper = sqlSession.getMapper(AlarmMsgMapper.class);

		return mapper.getAlarmPreData(map);
	}

	public List<HashMap<String, Object>> getAlarmHomeData(HashMap<String, Object> map) throws Throwable {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = (String)map.get("startdate") + " 00:00:00";
		String regdateEnd = (String)map.get("enddate") + " 23:59:00";
		
		Date date = format.parse(regdateStart);
		
		//date + 1일
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		 
		//String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);
		
		AlarmMsgMapper mapper = sqlSession.getMapper(AlarmMsgMapper.class);

		return mapper.getAlarmHomeData(map);
	}
	
	
	
	
	
	
	

	public List<HashMap<String, Object>> getAlarmDataMsgChildUser(HashMap<String, Object> map) throws Throwable {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = (String)map.get("startdate") + " 00:00:00";
		String regdateEnd = (String)map.get("enddate") + " 23:59:00";
		
		Date date = format.parse(regdateStart);
		
		//date + 1일
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		 
		//String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);
		
		AlarmMsgMapper mapper = sqlSession.getMapper(AlarmMsgMapper.class);

		return mapper.getAlarmDataMsgChildUser(map);
	}

	public List<HashMap<String, Object>> getAlarmNextMsgChildUser(HashMap<String, Object> map) throws Throwable {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = (String)map.get("startdate") + " 00:00:00";
		String regdateEnd = (String)map.get("enddate") + " 23:59:00";
		
		Date date = format.parse(regdateStart);
		
		//date + 1일
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		 
		//String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);
		
		AlarmMsgMapper mapper = sqlSession.getMapper(AlarmMsgMapper.class);

		return mapper.getAlarmNextMsgChildUser(map);
	}

	public List<HashMap<String, Object>> getAlarmPreMsgChildUser(HashMap<String, Object> map) throws Throwable {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = (String)map.get("startdate") + " 00:00:00";
		String regdateEnd = (String)map.get("enddate") + " 23:59:00";
		
		Date date = format.parse(regdateStart);
		
		//date + 1일
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		 
		//String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);
		
		AlarmMsgMapper mapper = sqlSession.getMapper(AlarmMsgMapper.class);

		return mapper.getAlarmPreMsgChildUser(map);
	}

	public List<HashMap<String, Object>> getAlarmHomeMsgChildUser(HashMap<String, Object> map) throws Throwable {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = (String)map.get("startdate") + " 00:00:00";
		String regdateEnd = (String)map.get("enddate") + " 23:59:00";
		
		Date date = format.parse(regdateStart);
		
		//date + 1일
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		 
		//String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);
		
		AlarmMsgMapper mapper = sqlSession.getMapper(AlarmMsgMapper.class);

		return mapper.getAlarmHomeMsgChildUser(map);
	}
	
	
}
