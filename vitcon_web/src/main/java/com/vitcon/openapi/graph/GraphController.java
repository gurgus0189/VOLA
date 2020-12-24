package com.vitcon.openapi.graph;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.device.DeviceVO;
import com.vitcon.service.graph.GraphMapper;
import com.vitcon.service.graph.GraphService;

@RestController
@RequestMapping("/openapi/graph/")
public class GraphController {

	@Autowired
	private GraphService graphService;
	
	@Autowired
	GraphMapper graphMapper;
	
	@RequestMapping("list")
	public ResponseObject getGraphData (HashMap<String, Object> hashMap,
			@RequestParam("deviceid") String deviceid,
			@RequestParam("channelids") String channelids,
			@RequestParam("startdate") String startdate, 
			@RequestParam("enddate") String enddate) throws Throwable {
		ResponseObject ret = new ResponseObject();
		
		// 시간이 넘어오지 않았을 경우
		if (startdate == null && enddate == null) {
			Date date = new Date();
			String fmt = "yyyy-MM-dd HH:mm:ss";		
			Calendar cal = new GregorianCalendar();
			cal.add(Calendar.DATE, -1);
					
			//어제 시간
			startdate = new SimpleDateFormat(fmt).format(cal.getTime());
			
			//지금 시간
			enddate = new SimpleDateFormat(fmt).format(date);
		}
		
		String ids = channelids.substring(1,channelids.length());
		
		String[] id = ids.split(",");
		
		HashMap<Integer, ArrayList<Object[]>> statsMap = new HashMap<Integer, ArrayList<Object[]>>();
		for(int i=0; i<id.length; i++) {
			
			hashMap.put("deviceid", deviceid);
			hashMap.put("channelid", id[i]);
			hashMap.put("startdate", startdate);
			hashMap.put("enddate", enddate);
			
			List<Map<String,Object>> list = null;
			
			list = graphService.getGraphData(hashMap);
			Iterator<Map<String, Object>> e2 = list.iterator();
			ArrayList<Object[]> arraylist = new ArrayList<Object[]>();
			
			while (e2.hasNext()) {
				Map<String, Object> m = e2.next();
				
				Date regdate = (Date) m.get("regdate");
				Double data = (Double) m.get("data");
	
				arraylist.add(new Object[] {regdate,data});
				
			}
			statsMap.put(i, arraylist);
			
		}
		
		ret.setData(statsMap);
		ret.setReturnCode(StatusCode.OK);
		return ret;
		
	}
	
	
}
