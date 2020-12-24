package com.vitcon.service.pastrecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.vitcon.service.device.DeviceVO;

@Service
public class PastRecordService implements PastRecordMapper {

    private static final Logger logger = LoggerFactory.getLogger(PastRecordService.class);
    
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	MessageSource messageSource;

	/* 날짜별 과거 데이터 조회 */
	@Override
	public List<HashMap<String, Object>> getTimeRecodList(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		PastRecordMapper mapper = sqlSession.getMapper(PastRecordMapper.class);

		return mapper.getTimeRecodList(map);
	}
			
	/* 날짜별 과거 데이터 조회 */
	public HashMap<String, Object> getTimeRecodList(DeviceVO vo, String startdate, String enddate, String userid) throws Throwable {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		HashMap<String, Object> ret = new HashMap<String, Object>();
		
		map.put("deviceid", vo.getDeviceid());
		map.put("groupid", vo.getGroupid());
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("userid", userid);		
		list = getTimeRecodList(map);
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			String devicetypecd = String.valueOf((Integer) ohm.get("devicetypecd"));
			String data = (String) ohm.get("data");
			String[] channelidList = null;
			String[] dataList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (data != null)
				dataList = data.split("\\|");
			
			HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
						
			if (channelidList != null && dataList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					String channelName = ""; //다국어로 설정해야 함
					
					// 샘플데이터
					channelName = "channel.code_" + devicetypecd + "_" + channelidList[i];										
					channelName = messageSource.getMessage(channelName, null, LocaleContextHolder.getLocale());
					
					dataMap.put(channelidList[i], dataList[i]);
					if (channelNameMap.get(channelidList[i]) == null)
						channelNameMap.put(channelidList[i], channelName);
				}
			}
			
			nhm.put("regdate", ohm.get("regdate"));
			nhm.put("regdateTime", ohm.get("regdateTime"));
			nhm.put("devicename", ohm.get("devicename"));
			nhm.put("groupname", ohm.get("groupname"));
			
