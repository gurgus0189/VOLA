package com.vitcon.web.statis;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.pastrecord.PastRecordController;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.openapi.statis.kindrecord.KindRecordController;
import com.vitcon.service.device.DeviceVO;
import com.vitcon.service.kindrecord.KindRecordService;
import com.vitcon.service.statis.StatisticsService;

@Controller
@RequestMapping("/statis/")
public class StatisticsController {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

	@Autowired
	private KindRecordController kindController;
	
	@Autowired
	private StatisticsService  statisticsService;

	@Autowired
	private PastRecordController pastRecordController;

	@Autowired
	private KindRecordService kindRecordService;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 전체 통계 엑셀 다운로드
	 * 엑셀 형식을 만든다. = > 가공중
	 * @param response
	 * @param request
	 */
	@RequestMapping("entire/excel/list")
	public void entireDownload(HttpServletResponse response, HttpServletRequest request,
			@RequestParam("startdate") Date startdate, @RequestParam("enddate") Date enddate,
			@RequestParam("devicetypecd") Integer devicetypecd,@RequestParam("localecode") String localecode) {		
		
		if (devicetypecd == null || devicetypecd.equals("")) {
			devicetypecd = -1; // 디바이스 타입코드가 넘어오지않는 경우 없는 코드인 -1으로 조회하도록 하였다.
		}
		
		ResponseObject ret = kindController.getEntireStatisticList(startdate, enddate, devicetypecd);

		if (!ret.getReturnCode().equals(StatusCode.OK))
			return;

		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) ret.getData();

		Iterator<HashMap<String, Object>> e = list.iterator();

		ArrayList<String> excelList = new ArrayList<>();

		HashMap<String, Object> map;

    // make Header
		String value = "";
    /*Locale locale = LocaleContextHolder.getLocale();
    if (locale.getLanguage() == null) {
      // korean default
      value = "분류,평균,min,max,max-min,표준편차";
    }*/
	if(localecode == null)
	{
		localecode = "en";
	}

//	switch (locale.getLanguage()) {
	switch (localecode) {
      case "ko":
        value = "분류,평균,min,max,max-min,표준편차";
        break;
      case "en":
        value = "SensorName,Average,min,Max,Max-min,Standard Deviation";
        break;
      case "ja":
        value = "分類,平均,min,Max,Max-min,標準偏差";
        break;
    }
    
		excelList.add(value);

		// make data
		while (e.hasNext()) {
			map = e.next();
			String channelid = String.valueOf((int) map.get("channelid")); // 분류
			Double avg = (Double) map.get("avgVal");
			String avgStr = String.valueOf(Math.round(avg.doubleValue() * 100d) / 100d);
			Double min = (Double) map.get("minVal");
			Double max = (Double) map.get("maxVal");

			String minStr = min.toString();
			String maxStr = max.toString();
			String maxMinStr = String.valueOf((int) (max - min));
			Double str = (Double) map.get("stdVal");
			String stdStr = String.valueOf(Math.round(str.doubleValue() * 100d) / 100d);
			excelList.add(channelid + "," + avgStr + "," + minStr + "," + maxStr + "," + maxMinStr + "," + stdStr);
		}

