package com.vitcon.openapi.pastrecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.vitcon.service.device.DeviceVO;
import com.vitcon.service.pastrecord.PastRecordMapper;
import com.vitcon.service.pastrecord.PastRecordService;
import com.vitcon.service.pastrecord.PastVO;
import com.vitcon.service.tui.grid.CommonMapper;
import com.vitcon.service.tui.grid.Data;
import com.vitcon.service.tui.grid.Pagination;
import com.vitcon.service.tui.grid.ResponseGridVO;

@RestController
@RequestMapping("/openapi")
public class PastRecordController {

    private static final Logger logger = LoggerFactory.getLogger(PastRecordController.class);
	
    @InitBinder
	  protected void initBinder(WebDataBinder binder){
	    DateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,true));
	 }
  
	@Autowired
	private PastRecordService service;

	@Autowired
	PastRecordMapper pastRecordMapper;
	
	@Autowired
	private CommonMapper commonmapper;
	
	/* 날짜별 과거 데이터 조회 */
	@RequestMapping("/past/recordlist")
	public ResponseObject getTimeRecodList(DeviceVO vo, String startdate, String enddate) {
		ResponseObject ret = new ResponseObject();
				
		if (vo.getDeviceid() == null || vo.getGroupid() == null || startdate == null || enddate == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
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
		
		HashMap<String, Object> data = null;		

		try {
			data = service.getTimeRecodList(vo, startdate, enddate, userid);
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);
		ret.setData(data);		
		
		return ret;
	}

	/* 날짜별 과거 데이터 조회 */
	@RequestMapping("/past/dashboarddetaillist")
	public ResponseObject getDashBoardDetailList(DeviceVO vo, String startdate, String enddate) {
		ResponseObject ret = new ResponseObject();
				
		if (vo.getDeviceid() == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}
		
		// 2018. 09. 18 김관우 추가, startdate 와 enddate 가 넘어오지 않을때는 최근 24시간의 데이터를 구하도록 한다.
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
		
		HashMap<String, Object> data = null;		

		try {
			data = service.getTimeRecodList(vo, startdate, enddate, userid);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);
		ret.setData(data);		
		
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/past/list")
	public ResponseGridVO getTimeRecodGridList(DeviceVO vo, String startdate, String enddate,
			@RequestParam("page") int page,
			@RequestParam("perPage") int perPage,
			@RequestParam("deviceid") String deviceid) throws Throwable {

      UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }
		
		int total =0;
		if(page <=0) {
			page = 1;
		}
		
		ResponseGridVO result = new ResponseGridVO();
		List<Object> result2 = new ArrayList<Object>();
	
		List<HashMap<String, Object>> data_test = null;
		data_test = service.getTimeRecodGridList(vo, startdate, enddate, userid, ((page-1)*perPage), perPage);
		total = commonmapper.pagingTotal();
		Iterator<HashMap<String, Object>> e2 = data_test.iterator();
		
		while (e2.hasNext()) {
			PastVO pastVO = new PastVO();
			HashMap<String, Object> m = e2.next();
			
			String regdateStr = (String) m.get("regdate");
			String regdateTimeStr = (String) m.get("regdateTime");
			String channel1 = (String) m.get("channel1");
			String channel2 = (String) m.get("channel2");
			String channel3 = (String) m.get("channel3");
			String channel4 = (String) m.get("channel4");
			String channel5 = (String) m.get("channel5");
			String channel6 = (String) m.get("channel6");
			String regdate = regdateStr + "\n" + regdateTimeStr;

			pastVO.setRegdate(regdate);
			pastVO.setChannel1(channel1);
			pastVO.setChannel2(channel2);
			pastVO.setChannel3(channel3);
			pastVO.setChannel4(channel4);
			pastVO.setChannel5(channel5);
			pastVO.setChannel6(channel6);
			
			result2.add(pastVO);
		}
	
		result.setResult(true);
		
		Data data2 = new Data();
		Pagination pagination2 = new Pagination();
		data2.setContents(result2);
		pagination2.setPage(page);
		pagination2.setTotalCount(total);
		data2.setPagination(pagination2);
		
		result.setData(data2);
		
		return result;
	}
	
	
	
	
	
	/* 날짜별 과거 데이터 조회 */
	@RequestMapping("/past/dashBoardDetailGridlist")
	public ResponseGridVO getDashBoardDetailGridlist(DeviceVO vo, String startdate, String enddate,
			@RequestParam("page") int page,
			@RequestParam("perPage") int perPage) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
				
		if (vo.getDeviceid() == null) {
			//ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return null;
		}
		
		// 2018. 09. 18 김관우 추가, startdate 와 enddate 가 넘어오지 않을때는 최근 24시간의 데이터를 구하도록 한다.
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

      UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       //ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return null;
	    }

	    int total =0;
		if(page <=0) {
			page = 1;
		}
	    
		ResponseGridVO result = new ResponseGridVO();
		List<Object> result2 = new ArrayList<Object>();
	
		List<HashMap<String, Object>> dashboard_detail = null;
		dashboard_detail = service.getDashBoardDetailGridlist(vo, startdate, enddate, userid, ((page-1)*perPage), perPage);
		total = commonmapper.pagingTotal();
		Iterator<HashMap<String, Object>> e2 = dashboard_detail.iterator();
		
		while (e2.hasNext()) {
			PastVO pastVO = new PastVO();
			HashMap<String, Object> m = e2.next();
			
			String regdateStr = (String) m.get("regdate");
			String regdateTimeStr = (String) m.get("regdateTime");
			String channel1 = (String) m.get("channel1");
			String channel2 = (String) m.get("channel2");
			String channel3 = (String) m.get("channel3");
			String channel4 = (String) m.get("channel4");
			String channel5 = (String) m.get("channel5");
			String channel6 = (String) m.get("channel6");
			String regdate = regdateStr + "\n" + regdateTimeStr;

			pastVO.setRegdate(regdate);
			pastVO.setChannel1(channel1);
			pastVO.setChannel2(channel2);
			pastVO.setChannel3(channel3);
			pastVO.setChannel4(channel4);
			pastVO.setChannel5(channel5);
			pastVO.setChannel6(channel6);
			
			result2.add(pastVO);
		}
	
		result.setResult(true);
		
		Data data2 = new Data();
		Pagination pagination2 = new Pagination();
		data2.setContents(result2);
		pagination2.setPage(page);
		pagination2.setTotalCount(total);
		data2.setPagination(pagination2);
		
		result.setData(data2);
		
		return result;
	}
	
	
	
	
	
	
	/* 과거데이터 페이징 */
	@RequestMapping("/past/pastData")
	public ResponseObject getPastData (DeviceVO vo, String startdate, String enddate, String deviceid) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
	
		UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }
		
		try {
			ret.setData(service.getPastData(vo, startdate, enddate, userid, deviceid));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/* 과거데이터 페이징 다음 */
	@RequestMapping("/past/pastNextData")
	public ResponseObject getPastNextData (DeviceVO vo, String startdate, String enddate, String deviceid, int lastSeqno) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
	
		UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }
		
		try {
			ret.setData(service.getPastNextData(vo, startdate, enddate, userid, deviceid, lastSeqno));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/* 과거데이터 페이징 이전 */
	@RequestMapping("/past/pastPreData")
	public ResponseObject getPastPreData (DeviceVO vo, String startdate, String enddate, String deviceid, int firstSeqno/*, int prelimit*/) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
	
		UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }
		
		try {
			ret.setData(service.getPastPreData(vo, startdate, enddate, userid, deviceid, firstSeqno/*, prelimit*/));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/* 과거데이터 페이징 처음으로 */
	@RequestMapping("/past/pastHomeData")
	public ResponseObject getPastHomeData (DeviceVO vo, String startdate, String enddate, String deviceid, int homeSeqno) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
	
		UserAuthentication user = null;
	    String userid = null;

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }
		
		try {
			ret.setData(service.getPastHomeData(vo, startdate, enddate, userid, deviceid, homeSeqno));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