			nhm.put("data", dataMap);			
			channelDataMap.add(nhm);			
		}
		
		ret.put("channelname", channelNameMap);
		ret.put("channeldata", channelDataMap);
				
		return ret;
	}

	@Override
	public List<HashMap<String, Object>> getTimeRecodGridList(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		PastRecordMapper mapper = sqlSession.getMapper(PastRecordMapper.class);

		return mapper.getTimeRecodGridList(map);
	}
			
	/* 날짜별 과거 데이터 조회 */
	public List<HashMap<String, Object>> getTimeRecodGridList(DeviceVO vo, String startdate, String enddate, String userid, int page, int perPage) throws Throwable {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		
		map.put("deviceid", vo.getDeviceid());
		map.put("groupid", vo.getGroupid());
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("userid", userid);
		map.put("page", page);
		map.put("perPage", perPage);
		
		list = getTimeRecodGridList(map);
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			String devicetypecd = String.valueOf((Integer) ohm.get("devicetypecd"));
			String data = (String) ohm.get("data");
			String[] channelidList = null;
			String[] dataList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (data != null)
				dataList = data.split("\\|");
			
			HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
						
			/*if (channelidList != null && dataList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					String channelName = ""; //다국어로 설정해야 함
					
					// 샘플데이터
					channelName = "channel.code_" + devicetypecd + "_" + channelidList[i];										
					channelName = messageSource.getMessage(channelName, null, LocaleContextHolder.getLocale());
					
					dataMap.put(channelidList[i], dataList[i]);
					if (channelNameMap.get(channelidList[i]) == null)
						channelNameMap.put(channelidList[i], channelName);
				}
			}*/
			
			if (channelidList != null && dataList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					String channelName = ""; //다국어로 설정해야 함
					nhm.put("channel"+channelidList[i], dataList[i]);
				}
			}
			
			nhm.put("regdate", ohm.get("regdate"));
			nhm.put("regdateTime", ohm.get("regdateTime"));
			channelDataMap.add(nhm);			
		
		}
				
		return channelDataMap;
	}

	
	@Override
	public List<HashMap<String, Object>> getDashBoardDetailGridlist(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		PastRecordMapper mapper = sqlSession.getMapper(PastRecordMapper.class);

		return mapper.getDashBoardDetailGridlist(map);
	}
			
	/* 날짜별 과거 데이터 조회 */
	public List<HashMap<String, Object>> getDashBoardDetailGridlist(DeviceVO vo, String startdate, String enddate, String userid, int page, int perPage) throws Throwable {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		
		map.put("deviceid", vo.getDeviceid());
		map.put("groupid", vo.getGroupid());
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("userid", userid);
		map.put("page", page);
		map.put("perPage", perPage);
		
		list = getDashBoardDetailGridlist(map);
		
		//System.out.println(list);
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			String devicetypecd = String.valueOf((Integer) ohm.get("devicetypecd"));
			String data = (String) ohm.get("data");
			String[] channelidList = null;
			String[] dataList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (data != null)
				dataList = data.split("\\|");
			
			HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
						
			/*if (channelidList != null && dataList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					String channelName = ""; //다국어로 설정해야 함
					
					// 샘플데이터
					channelName = "channel.code_" + devicetypecd + "_" + channelidList[i];										
					channelName = messageSource.getMessage(channelName, null, LocaleContextHolder.getLocale());
					
					dataMap.put(channelidList[i], dataList[i]);
					if (channelNameMap.get(channelidList[i]) == null)
						channelNameMap.put(channelidList[i], channelName);
				}
			}*/
			
			if (channelidList != null && dataList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					String channelName = ""; //다국어로 설정해야 함
					nhm.put("channel"+channelidList[i], dataList[i]);
				}
			}
			
			nhm.put("regdate", ohm.get("regdate"));
			nhm.put("regdateTime", ohm.get("regdateTime"));
			channelDataMap.add(nhm);			
		
		}
				
		return channelDataMap;
	}	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public List<HashMap<String, Object>> getPastData(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		PastRecordMapper mapper = sqlSession.getMapper(PastRecordMapper.class);

		return mapper.getPastData(map);
	}
	
	public List<HashMap<String, Object>> getPastData(DeviceVO vo, String startdate, String enddate, String userid, String deviceid) throws Throwable {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		
		map.put("deviceid", vo.getDeviceid());
		map.put("groupid", vo.getGroupid());
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("userid", userid);
		
		
		list = getPastData(map);
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			String devicetypecd = String.valueOf((Integer) ohm.get("devicetypecd"));
			String data = (String) ohm.get("data");
			String[] channelidList = null;
			String[] dataList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (data != null)
				dataList = data.split("\\|");
			
			HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
						
			if (channelidList != null && dataList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					String channelName = ""; //다국어로 설정해야 함
					nhm.put("channel"+channelidList[i], dataList[i]);
				}
			}
			
			nhm.put("regdate", ohm.get("regdate") + "\n" + ohm.get("regdateTime"));
			nhm.put("minseqno", ohm.get("minseqno"));
			nhm.put("maxseqno", ohm.get("maxseqno"));
			//nhm.put("regdateTime", ohm.get("regdateTime"));
			channelDataMap.add(nhm);			
		
		}
				
		return channelDataMap;
	}

	
	
	
	
	@Override
	public List<HashMap<String, Object>> getPastNextData(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		PastRecordMapper mapper = sqlSession.getMapper(PastRecordMapper.class);

		return mapper.getPastNextData(map);
	}
	
	public List<HashMap<String, Object>> getPastNextData(DeviceVO vo, String startdate, String enddate, String userid, String deviceid, 
			int lastSeqno) throws Throwable {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		
		map.put("deviceid", vo.getDeviceid());
		map.put("groupid", vo.getGroupid());
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("userid", userid);
		map.put("lastSeqno", lastSeqno);
		
		list = getPastNextData(map);
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			String devicetypecd = String.valueOf((Integer) ohm.get("devicetypecd"));
			String data = (String) ohm.get("data");
			String[] channelidList = null;
			String[] dataList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (data != null)
				dataList = data.split("\\|");
			
			HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
			
			if (channelidList != null && dataList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					String channelName = ""; //다국어로 설정해야 함
					nhm.put("channel"+channelidList[i], dataList[i]);
				}
			}
			
			nhm.put("regdate", ohm.get("regdate") + "\n" + ohm.get("regdateTime"));
			nhm.put("minseqno", ohm.get("minseqno"));
			nhm.put("maxseqno", ohm.get("maxseqno"));
			//nhm.put("regdateTime", ohm.get("regdateTime"));
			channelDataMap.add(nhm);			
		
		}
				
		return channelDataMap;
	}
	
	@Override
	public List<HashMap<String, Object>> getPastPreData(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		PastRecordMapper mapper = sqlSession.getMapper(PastRecordMapper.class);

		return mapper.getPastPreData(map);
	}
	
	public List<HashMap<String, Object>> getPastPreData(DeviceVO vo, String startdate, String enddate, String userid, String deviceid, 
			int firstSeqno/*, int prelimit*/) throws Throwable {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		
		map.put("deviceid", vo.getDeviceid());
		map.put("groupid", vo.getGroupid());
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("userid", userid);
		map.put("firstSeqno", firstSeqno);
		/*map.put("prelimit", prelimit);*/
		
		list = getPastPreData(map);
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			String devicetypecd = String.valueOf((Integer) ohm.get("devicetypecd"));
			String data = (String) ohm.get("data");
			String[] channelidList = null;
			String[] dataList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (data != null)
				dataList = data.split("\\|");
			
			HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
			
			if (channelidList != null && dataList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					String channelName = ""; //다국어로 설정해야 함
					nhm.put("channel"+channelidList[i], dataList[i]);
				}
			}
			
			nhm.put("regdate", ohm.get("regdate") + "\n" + ohm.get("regdateTime"));
			nhm.put("minseqno", ohm.get("minseqno"));
			nhm.put("maxseqno", ohm.get("maxseqno"));
			//nhm.put("regdateTime", ohm.get("regdateTime"));
			channelDataMap.add(nhm);			
		
		}
		
		Collections.reverse(channelDataMap);
		
		return channelDataMap;
	}

	@Override
	public List<HashMap<String, Object>> getPastHomeData(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		PastRecordMapper mapper = sqlSession.getMapper(PastRecordMapper.class);

		return mapper.getPastHomeData(map);
	}
	
	public List<HashMap<String, Object>> getPastHomeData(DeviceVO vo, String startdate, String enddate, String userid, String deviceid, 
			int homeSeqno) throws Throwable {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = null;
		List<HashMap<String, Object>> channelDataMap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> channelNameMap = new HashMap<String, String>();
		
		map.put("deviceid", vo.getDeviceid());
		map.put("groupid", vo.getGroupid());
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		map.put("userid", userid);
		map.put("homeSeqno", homeSeqno);
		
		list = getPastHomeData(map);
		
		if (list == null)
			return null;
		
		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		while (e.hasNext()) {
			HashMap<String, Object> ohm = e.next();
			HashMap<String, Object> nhm = new HashMap<>();
			
			String channelid = (String) ohm.get("channelid");
			String devicetypecd = String.valueOf((Integer) ohm.get("devicetypecd"));
			String data = (String) ohm.get("data");
			String[] channelidList = null;
			String[] dataList = null;
			
			if (channelid != null)
				channelidList = channelid.split("\\|");
			
			if (data != null)
				dataList = data.split("\\|");
			
			HashMap<String, String> dataMap = new HashMap<>();
			
			Locale locale = LocaleContextHolder.getLocale();
			
			if (channelidList != null && dataList != null) {			
				for (int i = 0; i < channelidList.length; i++) {
					String channelName = ""; //다국어로 설정해야 함
					nhm.put("channel"+channelidList[i], dataList[i]);
				}
			}
			
			nhm.put("regdate", ohm.get("regdate") + "\n" + ohm.get("regdateTime"));
			nhm.put("minseqno", ohm.get("minseqno"));
			nhm.put("maxseqno", ohm.get("maxseqno"));
			channelDataMap.add(nhm);			
		
		}
		
		return channelDataMap;
	}
	
	
	
	
}
