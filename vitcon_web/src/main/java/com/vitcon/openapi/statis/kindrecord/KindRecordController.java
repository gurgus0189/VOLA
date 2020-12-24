package com.vitcon.openapi.statis.kindrecord;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.kindrecord.KindRecordService;
import com.vitcon.service.kindrecord.StatisGridLink1VO;
import com.vitcon.service.kindrecord.StatisGridLink2VO;
import com.vitcon.service.kindrecord.StatisGridLink3VO;
import com.vitcon.service.kindrecord.StatisGridVO;
import com.vitcon.service.tui.grid.ResponseGridVO;

@RestController
@RequestMapping("/openapi/statis")
public class KindRecordController {

    private static final Logger logger = LoggerFactory.getLogger(KindRecordController.class);
    
	@Autowired
	private KindRecordService service;

	@InitBinder
	protected void initBinder(WebDataBinder binder){
		DateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,true));
	 }	
	
	@RequestMapping("/entire/datalist")
	public ResponseObject getEntireStatisticList(@RequestParam("startdate") Date startdate
                                               , @RequestParam("enddate") Date enddate
											   , @RequestParam("devicetypecd") Integer devicetypecd
												) {
		ResponseObject ret = new ResponseObject();
		
		if(startdate == null || enddate == null || devicetypecd == null ) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		HashMap<String,Object> map = new HashMap<String,Object>();

		UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return ret;
	    }
		
		map.put("userid", userid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("devicetypecd", devicetypecd);
		
		List<HashMap<String,Object>> list= null;

		try {
			list = service.getEntireStatisticList(map);			
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			e.printStackTrace();

			return ret;
		}
		ret.setReturnCode(StatusCode.OK);
		ret.setData(list);

		return ret;
	}
	
	@RequestMapping("/group/datalist")
	public ResponseObject getGroupStatisticList(@RequestParam("startdate") Date startdate
											   ,@RequestParam("enddate") Date enddate
											   ,@RequestParam("devicetypecd") Integer devicetypecd
											   ) {
		ResponseObject ret=new ResponseObject();
		int channelCnt = 0;
		
		if(startdate == null || enddate == null || devicetypecd == null ) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}
		
		HashMap<String,Object> map = new HashMap<String,Object>();

        UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return ret;
	    }
		
		map.put("userid", userid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("devicetypecd", devicetypecd);
		
		List<HashMap<String,Object>> list = null;
		
		try {
			list=service.getGroupStatisticList(map);
			channelCnt=service.getChannelCnt(map);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);			
			e.printStackTrace();

			return ret;
		}		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(list);
		ret.setCnt(channelCnt);

		return ret;
	}
	
	@RequestMapping("/sensor/datalist")
	public ResponseObject getSensorStatisticList(@RequestParam("startdate") Date startdate
											    ,@RequestParam("enddate") Date enddate
											    ,@RequestParam("devicetypecd") Integer devicetypecd) {
		ResponseObject ret = new ResponseObject();
		int channelCnt = 0;
		
		if(startdate == null || enddate == null || devicetypecd == null ) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		
        UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return ret;
	    }

		map.put("userid", userid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("devicetypecd", devicetypecd);
		
		List<HashMap<String,Object>> list = null;
		
		try {
			list = service.getSensorStatisticList(map);
			channelCnt=service.getChannelCnt(map);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			
			return ret;
		}
		
		ret.setData(list);
		ret.setReturnCode(StatusCode.OK);
		ret.setCnt(channelCnt);
		
		return ret;
	}
	
	
	
	
	
	
	@RequestMapping("/T10_statisticList")
	public ResponseObject getT10StatisticList(
			@RequestParam("deviceid") String deviceid,
			@RequestParam("startdate") Date startdate,
			@RequestParam("enddate") Date enddate,
			@RequestParam("datetypecd") String datetypecd,
			@RequestParam("arraySize") int arraySize ) {
		ResponseObject ret = new ResponseObject();
		
		if(deviceid == null || startdate == null || enddate == null || datetypecd == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		HashMap<String,Object> map = new HashMap<String,Object>();
		
		HashMap<String,HashMap<String,ArrayList<Object>>> channelMap = new HashMap<String,HashMap<String,ArrayList<Object>>>();
		HashMap<String, ArrayList<Object>> statsMap1 = new HashMap<String, ArrayList<Object>>();
		HashMap<String, ArrayList<Object>> statsMap2 = new HashMap<String, ArrayList<Object>>();
		HashMap<String, ArrayList<Object>> statsMap3 = new HashMap<String, ArrayList<Object>>();
		
		HashMap<String, String> emptyChk = new HashMap<String, String>();
		
		ArrayList<Object> minChannel1 = new ArrayList<Object>(arraySize);
		ArrayList<Object> avgChannel1 = new ArrayList<Object>(arraySize);
		ArrayList<Object> maxChannel1 = new ArrayList<Object>(arraySize);
		
		ArrayList<Object> minChannel2 = new ArrayList<Object>(arraySize);
		ArrayList<Object> avgChannel2 = new ArrayList<Object>(arraySize);
		ArrayList<Object> maxChannel2 = new ArrayList<Object>(arraySize);
		
		ArrayList<Object> minChannel3 = new ArrayList<Object>(arraySize);
		ArrayList<Object> avgChannel3 = new ArrayList<Object>(arraySize);
		ArrayList<Object> maxChannel3 = new ArrayList<Object>(arraySize);
		
		List<HashMap<String, ArrayList<Object>>> statsList = new ArrayList<HashMap<String, ArrayList<Object>>>();
		
		for(int i=0; i<arraySize; i++) {
			minChannel1.add(i,null);
			avgChannel1.add(i,null);
			maxChannel1.add(i,null);
			
			minChannel2.add(i,null);
			avgChannel2.add(i,null);
			maxChannel2.add(i,null);

			minChannel3.add(i,null);
			avgChannel3.add(i,null);
			maxChannel3.add(i,null);
			
		}
		
		UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return ret;
	    }
		
	    
		map.put("userid", userid);
		map.put("deviceid", deviceid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("datetypecd", datetypecd);
		
		List<HashMap<String, Object>> list = null;
		
		SimpleDateFormat format = null;
		
		try {
			
			list = service.getT10StatisticList(deviceid,startdate,enddate,datetypecd);
			
			if(datetypecd.equals("day")) {
				format = new SimpleDateFormat("H"); // 시간 으로 list 에 넣어줄 index 
			} else if(datetypecd.equals("month")) {
				format = new SimpleDateFormat("d");
			} else if(datetypecd.equals("year")) {
				format = new SimpleDateFormat("M");
			} 
			
			Iterator<HashMap<String, Object>> e2 = list.iterator();
			
			while (e2.hasNext()) {
				
				HashMap<String, Object> m = e2.next();
				
				int regdate = Integer.parseInt(format.format(m.get("regdate")));
				
				if(datetypecd.equals("month") || datetypecd.equals("year")) regdate = regdate-1;
				
				minChannel1.set(regdate, m.get("min_channel1"));
				avgChannel1.set(regdate, m.get("avg_channel1"));
				maxChannel1.set(regdate, m.get("max_channel1"));
				
				minChannel2.set(regdate, m.get("min_channel2"));
				avgChannel2.set(regdate, m.get("avg_channel2"));
				maxChannel2.set(regdate, m.get("max_channel2"));
				
				minChannel3.set(regdate, m.get("min_channel3"));
				avgChannel3.set(regdate, m.get("avg_channel3"));
				maxChannel3.set(regdate, m.get("max_channel3"));
				
				statsMap1.put("min1", minChannel1);
				statsMap1.put("avg1", avgChannel1);
				statsMap1.put("max1", maxChannel1);
				
				statsMap2.put("min2", minChannel2);
				statsMap2.put("avg2", avgChannel2);
				statsMap2.put("max2", maxChannel2);
				
				statsMap3.put("min3", minChannel3);
				statsMap3.put("avg3", avgChannel3);
				statsMap3.put("max3", maxChannel3);
				
				channelMap.put("channel1", statsMap1);
				channelMap.put("channel2", statsMap2);
				channelMap.put("channel3", statsMap3);
			
			}
			
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			e.printStackTrace();
			return ret;
		}
		
		if(channelMap.isEmpty()) {
			ret.setReturnCode(StatusCode.OK);
			ret.setData("null");
			return ret;
		} 
		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(channelMap);
		return ret;
	}
	
	
	
	
	
	
	@RequestMapping("/H10_statisticList")
	public ResponseObject getH10StatisticList(
			@RequestParam("deviceid") String deviceid,
			@RequestParam("startdate") Date startdate,
			@RequestParam("enddate") Date enddate,
			@RequestParam("datetypecd") String datetypecd,
			@RequestParam("arraySize") int arraySize ) {
		ResponseObject ret = new ResponseObject();
		
		if(deviceid == null || startdate == null || enddate == null || datetypecd == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		HashMap<String,Object> map = new HashMap<String,Object>();
		
		HashMap<String,HashMap<String,ArrayList<Object>>> channelMap = new HashMap<String,HashMap<String,ArrayList<Object>>>();
		HashMap<String, ArrayList<Object>> statsMap1 = new HashMap<String, ArrayList<Object>>();
		HashMap<String, ArrayList<Object>> statsMap2 = new HashMap<String, ArrayList<Object>>();
		HashMap<String, ArrayList<Object>> statsMap3 = new HashMap<String, ArrayList<Object>>();
		HashMap<String, ArrayList<Object>> statsMap4 = new HashMap<String, ArrayList<Object>>();
		HashMap<String, ArrayList<Object>> statsMap5 = new HashMap<String, ArrayList<Object>>();
		HashMap<String, ArrayList<Object>> statsMap6 = new HashMap<String, ArrayList<Object>>();
		
		ArrayList<Object> minChannel1 = new ArrayList<Object>(arraySize);
		ArrayList<Object> avgChannel1 = new ArrayList<Object>(arraySize);
		ArrayList<Object> maxChannel1 = new ArrayList<Object>(arraySize);
		
		ArrayList<Object> minChannel2 = new ArrayList<Object>(arraySize);
		ArrayList<Object> avgChannel2 = new ArrayList<Object>(arraySize);
		ArrayList<Object> maxChannel2 = new ArrayList<Object>(arraySize);
		
		ArrayList<Object> minChannel3 = new ArrayList<Object>(arraySize);
		ArrayList<Object> avgChannel3 = new ArrayList<Object>(arraySize);
		ArrayList<Object> maxChannel3 = new ArrayList<Object>(arraySize);
		
		ArrayList<Object> minChannel4 = new ArrayList<Object>(arraySize);
		ArrayList<Object> avgChannel4 = new ArrayList<Object>(arraySize);
		ArrayList<Object> maxChannel4 = new ArrayList<Object>(arraySize);
		
		ArrayList<Object> minChannel5 = new ArrayList<Object>(arraySize);
		ArrayList<Object> avgChannel5 = new ArrayList<Object>(arraySize);
		ArrayList<Object> maxChannel5 = new ArrayList<Object>(arraySize);
		
		ArrayList<Object> minChannel6 = new ArrayList<Object>(arraySize);
		ArrayList<Object> avgChannel6 = new ArrayList<Object>(arraySize);
		ArrayList<Object> maxChannel6 = new ArrayList<Object>(arraySize);
		
		List<HashMap<String, ArrayList<Object>>> statsList = new ArrayList<HashMap<String, ArrayList<Object>>>();
		
		for(int i=0; i<arraySize; i++) {
			minChannel1.add(i,null);
			avgChannel1.add(i,null);
			maxChannel1.add(i,null);
			
			minChannel2.add(i,null);
			avgChannel2.add(i,null);
			maxChannel2.add(i,null);

			minChannel3.add(i,null);
			avgChannel3.add(i,null);
			maxChannel3.add(i,null);
			
			minChannel4.add(i,null);
			avgChannel4.add(i,null);
			maxChannel4.add(i,null);
			
			minChannel5.add(i,null);
			avgChannel5.add(i,null);
			maxChannel5.add(i,null);
			
			minChannel6.add(i,null);
			avgChannel6.add(i,null);
			maxChannel6.add(i,null);
			
		}
		
		UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return ret;
	    }
		
	    
		map.put("userid", userid);
		map.put("deviceid", deviceid);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("datetypecd", datetypecd);
		
		List<HashMap<String, Object>> list = null;
		
		SimpleDateFormat format = null;
		
		try {
		
			list = service.getH10StatisticList(deviceid,startdate,enddate,datetypecd);
			
			if(datetypecd.equals("day")) {
				format = new SimpleDateFormat("H"); // 시간 으로 list 에 넣어줄 index 
			} else if(datetypecd.equals("month")) {
				format = new SimpleDateFormat("d");
			} else if(datetypecd.equals("year")) {
				format = new SimpleDateFormat("M");
			} 
			
			Iterator<HashMap<String, Object>> e2 = list.iterator();
			
			while (e2.hasNext()) {
				
				HashMap<String, Object> m = e2.next();
				
				int regdate = Integer.parseInt(format.format(m.get("regdate")));
				
				if(datetypecd.equals("month") || datetypecd.equals("year")) regdate = regdate-1;
				
				minChannel1.set(regdate, m.get("min_channel1"));
				avgChannel1.set(regdate, m.get("avg_channel1"));
				maxChannel1.set(regdate, m.get("max_channel1"));
				
				minChannel2.set(regdate, m.get("min_channel2"));
				avgChannel2.set(regdate, m.get("avg_channel2"));
				maxChannel2.set(regdate, m.get("max_channel2"));
				
				minChannel3.set(regdate, m.get("min_channel3"));
				avgChannel3.set(regdate, m.get("avg_channel3"));
				maxChannel3.set(regdate, m.get("max_channel3"));
				
				minChannel4.set(regdate, m.get("min_channel4"));
				avgChannel4.set(regdate, m.get("avg_channel4"));
				maxChannel4.set(regdate, m.get("max_channel4"));
				
				minChannel5.set(regdate, m.get("min_channel5"));
				avgChannel5.set(regdate, m.get("avg_channel5"));
				maxChannel5.set(regdate, m.get("max_channel5"));
				
				minChannel6.set(regdate, m.get("min_channel6"));
				avgChannel6.set(regdate, m.get("avg_channel6"));
				maxChannel6.set(regdate, m.get("max_channel6"));
				
				statsMap1.put("min1", minChannel1);
				statsMap1.put("avg1", avgChannel1);
				statsMap1.put("max1", maxChannel1);
				
				statsMap2.put("min2", minChannel2);
				statsMap2.put("avg2", avgChannel2);
				statsMap2.put("max2", maxChannel2);
				
				statsMap3.put("min3", minChannel3);
				statsMap3.put("avg3", avgChannel3);
				statsMap3.put("max3", maxChannel3);
				
				statsMap4.put("min4", minChannel4);
				statsMap4.put("avg4", avgChannel4);
				statsMap4.put("max4", maxChannel4);
				
				statsMap5.put("min5", minChannel5);
				statsMap5.put("avg5", avgChannel5);
				statsMap5.put("max5", maxChannel5);
				
				statsMap6.put("min6", minChannel6);
				statsMap6.put("avg6", avgChannel6);
				statsMap6.put("max6", maxChannel6);
				
				channelMap.put("channel1", statsMap1);
				channelMap.put("channel2", statsMap2);
				channelMap.put("channel3", statsMap3);
				channelMap.put("channel4", statsMap4);
				channelMap.put("channel5", statsMap5);
				channelMap.put("channel6", statsMap6);
			}
			
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			e.printStackTrace();
			return ret;
		}
		
		if(channelMap.isEmpty()) {
			ret.setReturnCode(StatusCode.OK);
			ret.setData("null");
			return ret;
		}
		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(channelMap);
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// T10 통계 그리드
	@RequestMapping("/T10_link1_grid_statisticList")
	public ResponseObject getT10Link1GridStatisticList(
			@RequestParam("deviceid") String deviceid,
			@RequestParam("startdate") Date startdate,
			@RequestParam("enddate") Date enddate,
			@RequestParam("datetypecd") String datetypecd,
			@RequestParam("channelid_start") int channelid_start ) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
		
		ResponseGridVO result = new ResponseGridVO();
		
		List<Object> link1 = new ArrayList<Object>();
		List<Object> link2 = new ArrayList<Object>();
		List<Object> link3 = new ArrayList<Object>();
		
		List<HashMap<String, Object>> list = null;
		
		list = service.getT10GridDayStatisticList(deviceid, startdate, enddate, datetypecd, channelid_start);

		
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		
		StatisGridVO vo = new StatisGridVO();
		
		HashMap<String, List<Object>> gridMap = new HashMap<String, List<Object>>();
		
		
		DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = new Timestamp(System.currentTimeMillis());  
		
		while (e2.hasNext()) {
			StatisGridLink1VO linkVO1 = new StatisGridLink1VO();
			StatisGridLink2VO linkVO2 = new StatisGridLink2VO();
			StatisGridLink3VO linkVO3 = new StatisGridLink3VO();
			
			HashMap<String, Object> m = e2.next();
			
			Date date = ts;
			date = (Date) m.get("regdate");
			
			String regdate_channel1 = dateFormat.format(date);
			Double avg_channel1 = (Double) m.get("avg_channel1");
			Double max_channel1 = (Double) m.get("max_channel1");
			Double min_channel1 = (Double) m.get("min_channel1");
		
			linkVO1.setRegdateLink1(regdate_channel1);
			linkVO1.setTempAvgLink1(avg_channel1);
			linkVO1.setTempMaxLink1(max_channel1);
			linkVO1.setTempMinLink1(min_channel1);
		
			link1.add(linkVO1);
			
			gridMap.put("link1", link1);
			
		}
		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(gridMap);
		return ret;
		
	}
	
	@RequestMapping("/T10_link2_grid_statisticList")
	public ResponseObject getT10Link2GridStatisticList(
			@RequestParam("deviceid") String deviceid,
			@RequestParam("startdate") Date startdate,
			@RequestParam("enddate") Date enddate,
			@RequestParam("datetypecd") String datetypecd,
			@RequestParam("channelid_start") int channelid_start ) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
		
		
		ResponseGridVO result = new ResponseGridVO();
		
		
		List<Object> link2 = new ArrayList<Object>();
	
		
		List<HashMap<String, Object>> list = null;
		
		list = service.getT10GridDayStatisticList(deviceid, startdate, enddate, datetypecd, channelid_start);
	
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		
		StatisGridVO vo = new StatisGridVO();
		
		HashMap<String, List<Object>> gridMap = new HashMap<String, List<Object>>();
		
		DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = new Timestamp(System.currentTimeMillis());  
		
		while (e2.hasNext()) {
		
			StatisGridLink2VO linkVO2 = new StatisGridLink2VO();
		
			
			HashMap<String, Object> m = e2.next();
			
			Date date = ts;
			date = (Date) m.get("regdate");
			
			String regdate_channel2 = dateFormat.format(date);
			Double avg_channel2 = (Double) m.get("avg_channel2");
			Double max_channel2 = (Double) m.get("max_channel2");
			Double min_channel2 = (Double) m.get("min_channel2");
			
			linkVO2.setRegdateLink2(regdate_channel2);
			linkVO2.setTempAvgLink2(avg_channel2);
			linkVO2.setTempMaxLink2(max_channel2);
			linkVO2.setTempMinLink2(min_channel2);
			
			link2.add(linkVO2);
			
			gridMap.put("link2", link2);
			
		}
		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(gridMap);
		return ret;
		
	}
	
	
	@RequestMapping("/T10_link3_grid_statisticList")
	public ResponseObject getT10Link3GridStatisticList(
			@RequestParam("deviceid") String deviceid,
			@RequestParam("startdate") Date startdate,
			@RequestParam("enddate") Date enddate,
			@RequestParam("datetypecd") String datetypecd,
			@RequestParam("channelid_start") int channelid_start ) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
		
		
		ResponseGridVO result = new ResponseGridVO();
		
		
		List<Object> link3 = new ArrayList<Object>();
	
		
		List<HashMap<String, Object>> list = null;
		
		list = service.getT10GridDayStatisticList(deviceid, startdate, enddate, datetypecd, channelid_start);
	
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		
		StatisGridVO vo = new StatisGridVO();
		
		HashMap<String, List<Object>> gridMap = new HashMap<String, List<Object>>();
		
		DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = new Timestamp(System.currentTimeMillis());  
		
		while (e2.hasNext()) {
		
			StatisGridLink3VO linkVO3 = new StatisGridLink3VO();
		
			HashMap<String, Object> m = e2.next();
			
			Date date = ts;
			date = (Date) m.get("regdate");
			
			String regdate_channel3 = dateFormat.format(date);
			Double avg_channel3 = (Double) m.get("avg_channel3");
			Double max_channel3 = (Double) m.get("max_channel3");
			Double min_channel3 = (Double) m.get("min_channel3");
			
			linkVO3.setRegdateLink3(regdate_channel3);
			linkVO3.setTempAvgLink3(avg_channel3);
			linkVO3.setTempMaxLink3(max_channel3);
			linkVO3.setTempMinLink3(min_channel3);
		
			link3.add(linkVO3);
			
			gridMap.put("link3", link3);
			
		}
		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(gridMap);
		return ret;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// H10 통계 그리드
	@RequestMapping("/H10_link1_grid_statisticList")
	public ResponseObject getH10Link1GridStatisticList(
			@RequestParam("deviceid") String deviceid,
			@RequestParam("startdate") Date startdate,
			@RequestParam("enddate") Date enddate,
			@RequestParam("datetypecd") String datetypecd,
			@RequestParam("channelid_start") int channelid_start,
			@RequestParam("channelid_end") int channelid_end) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
		
		ResponseGridVO result = new ResponseGridVO();
		
		List<Object> link1 = new ArrayList<Object>();
		List<Object> link2 = new ArrayList<Object>();
		List<Object> link3 = new ArrayList<Object>();
		
		List<HashMap<String, Object>> list = null;
		
		list = service.getH10GridDayStatisticList(deviceid, startdate, enddate, datetypecd, channelid_start, channelid_end);

		
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		
		StatisGridVO vo = new StatisGridVO();
		
		HashMap<String, List<Object>> gridMap = new HashMap<String, List<Object>>();
		
		
		DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = new Timestamp(System.currentTimeMillis());  
		
		while (e2.hasNext()) {
			StatisGridLink1VO linkVO1 = new StatisGridLink1VO();
			StatisGridLink2VO linkVO2 = new StatisGridLink2VO();
			StatisGridLink3VO linkVO3 = new StatisGridLink3VO();
			
			HashMap<String, Object> m = e2.next();
			
			Date date = ts;
			date = (Date) m.get("regdate");
			
			String regdate_channel1 = dateFormat.format(date);
			Double avg_channel1 = (Double) m.get("avg_channel1");
			Double max_channel1 = (Double) m.get("max_channel1");
			Double min_channel1 = (Double) m.get("min_channel1");
			
			Double avg_channel2 = (Double) m.get("avg_channel2");
			Double max_channel2 = (Double) m.get("max_channel2");
			Double min_channel2 = (Double) m.get("min_channel2");
			
		/*
			Double avg_channel3 = (Double) m.get("avg_channel3");
			Double max_channel3 = (Double) m.get("max_channel3");
			Double min_channel3 = (Double) m.get("min_channel3");
			
			Double avg_channel4 = (Double) m.get("avg_channel4");
			Double max_channel4 = (Double) m.get("max_channel4");
			Double min_channel4 = (Double) m.get("min_channel4");
	
			Double avg_channel5 = (Double) m.get("avg_channel5");
			Double max_channel5 = (Double) m.get("max_channel5");
			Double min_channel5 = (Double) m.get("min_channel5");
			
			Double avg_channel6 = (Double) m.get("avg_channel6");
			Double max_channel6 = (Double) m.get("max_channel6");
			Double min_channel6 = (Double) m.get("min_channel6");*/
			
			
			
			linkVO1.setRegdateLink1(regdate_channel1);
			linkVO1.setTempAvgLink1(avg_channel1);
			linkVO1.setTempMaxLink1(max_channel1);
			linkVO1.setTempMinLink1(min_channel1);
			linkVO1.setHumAvgLink1(avg_channel2);
			linkVO1.setHumMaxLink1(max_channel2);
			linkVO1.setHumMinLink1(min_channel2);
			
			//linkVO2.setRegdateLink2(regdate_channel3);
			/*linkVO2.setTempAvgLink2(avg_channel3);
			linkVO2.setTempMaxLink2(max_channel3);
			linkVO2.setTempMinLink2(min_channel3);
			linkVO2.setHumAvgLink2(avg_channel4);
			linkVO2.setHumMaxLink2(max_channel4);
			linkVO2.setHumMinLink2(min_channel4);
			
			//linkVO3.setRegdateLink3(regdate_channel5);
			linkVO3.setTempAvgLink3(avg_channel5);
			linkVO3.setTempMaxLink3(max_channel5);
			linkVO3.setTempMinLink3(min_channel5);
			linkVO3.setHumAvgLink3(avg_channel6);
			linkVO3.setHumMaxLink3(max_channel6);
			linkVO3.setHumMinLink3(min_channel6);*/
			
			link1.add(linkVO1);
			//link2.add(linkVO2);
			//link3.add(linkVO3);
			
			gridMap.put("link1", link1);
			//gridMap.put("link2", link2);
			//gridMap.put("link3", link3);
			
		}
		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(gridMap);
		return ret;
		
	}
	
	@RequestMapping("/H10_link2_grid_statisticList")
	public ResponseObject getH10Link2GridStatisticList(
			@RequestParam("deviceid") String deviceid,
			@RequestParam("startdate") Date startdate,
			@RequestParam("enddate") Date enddate,
			@RequestParam("datetypecd") String datetypecd,
			@RequestParam("channelid_start") int channelid_start,
			@RequestParam("channelid_end") int channelid_end) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
		
		
		ResponseGridVO result = new ResponseGridVO();
		
		
		List<Object> link2 = new ArrayList<Object>();
	
		
		List<HashMap<String, Object>> list = null;
		
		list = service.getH10GridDayStatisticList(deviceid, startdate, enddate, datetypecd, channelid_start, channelid_end);
	
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		
		StatisGridVO vo = new StatisGridVO();
		
		HashMap<String, List<Object>> gridMap = new HashMap<String, List<Object>>();
		
		/*if(datetypecd.equals("day")) {
			list = service.getH10GridStatisticList(map);
			System.out.println(list);
		} else if(datetypecd.equals("month")) {
			list = service.getDayGridStatisticList(map);
		} else if(datetypecd.equals("year")) {
			list = service.getMonthGridStatisticList(map);
		} else if(datetypecd.equals("year")) {
			list = service.getYearGridStatisticList(map);
		}*/
		
		DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = new Timestamp(System.currentTimeMillis());  
		
		while (e2.hasNext()) {
		
			StatisGridLink2VO linkVO2 = new StatisGridLink2VO();
		
			
			HashMap<String, Object> m = e2.next();
			
			Date date = ts;
			date = (Date) m.get("regdate");
			
			
			
			String regdate_channel3 = dateFormat.format(date);
			Double avg_channel3 = (Double) m.get("avg_channel3");
			Double max_channel3 = (Double) m.get("max_channel3");
			Double min_channel3 = (Double) m.get("min_channel3");
			
			Double avg_channel4 = (Double) m.get("avg_channel4");
			Double max_channel4 = (Double) m.get("max_channel4");
			Double min_channel4 = (Double) m.get("min_channel4");
			
			
			
			linkVO2.setRegdateLink2(regdate_channel3);
			linkVO2.setTempAvgLink2(avg_channel3);
			linkVO2.setTempMaxLink2(max_channel3);
			linkVO2.setTempMinLink2(min_channel3);
			linkVO2.setHumAvgLink2(avg_channel4);
			linkVO2.setHumMaxLink2(max_channel4);
			linkVO2.setHumMinLink2(min_channel4);
			
		
			link2.add(linkVO2);
			
			gridMap.put("link2", link2);
			
		}
		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(gridMap);
		return ret;
		
	}
	
	
	@RequestMapping("/H10_link3_grid_statisticList")
	public ResponseObject getH10Link3GridStatisticList(
			@RequestParam("deviceid") String deviceid,
			@RequestParam("startdate") Date startdate,
			@RequestParam("enddate") Date enddate,
			@RequestParam("datetypecd") String datetypecd,
			@RequestParam("channelid_start") int channelid_start,
			@RequestParam("channelid_end") int channelid_end) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
		
		
		ResponseGridVO result = new ResponseGridVO();
		
		
		List<Object> link3 = new ArrayList<Object>();
	
		
		List<HashMap<String, Object>> list = null;
		
		list = service.getH10GridDayStatisticList(deviceid, startdate, enddate, datetypecd, channelid_start, channelid_end);
	
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		
		StatisGridVO vo = new StatisGridVO();
		
		HashMap<String, List<Object>> gridMap = new HashMap<String, List<Object>>();
		
		DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = new Timestamp(System.currentTimeMillis());  
		
		while (e2.hasNext()) {
		
			StatisGridLink3VO linkVO3 = new StatisGridLink3VO();
		
			HashMap<String, Object> m = e2.next();
			
			Date date = ts;
			date = (Date) m.get("regdate");
			
			String regdate_channel5 = dateFormat.format(date);
			Double avg_channel5 = (Double) m.get("avg_channel5");
			Double max_channel5 = (Double) m.get("max_channel5");
			Double min_channel5 = (Double) m.get("min_channel5");
			
			Double avg_channel6 = (Double) m.get("avg_channel6");
			Double max_channel6 = (Double) m.get("max_channel6");
			Double min_channel6 = (Double) m.get("min_channel6");
			
			linkVO3.setRegdateLink3(regdate_channel5);
			linkVO3.setTempAvgLink3(avg_channel5);
			linkVO3.setTempMaxLink3(max_channel5);
			linkVO3.setTempMinLink3(min_channel5);
			linkVO3.setHumAvgLink3(avg_channel6);
			linkVO3.setHumMaxLink3(max_channel6);
			linkVO3.setHumMinLink3(min_channel6);
		
			link3.add(linkVO3);
			
			gridMap.put("link3", link3);
			
		}
		
		ret.setReturnCode(StatusCode.OK);
		ret.setData(gridMap);
		return ret;
		
	}
	
	
	
	
	
	
	
	
	
	
}