		statisticsService.excelDown(request, response, "entire", excelList);
	}
	
	@RequestMapping("/group/excel/list")
	public void groupDownload(HttpServletResponse response, HttpServletRequest request,
			@RequestParam("startdate") Date startdate, @RequestParam("enddate") Date enddate,
			@RequestParam("devicetypecd") Integer devicetypecd,@RequestParam("localecode") String localecode) {
		if (devicetypecd == null || devicetypecd.equals("")) {
			devicetypecd = -1; // 디바이스 타입코드가 넘어오지않는 경우 없는 코드인 -1으로 조회하도록 하였다.
		}
				
		ResponseObject ret = 
				kindController.getGroupStatisticList(startdate, enddate, devicetypecd);
		
		if (!ret.getReturnCode().equals(StatusCode.OK)) 
			return;
		
		
		List<HashMap<String, Object>> list =
				(List<HashMap<String, Object>>) ret.getData();
		
		Iterator<HashMap<String, Object>> e = list.iterator();
				
		ArrayList<String> excelList = new ArrayList<>();
		
		HashMap<String, Object> map = null;
		
    // make Header
    String value = "";
    /*Locale locale = LocaleContextHolder.getLocale();
    if (locale.getLanguage() == null) {
      // korean default
      value = "그룹,분류,평균,min,max,max-min,표준편차"; 
    }*/

    if(localecode == null)
	{
		localecode = "en";
	}

//	switch (locale.getLanguage()) {
	switch (localecode) {
      case "ko":
        value = "그룹,분류,평균,min,max,max-min,표준편차"; 
        break;
      case "en":
        value = "Group,SensorName,Average,min,Max,Max-min,Standard Deviation";
        break;
      case "ja":
        value = "グループ,分類,平均,min,Max,Max-min,標準偏差";
        break;
    }
		excelList.add(value);
		
		// make data
		while (e.hasNext()) {
			map = e.next();			
			String groupname = String.valueOf(map.get("groupname"));			
			String channelid = String.valueOf((int)map.get("channelid"));			
			Double avg = (Double) map.get("avgVal");
			String avgStr = String.valueOf(Math.round(avg.doubleValue() * 100d) / 100d);
			Double min = (Double) map.get("minVal");
			Double max = (Double) map.get("maxVal");

			String minStr = min.toString();
			String maxStr = max.toString();
			String maxMinStr = String.valueOf((int) (max - min));
			Double str = (Double) map.get("stdVal");
			String stdStr = String.valueOf(Math.round(str.doubleValue() * 100d) / 100d);
			
			excelList.add(groupname + "," + channelid + "," + avgStr + "," + minStr + "," + maxStr
					+ "," + maxMinStr + "," + stdStr);			
		}
		statisticsService.excelDown(request, response, "group", excelList);
	}
	
	@RequestMapping("/device/excel/list")
	public void sensorDownload(HttpServletResponse response, HttpServletRequest request,
			@RequestParam("startdate") Date startdate, @RequestParam("enddate") Date enddate,
			@RequestParam("devicetypecd") Integer devicetypecd,@RequestParam("localecode") String localecode) {
		if (devicetypecd == null || devicetypecd.equals("")) {
			devicetypecd = -1; // 디바이스 타입코드가 넘어오지않는 경우 없는 코드인 -1으로 조회하도록 하였다.
		}
		
		ResponseObject ret = kindController.getSensorStatisticList(startdate, enddate, devicetypecd);
		
		if (!ret.getReturnCode().equals(StatusCode.OK)) 
			return;
		
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) ret.getData();
		
		Iterator<HashMap<String, Object>> e = list.iterator();
		
		ArrayList<String> excelList = new ArrayList<>();
		
		HashMap<String, Object> map;
		
    // make Header
    String value = "";
    /*Locale locale = LocaleContextHolder.getLocale();
    if (locale.getLanguage() == null) {
      // korean default
      value = "디바이스,그룹,분류,평균,min,max,max-min,표준편차"; 
    }*/

    if(localecode == null)
	{
		localecode = "en";
	}

