package com.vitcon.service.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class GraphService implements GraphMapper {

	@Autowired
	private GraphMapper mapper;
	
	@Autowired
	MessageSource messageSource;
	
	@Override
	public List<Map<String, Object>> getGraphData(HashMap<String, Object> map) throws Throwable {
		return mapper.getGraphData(map);
	}

	
}
