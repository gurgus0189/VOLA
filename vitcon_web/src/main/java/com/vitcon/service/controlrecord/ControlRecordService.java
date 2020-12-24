package com.vitcon.service.controlrecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ControlRecordService implements ControlRecordMapper{

	@Autowired
	SqlSession sqlSession;
	
	@Override
	public List<HashMap<String, Object>> getControlRecord(HashMap<String, Object> map) throws Throwable {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String regdateStart = map.get("regdate") + " 00:00:00";
		
		Date date = format.parse(regdateStart);
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		
		String regdateEnd = format.format(date);
		
		map.put("regdatestart", regdateStart);
		map.put("regdateend", regdateEnd);

		ControlRecordMapper mapper = sqlSession.getMapper(ControlRecordMapper.class);		
		return mapper.getControlRecord(map);
	}

}