//	switch (locale.getLanguage()) {
	switch (localecode) {
      case "ko":
        value = "디바이스,그룹,분류,평균,min,max,max-min,표준편차"; 
        break;
      case "en":
        value = "Device,Group,SensorName,Average,min,Max,Max-min,Standard Deviation";
        break;
      case "ja":
        value = "デバイス,グループ,分類,平均,min,Max,Max-min,標準偏差";
        break;
    }
    
		excelList.add(value);
				
		// make data
		while (e.hasNext()) {
			map = e.next();	
			String devicename = String.valueOf(map.get("devicename"));			
			String groupname = String.valueOf(map.get("groupname"));			
			String channelid = String.valueOf((int)map.get("channelid"));			
			Double avg = (Double) map.get("avgVal");
			String avgStr = String.valueOf(Math.round(avg.doubleValue() * 100d) / 100d);
			Double min = (Double) map.get("minVal");
			Double max = (Double) map.get("maxVal");

			String minStr = min.toString();
			String maxStr = max.toString();
			String maxMinStr = String.valueOf((int) (max - min));
			Double str = (Double) map.get("stdVal");
			String stdStr = String.valueOf(Math.round(str.doubleValue() * 100d) / 100d);
			
			excelList.add(devicename + "," +groupname + "," + channelid + "," + avgStr + "," + minStr + "," + maxStr
					+ "," + maxMinStr + "," + stdStr);			

		}
		
		statisticsService.excelDown(request, response, "device", excelList);					
	}
	
	@RequestMapping("/past/excel/list")
	   public void pastListDownload(HttpServletResponse response, HttpServletRequest request,
	         @RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
	         @RequestParam("deviceid") String deviceid, @RequestParam("devicename") String devicename,
	         @RequestParam("devicetypecd") String devicetypecd,
	         @RequestParam("groupid") Integer groupid,@RequestParam("localecode") String localecode) {

	      if (startdate == null || startdate.isEmpty() || enddate == null || enddate.isEmpty() || deviceid == null
	            || deviceid.isEmpty() || groupid == null) {
	         return;
	      }

	      DeviceVO vo = new DeviceVO();
	      vo.setDeviceid(deviceid);
	      vo.setGroupid(groupid);
	      
	      ArrayList<String> result = new ArrayList<>();
	      
	      //String devicetypecd2 = "H10";
	      switch(devicetypecd) {
	      case "T10-N": case "T10-C": case "T10-R":
	         result = t10ExcelDown(vo, startdate, enddate, localecode);
	         break;
	      case "H10":
	         result = h10ExcelDown(vo, startdate, enddate, localecode);
	         break;
	      default:
	         break;
	      }
	      
	      
	      String startdate2 = startdate.replace(":", "");
	      String enddate2 = enddate.replace(":", "");
	      
	      statisticsService.excelDown(request, response, devicename + "_" + startdate2 + "_to_" + enddate2, result);
	   }
	
	public ArrayList<String> t10ExcelDown(DeviceVO vo,String startdate,String enddate, String localecode) {
	      ResponseObject ret = pastRecordController.getTimeRecodList(vo, startdate, enddate);

	      if (!ret.getReturnCode().equals(StatusCode.OK)) {
	         return null;
	      }

	      HashMap<String, Object> data = (HashMap<String, Object>) ret.getData();

	      HashMap<String, String> channelNameMap = (HashMap<String, String>) data.get("channelname");
	      String chan1 = channelNameMap.get("1");
	      String chan2 = channelNameMap.get("2");
	      String chan3 = channelNameMap.get("3");

	      List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) data.get("channeldata");
	      // make Header
	      String value = "";
	      if (localecode == null) {
	         localecode = "en";
	      }
	      
