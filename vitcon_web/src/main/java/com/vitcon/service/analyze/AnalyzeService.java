package com.vitcon.service.analyze;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vitcon.service.device.StatisVO;

@Service
public class AnalyzeService implements AnalyzeMapper {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzeService.class);
	
    @Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<HashMap<String,Object>> getProblemDeviceList(String userid) throws Throwable {
		// TODO Auto-generated method stub
		AnalyzeMapper mapper = sqlSession.getMapper(AnalyzeMapper.class);
		
		return mapper.getProblemDeviceList(userid);
	}

	@Override
	public List<HashMap<String,Object>> getOldDeviceList(String userid) throws Throwable {
		// TODO Auto-generated method stub
		AnalyzeMapper mapper = sqlSession.getMapper(AnalyzeMapper.class);	
		
		return mapper.getOldDeviceList(userid);
	}

}