//	      switch (locale.getLanguage()) {
	      switch (localecode) {
	      case "en":
	         value = "Date,Temperature 1,Temperature 2,Temperature 3";
	         /*if (chan2 != null) {
	            value += ",Temperature 2";
	         }
	         if (chan3 != null) {
	            value += ",Temperature 3";
	         }*/
	         break;
	      case "ja":
	         value = "日,温度 1,温度 2,温度 3";
	         /*if (chan2 != null) {
	            value += ",温度 2";
	         }
	         if (chan3 != null) {
	            value += ",温度 3";
	         }*/
	         break;
	      case "ko":
	      default:
	         value = "날짜,온도 1,온도 2,온도 3";
	         /*if (chan2 != null) {
	            value += ",온도 2";
	         }
	         if (chan3 != null) {
	            value += ",온도 3";
	         }*/
	         break;
	      }

	      ArrayList<String> excelList = new ArrayList<>();
	      excelList.add(startdate + " to " + enddate);
	      excelList.add(value);

	      HashMap<String, Object> map;
	      Iterator<HashMap<String, Object>> e = list.iterator();
	      // make data
	      while (e.hasNext()) {
	         map = e.next();
	         String date = String.valueOf(map.get("regdate"));
	         String time = String.valueOf(map.get("regdateTime"));
	         HashMap<String, String> tempMap = (HashMap<String, String>) map.get("data");

	         String datetime = String.format("%s %s", date, time);
	         String temp1 = "";
	         String temp2 = "";
	         String temp3 = "";
	         if (chan1 != null) {
	            temp1 = tempMap.get("1");
	         }
	         if (chan2 != null) {
	            temp2 = tempMap.get("2");
	         }
	         if (chan3 != null) {
	            temp3 = tempMap.get("3");
	         }

	         excelList.add(String.format("%s,%s,%s,%s", datetime, temp1, temp2, temp3));
	      }
	      return excelList;
	   }
	   public ArrayList<String> h10ExcelDown(DeviceVO vo,String startdate,String enddate, String localecode) {
	      ResponseObject ret = pastRecordController.getTimeRecodList(vo, startdate, enddate);

	      if (!ret.getReturnCode().equals(StatusCode.OK)) {
	         return null;
	      }

	      HashMap<String, Object> data = (HashMap<String, Object>) ret.getData();

	      HashMap<String, String> channelNameMap = (HashMap<String, String>) data.get("channelname");
	      String chan1 = channelNameMap.get("1");
	      String chan2 = channelNameMap.get("2");
	      String chan3 = channelNameMap.get("3");
	      String chan4 = channelNameMap.get("4");
	      String chan5 = channelNameMap.get("5");
	      String chan6 = channelNameMap.get("6");

	      List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) data.get("channeldata");
	      // make Header
	      String value = "";
	      if (localecode == null) {
	         localecode = "en";
	      }
	      
//	      switch (locale.getLanguage()) {
	      switch (localecode) {
	      case "en":
	         value = "Date,Temperature 1,Humidity 1,Temperature 2,Humidity 2,Temperature 3,Humidity 3";
	         /*if (chan2 != null) {
	            value += ",Humidity 1";
	         }
	         if (chan3 != null) {
	            value += ",Temperature 2";
	         }
	         if (chan4 != null) {
	            value += ",Humidity 2";
	         }
	         if (chan5 != null) {
	            value += ",Temperature 3";
	         }
	         if (chan6 != null) {
	            value += ",Humidity 3";
	         }*/
	         break;
	      case "ja":
	         value = "日,温度 1,湿度 1,温度 2,湿度 2,温度 3,湿度 3";
	         /*if (chan2 != null) {
	            value += ",湿度 1";
	         }
	         if (chan3 != null) {
	            value += ",温度 2";
	         }
	         if (chan3 != null) {
	            value += ",湿度 2";
	         }
	         if (chan3 != null) {
	            value += ",温度 3";
	         }
	         if (chan3 != null) {
	            value += ",湿度 3";
	         }*/
	         break;
	      case "ko":
	      default:
	         value = "날짜,온도 1,습도 1,온도 2,습도 2,온도 3,습도 3";
	         /*if (chan2 != null) {
	            value += ",습도 1";
	         }
	         if (chan3 != null) {
	            value += ",온도 2";
	         }
	         if (chan4 != null) {
	            value += ",습도 2";
	         }
	         if (chan5 != null) {
	            value += ",온도 3";
	         }
	         if (chan6 != null) {
	            value += ",습도 3";
	         }*/
	         break;
	      }

	      ArrayList<String> excelList = new ArrayList<>();
	      excelList.add(startdate + " to " + enddate);
	      excelList.add(value);

	      HashMap<String, Object> map;
	      Iterator<HashMap<String, Object>> e = list.iterator();
	      // make data
	      while (e.hasNext()) {
	         map = e.next();
	         String date = String.valueOf(map.get("regdate"));
	         String time = String.valueOf(map.get("regdateTime"));
	         HashMap<String, String> tempMap = (HashMap<String, String>) map.get("data");

	         String datetime = String.format("%s %s", date, time);
	         String temp1 = "";
	         String temp2 = "";
	         String temp3 = "";
	         String temp4 = "";
	         String temp5 = "";
	         String temp6 = "";
	         if (chan1 != null) {
	            temp1 = tempMap.get("1");
	         }
	         if (chan2 != null) {
	            temp2 = tempMap.get("2");
	         }
	         if (chan3 != null) {
	            temp3 = tempMap.get("3");
	         }
	         if (chan4 != null) {
	            temp4 = tempMap.get("4");
	         }
	         if (chan5 != null) {
	            temp5 = tempMap.get("5");
	         }
	         if (chan6 != null) {
	            temp6 = tempMap.get("6");
	         }

	         excelList.add(String.format("%s,%s,%s,%s,%s,%s,%s", datetime, temp1, temp2, temp3, temp4, temp5, temp6));
	      }
	      return excelList;
	   }
	   
	   
	   
	   @RequestMapping("entire/excel/excel_list")
		public void StatisentireDownload(HttpServletResponse response, HttpServletRequest request,
				@RequestParam("down_deviceid") String deviceid,
				@RequestParam("down_startdate") String startdate,
				@RequestParam("down_enddate") String enddate,
				@RequestParam("down_datetypecd") String datetypecd,
				@RequestParam("down_devicetypecd") Integer devicetypecd,
				@RequestParam("down_arraySize") Integer arraySize,
				@RequestParam("down_localecode") String localecode,
				@RequestParam("down_startchannelid") Integer startchannelid,
				@RequestParam("down_endchannelid") Integer endchannelid ) throws Throwable {		
			
			if (deviceid == null || startdate == null || enddate == null || devicetypecd == null || devicetypecd == null || 
					arraySize == null || localecode == null) {
				return;
			}
			
			ArrayList<String> result = new ArrayList<>();
			
			switch(devicetypecd) {
				case 1: case 2: case 3:
					result = t10StatisExcelDown(deviceid, startdate, enddate, datetypecd, devicetypecd, localecode, startchannelid);
					break;
				case 4:
					result = h10StatisExcelDown(deviceid, startdate, enddate, datetypecd, devicetypecd, localecode, startchannelid, endchannelid);
					break;
				default:
					break;
			}
			
			String startdate2 = startdate.replace(":", "");
		    String enddate2 = enddate.replace(":", "");
			
			statisticsService.excelDown(request, response, deviceid + "_" + startdate2 + "_to_" + enddate2, result);
			
		}
	   
	   
	   
	   public ArrayList<String> t10StatisExcelDown(String deviceid, String startdate, String enddate, String datetypecd, Integer devicetypecd, String localecode,
			   Integer startchannelid) throws Throwable {
		  
		  List<HashMap<String, Object>> list  = kindRecordService.getT10StatisticExcelList(deviceid, startdate, enddate, datetypecd, startchannelid);
		    
		  String value = "";
	      if (localecode == null) {
	        localecode = "en";
	      }
		  
	      switch (localecode) {
		      case "en":
		         value = "Date,Temperature Average, Temperature Max, Temperature Min";
		         break;
		      case "ja":
		         value = "日,温度  平均, 温度 Max, 温度 Min";
		         break;
		      case "ko":
		      default:
		         value = "날짜,온도 평균,온도 MAX,온도 MIN";
		         break;
	      }
		   
  		Iterator<HashMap<String, Object>> e = list.iterator();		
		
  		Timestamp ts = new Timestamp(System.currentTimeMillis());  
		DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  		
		ArrayList<String> excelList = new ArrayList<>();
		
  		excelList.add(startdate + " to " + enddate);
  		excelList.add(value);
		
		while (e.hasNext()) {
			
			HashMap<String, Object> ohm = e.next();
			
			Date regdateDate = ts; 
			regdateDate = (Date) ohm.get("regdate");
			
			String regdate = dateFormat.format(regdateDate);
			Double tempAvg = null;
			Double tempMax = null;
			Double tempMin = null;
			
			if(startchannelid.equals(1)) {
				tempAvg = (Double) ohm.get("avg_channel1");
				tempMax = (Double) ohm.get("max_channel1");
				tempMin = (Double) ohm.get("min_channel1");	
			} else if (startchannelid.equals(2)) {
				tempAvg = (Double) ohm.get("avg_channel2");
				tempMax = (Double) ohm.get("max_channel2");
				tempMin = (Double) ohm.get("min_channel2");
			} else if(startchannelid.equals(3)) {
				tempAvg = (Double) ohm.get("avg_channel3");
				tempMax = (Double) ohm.get("max_channel3");
				tempMin = (Double) ohm.get("min_channel3");		
			}
			
			excelList.add(String.format("%s,%s,%s,%s", regdate, tempAvg, tempMax, tempMin));
		}
  		
		 return excelList;
			 
	  }
	   
	  public ArrayList<String> h10StatisExcelDown(String deviceid, String startdate, String enddate, String datetypecd, Integer devicetypecd, String localecode,
			   Integer startchannelid, Integer endchannelid) throws Throwable {
		  
		  List<HashMap<String, Object>> list  = kindRecordService.getH10StatisticExcelList(deviceid, startdate, enddate, datetypecd, startchannelid, endchannelid);
		    
		  String value = "";
	      if (localecode == null) {
	        localecode = "en";
	      }
		  
	      switch (localecode) {
		      case "en":
		         value = "Date,Temperature Average, Temperature Max, Temperature Min, Humidity Average, Humidity Max, Humidity Min";
		         break;
		      case "ja":
		         value = "日,温度  平均, 温度 Max, 温度 Min, 湿度 平均, 湿度 Max, 湿度 Min";
		         break;
		      case "ko":
		      default:
		         value = "날짜,온도 평균,온도 MAX,온도 MIN,습도 평균,습도 MAX,습도 MIN";
		         break;
	      }
		   
  		Iterator<HashMap<String, Object>> e = list.iterator();		
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());  
		DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		ArrayList<String> excelList = new ArrayList<>();
	
  		excelList.add(startdate + " to " + enddate);
  		excelList.add(value);
		
		while (e.hasNext()) {
			
			HashMap<String, Object> ohm = e.next();
			
			Date regdateDate = ts; 
			regdateDate = (Date) ohm.get("regdate");
			
			String regdate = dateFormat.format(regdateDate);
			Double tempAvg = null;
			Double tempMax = null;
			Double tempMin = null;
			Double humAvg = null;
			Double humMax = null;
			Double humMin = null;
			
			if(startchannelid.equals(1) && endchannelid.equals(2)) {
				tempAvg = (Double) ohm.get("avg_channel1");
				tempMax = (Double) ohm.get("max_channel1");
				tempMin = (Double) ohm.get("min_channel1");
				humAvg = (Double) ohm.get("avg_channel2");
				humMax = (Double) ohm.get("max_channel2");
				humMin = (Double) ohm.get("min_channel2");
			} else if (startchannelid.equals(3) && endchannelid.equals(4)) {
				tempAvg = (Double) ohm.get("avg_channel3");
				tempMax = (Double) ohm.get("max_channel3");
				tempMin = (Double) ohm.get("min_channel3");
				humAvg = (Double) ohm.get("avg_channel4");
				humMax = (Double) ohm.get("max_channel4");
				humMin = (Double) ohm.get("min_channel4");
			} else if(startchannelid.equals(5) && endchannelid.equals(6)) {
				tempAvg = (Double) ohm.get("avg_channel5");
				tempMax = (Double) ohm.get("max_channel5");
				tempMin = (Double) ohm.get("min_channel5");
				humAvg = (Double) ohm.get("avg_channel6");
				humMax = (Double) ohm.get("max_channel6");
				humMin = (Double) ohm.get("min_channel6");
			}
			
			excelList.add(String.format("%s,%s,%s,%s,%s,%s,%s", regdate, tempAvg, tempMax, tempMin, humAvg, humMax, humMin));
		}
  		
		 return excelList;
			 
	  }
	   
	   
	   
	   
	   
	   
	
}
