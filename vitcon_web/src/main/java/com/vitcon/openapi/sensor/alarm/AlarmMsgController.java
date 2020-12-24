package com.vitcon.openapi.sensor.alarm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.account.AccountService;
import com.vitcon.service.alarm.AlarmMsgService;
import com.vitcon.service.tui.grid.AlarmMsgGridVO;
import com.vitcon.service.tui.grid.CommonMapper;
import com.vitcon.service.tui.grid.Data;
import com.vitcon.service.tui.grid.Pagination;
import com.vitcon.service.tui.grid.ResponseGridVO;
import com.vitcon.service.user.UserVO;

@RestController
@RequestMapping("/openapi/alarm")
public class AlarmMsgController {
	
	private static final Logger logger = LoggerFactory.getLogger(AlarmMsgController.class);
	
	@Autowired
	private AlarmMsgService service;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private CommonMapper commonmapper;
	
	ObjectMapper mapper = new ObjectMapper();

	@InitBinder
	protected void initBinder(WebDataBinder binder){
		//DateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,true));
	 }	
	
	@RequestMapping(value = "/test2", method = RequestMethod.GET)
	public void test(@RequestParam("page") String page,@RequestParam("perPage") String perPage, HttpServletRequest request) {
		System.out.println("!@3123123");
	}
	/* 알람 내역 조회 */
	@RequestMapping(value = "/msglist", method = RequestMethod.GET)
	//@RequestMapping("/msglist")
	public ResponseGridVO getAlarmMsg(@RequestParam("startdate") String startdate, 
			
			@RequestParam("enddate") String enddate,
			@RequestParam(value="groupid", required=false, defaultValue="") String groupid,
			@RequestParam(value="devicetypecd", required=false, defaultValue="") String devicetypecd,
			@RequestParam(value="deviceid", required=false, defaultValue="") String deviceid,
			@RequestParam("page") int page,
			@RequestParam("perPage") int perPage) {
		ResponseGridVO result = new ResponseGridVO();
		int total =0;
		
		//ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<String, Object>();

		if (startdate == null || enddate == null || groupid == null) {
			result.setResult(false);
			result.setMessage("error");
			result.setData(null);
			return result;
		}

		List<HashMap<String, Object>> list = null;

		UserAuthentication user = null;
		String userid = null;

		try {
			user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			userid = user.getName(); // 로그인한 사용자의 id
		} catch (Exception e) {
		}

		if (userid == null) {
			// 유저아이디 존재하지 않음.
			result.setResult(false);
			result.setMessage("error");
			result.setData(null);
			return result;
		}
		
		//데이터 불러오기
		//등급별로 sql 문 다르게 해야해서 추가한다, 2019-01-14 kwt
		UserVO userVO = new UserVO();
		userVO.setUserid(userid);
		try {
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				result.setResult(false);
				result.setMessage("error: not exist user");
				result.setData(null);
				return result;
			}
			
			map.put("userid", userid);
			map.put("startdate", startdate);
			map.put("enddate", enddate);
			map.put("groupid", groupid);
			map.put("devicetypecd", devicetypecd);
			map.put("deviceid", deviceid);
			if(page <=0)
			{
				page = 1;
			}
			map.put("page", ((page-1)*perPage));
			map.put("perPage", perPage);
	
			if (userVO.getGrade() == 10) { //  GRADE = 10 은 구매자 - 구매자 리스트뷰 select	
				
				//logger.info("알람 메세시 mysql 시작");
				list = service.getAlarmMsg(map);
				total = commonmapper.pagingTotal();
				//logger.info("알람 메세시 mysql 끝");
				
			} else { //  구매자를 제외한 나머지 (중간관리자,사용자) 리스트뷰 select
				//여기를 수정할것임 sql 구문
				list = service.getAlarmMsgChildUser(map);
				total = commonmapper.pagingTotal();
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setResult(false);
			result.setMessage("error");
			result.setData(null);
			return result;
		}
		
		
		//알람리스트 작성용
		try {

			Iterator<HashMap<String, Object>> e = list.iterator();

			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Map<String, Map>> typeRef = new TypeReference<Map<String, Map>>() {
			};

			Map<String, Map> mdata = null;
			Map<String, String> mvalue = null;
			String data = null;

			String prefix = "alarm.code.";

			while (e.hasNext()) {
				HashMap<String, Object> m = e.next();
				String json = (String) m.get("data");

				if (json != null) {
					try {
						// 2018-11-14 김원태 변경 알람메세지 
						data = "";
						mdata = mapper.readValue(json, new TypeReference<Map<String, Map<String, String>>>() {
						});
						Iterator<String> keys = mdata.keySet().iterator();

						while (keys.hasNext()) {
							String channelid = keys.next();
							mvalue = mdata.get(channelid);

							Iterator<String> mkeys = mvalue.keySet().iterator();
							while (mkeys.hasNext()) {
								String code = mkeys.next();
								String unit = "";
								String typeCode = "";
								String v = mvalue.get(code);
								String key = prefix + code;
								String channelName = "";

								try {
									String tmp = "channel.code_" + channelid; // 임시
									// System.err.println("tmp=" + tmp);
									channelName = messageSource.getMessage(tmp, null, LocaleContextHolder.getLocale());
								} catch (Exception ec) {
									channelName = channelid;
								}

								try {
									String tmp = "alarm.code." + code;
									code = messageSource.getMessage(tmp, null, LocaleContextHolder.getLocale());

									String tmp2 = "type.code_" + channelid;
									unit = messageSource.getMessage(tmp2, null, LocaleContextHolder.getLocale());

								} catch (Exception ec) {

								}
								if (v.equals("")) {
									// 0번알람 (시스템 영역은 현재값이라는게 없음)
									data += String.format("%s:%s", channelName, code);
								} else {
									if (v.equals("ERR")) {
										data += String.format("%s:%s(%s)", channelName, code, v);
									} else {
										data += String.format("%s:%s(%s)%s", channelName, code, v, unit);
									}
								}

								if (mkeys.hasNext()) {
									data += ",";
								} else {

								}

							}

							if (keys.hasNext()) {
								data += ",";
							} else {

							}
						}
						// 2018-11-14 김원태 변경 알람메세지  end

					} catch (Exception ex) {
						data = "";
					}
				}

				if (data.length() > 2) {
					//data = data.substring(0, data.length() - 2); //2018-11-14 김원태 변경 
					m.put("data", data);
				}
			}

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setResult(false);
			result.setMessage("error");
			result.setData(null);
			return result;
		}

		
		
		//여기서부터 그리드 형태로 바꾸는 것 데이터 생성
		List<Object> result2 = new ArrayList<Object>();
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		while (e2.hasNext()) {
			AlarmMsgGridVO test = new AlarmMsgGridVO();
			HashMap<String, Object> m = e2.next();
			String regdate = (String) m.get("regdate");
			String devicename = (String) m.get("devicename");
			String data = (String) m.get("data");
			
			/*Date tempDate = null;
			
			try {
				tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(regdate +" "+ regdateTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			test.setRegdate(regdate);
			test.setDevicename(devicename);
			test.setAlarmmessage(data);
			result2.add(test);
			
		}
		
		//리턴시키는 값들
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* 알람 내역 조회 */
	@RequestMapping(value = "/alarmData", method = RequestMethod.GET)
	//@RequestMapping("/msglist")
	public ResponseObject getAlarmData(@RequestParam("startdate") String startdate, 
			
			@RequestParam("enddate") String enddate,
			@RequestParam(value="groupid", required=false, defaultValue="") String groupid,
			@RequestParam(value="devicetypecd", required=false, defaultValue="") String devicetypecd,
			@RequestParam(value="deviceid", required=false, defaultValue="") String deviceid) {
		ResponseGridVO result = new ResponseGridVO();
		ResponseObject ret = new ResponseObject();
		//int total =0;
		
		//ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<String, Object>();

		if (startdate == null || enddate == null || groupid == null) {
			//ret.setResult(false);
			//ret.setMessage("error");
			ret.setData(null);
			return ret;
		}

		List<HashMap<String, Object>> list = null;

		UserAuthentication user = null;
		String userid = null;

		try {
			user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			userid = user.getName(); // 로그인한 사용자의 id
		} catch (Exception e) {
		}

		if (userid == null) {
			// 유저아이디 존재하지 않음.
			//result.setResult(false);
			//result.setMessage("error");
			ret.setData(null);
			return ret;
		}
		
		//데이터 불러오기
		//등급별로 sql 문 다르게 해야해서 추가한다, 2019-01-14 kwt
		UserVO userVO = new UserVO();
		userVO.setUserid(userid);
		try {
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				//result.setResult(false);
				//result.setMessage("error: not exist user");
				ret.setData(null);
				return ret;
			}
			
			map.put("userid", userid);
			map.put("startdate", startdate);
			map.put("enddate", enddate);
			map.put("groupid", groupid);
			map.put("devicetypecd", devicetypecd);
			map.put("deviceid", deviceid);
			
	
			if (userVO.getGrade() == 10) { //  GRADE = 10 은 구매자 - 구매자 리스트뷰 select	
				
				//logger.info("알람 메세시 mysql 시작");
				list = service.getAlarmData(map);
				//total = commonmapper.pagingTotal();
				//logger.info("알람 메세시 mysql 끝");
				
			} else { //  구매자를 제외한 나머지 (중간관리자,사용자) 리스트뷰 select
				//여기를 수정할것임 sql 구문
				list = service.getAlarmDataMsgChildUser(map);
				//total = commonmapper.pagingTotal();
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//result.setResult(false);
			//result.setMessage("error");
			ret.setData(null);
			return ret;
		}
		
		
		//알람리스트 작성용
		try {

			Iterator<HashMap<String, Object>> e = list.iterator();

			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Map<String, Map>> typeRef = new TypeReference<Map<String, Map>>() {
			};

			Map<String, Map> mdata = null;
			Map<String, String> mvalue = null;
			String data = null;

			String prefix = "alarm.code.";

			while (e.hasNext()) {
				HashMap<String, Object> m = e.next();
				String json = (String) m.get("data");

				if (json != null) {
					try {
						// 2018-11-14 김원태 변경 알람메세지 
						data = "";
						mdata = mapper.readValue(json, new TypeReference<Map<String, Map<String, String>>>() {
						});
						Iterator<String> keys = mdata.keySet().iterator();

						while (keys.hasNext()) {
							String channelid = keys.next();
							mvalue = mdata.get(channelid);

							Iterator<String> mkeys = mvalue.keySet().iterator();
							while (mkeys.hasNext()) {
								String code = mkeys.next();
								String unit = "";
								String typeCode = "";
								String v = mvalue.get(code);
								String key = prefix + code;
								String channelName = "";

								try {
									String tmp = "channel.code_" + channelid; // 임시
									// System.err.println("tmp=" + tmp);
									channelName = messageSource.getMessage(tmp, null, LocaleContextHolder.getLocale());
								} catch (Exception ec) {
									channelName = channelid;
								}

								try {
									String tmp = "alarm.code." + code;
									code = messageSource.getMessage(tmp, null, LocaleContextHolder.getLocale());

									String tmp2 = "type.code_" + channelid;
									unit = messageSource.getMessage(tmp2, null, LocaleContextHolder.getLocale());

								} catch (Exception ec) {

								}
								if (v.equals("")) {
									// 0번알람 (시스템 영역은 현재값이라는게 없음)
									data += String.format("%s:%s", channelName, code);
								} else {
									if (v.equals("ERR")) {
										data += String.format("%s:%s(%s)", channelName, code, v);
									} else {
										data += String.format("%s:%s(%s)%s", channelName, code, v, unit);
									}
								}

								if (mkeys.hasNext()) {
									data += ",";
								} else {

								}

							}

							if (keys.hasNext()) {
								data += ",";
							} else {

							}
						}
						// 2018-11-14 김원태 변경 알람메세지  end

					} catch (Exception ex) {
						data = "";
					}
				}

				if (data.length() > 2) {
					//data = data.substring(0, data.length() - 2); //2018-11-14 김원태 변경 
					m.put("data", data);
				}
			}

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//ret.setResult(false);
			//ret.setMessage("error");
			ret.setData(null);
			return ret;
		}

		
		
		//여기서부터 그리드 형태로 바꾸는 것 데이터 생성
		List<Object> result2 = new ArrayList<Object>();
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		while (e2.hasNext()) {
			AlarmMsgGridVO test = new AlarmMsgGridVO();
			HashMap<String, Object> m = e2.next();
		
			int seqnoInt = (int) m.get("seqno");
			long seqno = Long.valueOf(seqnoInt);
			String regdate = (String) m.get("regdate");
			String devicename = (String) m.get("devicename");
			String data = (String) m.get("data");
			
			test.setSeqno(seqno);
			test.setRegdate(regdate);
			test.setDevicename(devicename);
			test.setAlarmmessage(data);
			result2.add(test);
			
		}
		
		ret.setData(result2);

		return ret;
		
	}
	
	/* 알람 내역 조회 */
	@RequestMapping(value = "/alarmNextData", method = RequestMethod.GET)
	//@RequestMapping("/msglist")
	public ResponseObject getAlarmNextData(@RequestParam("startdate") String startdate, 
			
			@RequestParam("enddate") String enddate,
			@RequestParam(value="groupid", required=false, defaultValue="") String groupid,
			@RequestParam(value="devicetypecd", required=false, defaultValue="") String devicetypecd,
			@RequestParam(value="deviceid", required=false, defaultValue="") String deviceid,
			@RequestParam(value="lastSeqno", required=false, defaultValue="") long lastSeqno) {
		ResponseGridVO result = new ResponseGridVO();
		ResponseObject ret = new ResponseObject();
		//int total =0;
		
		//ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<String, Object>();

		if (startdate == null || enddate == null || groupid == null) {
			//ret.setResult(false);
			//ret.setMessage("error");
			ret.setData(null);
			return ret;
		}

		List<HashMap<String, Object>> list = null;

		UserAuthentication user = null;
		String userid = null;

		try {
			user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			userid = user.getName(); // 로그인한 사용자의 id
		} catch (Exception e) {
		}

		if (userid == null) {
			// 유저아이디 존재하지 않음.
			//result.setResult(false);
			//result.setMessage("error");
			ret.setData(null);
			return ret;
		}
		
		//데이터 불러오기
		//등급별로 sql 문 다르게 해야해서 추가한다, 2019-01-14 kwt
		UserVO userVO = new UserVO();
		userVO.setUserid(userid);
		try {
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				//result.setResult(false);
				//result.setMessage("error: not exist user");
				ret.setData(null);
				return ret;
			}
			
			map.put("userid", userid);
			map.put("startdate", startdate);
			map.put("enddate", enddate);
			map.put("groupid", groupid);
			map.put("devicetypecd", devicetypecd);
			map.put("deviceid", deviceid);
			map.put("lastSeqno", lastSeqno);
			
	
			if (userVO.getGrade() == 10) { //  GRADE = 10 은 구매자 - 구매자 리스트뷰 select	
				
				//logger.info("알람 메세시 mysql 시작");
				list = service.getAlarmNextData(map);
				//total = commonmapper.pagingTotal();
				//logger.info("알람 메세시 mysql 끝");
				
			} else { //  구매자를 제외한 나머지 (중간관리자,사용자) 리스트뷰 select
				//여기를 수정할것임 sql 구문
				list = service.getAlarmNextMsgChildUser(map);
				//total = commonmapper.pagingTotal();
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//result.setResult(false);
			//result.setMessage("error");
			ret.setData(null);
			return ret;
		}
		
		
		//알람리스트 작성용
		try {

			Iterator<HashMap<String, Object>> e = list.iterator();

			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Map<String, Map>> typeRef = new TypeReference<Map<String, Map>>() {
			};

			Map<String, Map> mdata = null;
			Map<String, String> mvalue = null;
			String data = null;

			String prefix = "alarm.code.";

			while (e.hasNext()) {
				HashMap<String, Object> m = e.next();
				String json = (String) m.get("data");

				if (json != null) {
					try {
						// 2018-11-14 김원태 변경 알람메세지 
						data = "";
						mdata = mapper.readValue(json, new TypeReference<Map<String, Map<String, String>>>() {
						});
						Iterator<String> keys = mdata.keySet().iterator();

						while (keys.hasNext()) {
							String channelid = keys.next();
							mvalue = mdata.get(channelid);

							Iterator<String> mkeys = mvalue.keySet().iterator();
							while (mkeys.hasNext()) {
								String code = mkeys.next();
								String unit = "";
								String typeCode = "";
								String v = mvalue.get(code);
								String key = prefix + code;
								String channelName = "";

								try {
									String tmp = "channel.code_" + channelid; // 임시
									// System.err.println("tmp=" + tmp);
									channelName = messageSource.getMessage(tmp, null, LocaleContextHolder.getLocale());
								} catch (Exception ec) {
									channelName = channelid;
								}

								try {
									String tmp = "alarm.code." + code;
									code = messageSource.getMessage(tmp, null, LocaleContextHolder.getLocale());

									String tmp2 = "type.code_" + channelid;
									unit = messageSource.getMessage(tmp2, null, LocaleContextHolder.getLocale());

								} catch (Exception ec) {

								}
								if (v.equals("")) {
									// 0번알람 (시스템 영역은 현재값이라는게 없음)
									data += String.format("%s:%s", channelName, code);
								} else {
									if (v.equals("ERR")) {
										data += String.format("%s:%s(%s)", channelName, code, v);
									} else {
										data += String.format("%s:%s(%s)%s", channelName, code, v, unit);
									}
								}

								if (mkeys.hasNext()) {
									data += ",";
								} else {

								}

							}

							if (keys.hasNext()) {
								data += ",";
							} else {

							}
						}
						// 2018-11-14 김원태 변경 알람메세지  end

					} catch (Exception ex) {
						data = "";
					}
				}

				if (data.length() > 2) {
					//data = data.substring(0, data.length() - 2); //2018-11-14 김원태 변경 
					m.put("data", data);
				}
			}

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//ret.setResult(false);
			//ret.setMessage("error");
			ret.setData(null);
			return ret;
		}

		
		
		//여기서부터 그리드 형태로 바꾸는 것 데이터 생성
		List<Object> result2 = new ArrayList<Object>();
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		while (e2.hasNext()) {
			AlarmMsgGridVO test = new AlarmMsgGridVO();
			HashMap<String, Object> m = e2.next();
		
			int seqnoInt = (int) m.get("seqno");
			long seqno = Long.valueOf(seqnoInt);
			String regdate = (String) m.get("regdate");
			String devicename = (String) m.get("devicename");
			String data = (String) m.get("data");
			
			test.setSeqno(seqno);
			test.setRegdate(regdate);
			test.setDevicename(devicename);
			test.setAlarmmessage(data);
			result2.add(test);
			
		}
		
		ret.setData(result2);

		return ret;
		
	}
	
	
	
	
	/* 알람 내역 조회 이전버튼 */
	@RequestMapping(value = "/alarmPreData", method = RequestMethod.GET)
	//@RequestMapping("/msglist")
	public ResponseObject getAlarmPreData(@RequestParam("startdate") String startdate, 
			
			@RequestParam("enddate") String enddate,
			@RequestParam(value="groupid", required=false, defaultValue="") String groupid,
			@RequestParam(value="devicetypecd", required=false, defaultValue="") String devicetypecd,
			@RequestParam(value="deviceid", required=false, defaultValue="") String deviceid,
			@RequestParam(value="firstSeqno", required=false, defaultValue="") long firstSeqno) {
		ResponseGridVO result = new ResponseGridVO();
		ResponseObject ret = new ResponseObject();
		//int total =0;
		
		//ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<String, Object>();

		if (startdate == null || enddate == null || groupid == null) {
			//ret.setResult(false);
			//ret.setMessage("error");
			ret.setData(null);
			return ret;
		}

		List<HashMap<String, Object>> list = null;

		UserAuthentication user = null;
		String userid = null;

		try {
			user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			userid = user.getName(); // 로그인한 사용자의 id
		} catch (Exception e) {
		}

		if (userid == null) {
			// 유저아이디 존재하지 않음.
			//result.setResult(false);
			//result.setMessage("error");
			ret.setData(null);
			return ret;
		}
		
		//데이터 불러오기
		//등급별로 sql 문 다르게 해야해서 추가한다, 2019-01-14 kwt
		UserVO userVO = new UserVO();
		userVO.setUserid(userid);
		try {
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				//result.setResult(false);
				//result.setMessage("error: not exist user");
				ret.setData(null);
				return ret;
			}
			
			map.put("userid", userid);
			map.put("startdate", startdate);
			map.put("enddate", enddate);
			map.put("groupid", groupid);
			map.put("devicetypecd", devicetypecd);
			map.put("deviceid", deviceid);
			map.put("firstSeqno", firstSeqno);
			
	
			if (userVO.getGrade() == 10) { //  GRADE = 10 은 구매자 - 구매자 리스트뷰 select	
				
				//logger.info("알람 메세시 mysql 시작");
				list = service.getAlarmPreData(map);
				//total = commonmapper.pagingTotal();
				//logger.info("알람 메세시 mysql 끝");
				
			} else { //  구매자를 제외한 나머지 (중간관리자,사용자) 리스트뷰 select
				//여기를 수정할것임 sql 구문
				list = service.getAlarmPreMsgChildUser(map);
				//total = commonmapper.pagingTotal();
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//result.setResult(false);
			//result.setMessage("error");
			ret.setData(null);
			return ret;
		}
		
		
		//알람리스트 작성용
		try {

			Iterator<HashMap<String, Object>> e = list.iterator();

			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Map<String, Map>> typeRef = new TypeReference<Map<String, Map>>() {
			};

			Map<String, Map> mdata = null;
			Map<String, String> mvalue = null;
			String data = null;

			String prefix = "alarm.code.";

			while (e.hasNext()) {
				HashMap<String, Object> m = e.next();
				String json = (String) m.get("data");

				if (json != null) {
					try {
						// 2018-11-14 김원태 변경 알람메세지 
						data = "";
						mdata = mapper.readValue(json, new TypeReference<Map<String, Map<String, String>>>() {
						});
						Iterator<String> keys = mdata.keySet().iterator();

						while (keys.hasNext()) {
							String channelid = keys.next();
							mvalue = mdata.get(channelid);

							Iterator<String> mkeys = mvalue.keySet().iterator();
							while (mkeys.hasNext()) {
								String code = mkeys.next();
								String unit = "";
								String typeCode = "";
								String v = mvalue.get(code);
								String key = prefix + code;
								String channelName = "";

								try {
									String tmp = "channel.code_" + channelid; // 임시
									// System.err.println("tmp=" + tmp);
									channelName = messageSource.getMessage(tmp, null, LocaleContextHolder.getLocale());
								} catch (Exception ec) {
									channelName = channelid;
								}

								try {
									String tmp = "alarm.code." + code;
									code = messageSource.getMessage(tmp, null, LocaleContextHolder.getLocale());

									String tmp2 = "type.code_" + channelid;
									unit = messageSource.getMessage(tmp2, null, LocaleContextHolder.getLocale());

								} catch (Exception ec) {

								}
								if (v.equals("")) {
									// 0번알람 (시스템 영역은 현재값이라는게 없음)
									data += String.format("%s:%s", channelName, code);
								} else {
									if (v.equals("ERR")) {
										data += String.format("%s:%s(%s)", channelName, code, v);
									} else {
										data += String.format("%s:%s(%s)%s", channelName, code, v, unit);
									}
								}

								if (mkeys.hasNext()) {
									data += ",";
								} else {

								}

							}

							if (keys.hasNext()) {
								data += ",";
							} else {

							}
						}
						// 2018-11-14 김원태 변경 알람메세지  end

					} catch (Exception ex) {
						data = "";
					}
				}

				if (data.length() > 2) {
					//data = data.substring(0, data.length() - 2); //2018-11-14 김원태 변경 
					m.put("data", data);
				}
			}

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//ret.setResult(false);
			//ret.setMessage("error");
			ret.setData(null);
			return ret;
		}

		
		
		//여기서부터 그리드 형태로 바꾸는 것 데이터 생성
		List<Object> result2 = new ArrayList<Object>();
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		while (e2.hasNext()) {
			AlarmMsgGridVO test = new AlarmMsgGridVO();
			HashMap<String, Object> m = e2.next();
		
			int seqnoInt = (int) m.get("seqno");
			long seqno = Long.valueOf(seqnoInt);
			String regdate = (String) m.get("regdate");
			String devicename = (String) m.get("devicename");
			String data = (String) m.get("data");
			
			test.setSeqno(seqno);
			test.setRegdate(regdate);
			test.setDevicename(devicename);
			test.setAlarmmessage(data);
			result2.add(test);
			
		}
		
		Collections.reverse(result2);
		
		ret.setData(result2);

		return ret;
		
	}
	
	/* 알람 내역 조회 처음으로 */
	@RequestMapping(value = "/alarmHomeData", method = RequestMethod.GET)
	//@RequestMapping("/msglist")
	public ResponseObject getAlarmHomeData(@RequestParam("startdate") String startdate, 
			
			@RequestParam("enddate") String enddate,
			@RequestParam(value="groupid", required=false, defaultValue="") String groupid,
			@RequestParam(value="devicetypecd", required=false, defaultValue="") String devicetypecd,
			@RequestParam(value="deviceid", required=false, defaultValue="") String deviceid,
			@RequestParam(value="homeSeqno", required=false, defaultValue="") long homeSeqno) {
		ResponseGridVO result = new ResponseGridVO();
		ResponseObject ret = new ResponseObject();
		//int total =0;
		
		//ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<String, Object>();

		if (startdate == null || enddate == null || groupid == null) {
			//ret.setResult(false);
			//ret.setMessage("error");
			ret.setData(null);
			return ret;
		}

		List<HashMap<String, Object>> list = null;

		UserAuthentication user = null;
		String userid = null;

		try {
			user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			userid = user.getName(); // 로그인한 사용자의 id
		} catch (Exception e) {
		}

		if (userid == null) {
			// 유저아이디 존재하지 않음.
			//result.setResult(false);
			//result.setMessage("error");
			ret.setData(null);
			return ret;
		}
		
		//데이터 불러오기
		//등급별로 sql 문 다르게 해야해서 추가한다, 2019-01-14 kwt
		UserVO userVO = new UserVO();
		userVO.setUserid(userid);
		try {
			userVO = accountService.getUser(userVO); // userVO 를 다시 재활용한다.
			if (userVO == null) {
				// 해당 유저 정보 없음
				//result.setResult(false);
				//result.setMessage("error: not exist user");
				ret.setData(null);
				return ret;
			}
			
			map.put("userid", userid);
			map.put("startdate", startdate);
			map.put("enddate", enddate);
			map.put("groupid", groupid);
			map.put("devicetypecd", devicetypecd);
			map.put("deviceid", deviceid);
			map.put("homeSeqno", homeSeqno);
			
	
			if (userVO.getGrade() == 10) { //  GRADE = 10 은 구매자 - 구매자 리스트뷰 select	
				
				//logger.info("알람 메세시 mysql 시작");
				list = service.getAlarmHomeData(map);
				//total = commonmapper.pagingTotal();
				//logger.info("알람 메세시 mysql 끝");
				
			} else { //  구매자를 제외한 나머지 (중간관리자,사용자) 리스트뷰 select
				//여기를 수정할것임 sql 구문
				list = service.getAlarmHomeMsgChildUser(map);
				//total = commonmapper.pagingTotal();
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//result.setResult(false);
			//result.setMessage("error");
			ret.setData(null);
			return ret;
		}
		
		
		//알람리스트 작성용
		try {

			Iterator<HashMap<String, Object>> e = list.iterator();

			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Map<String, Map>> typeRef = new TypeReference<Map<String, Map>>() {
			};

			Map<String, Map> mdata = null;
			Map<String, String> mvalue = null;
			String data = null;

			String prefix = "alarm.code.";

			while (e.hasNext()) {
				HashMap<String, Object> m = e.next();
				String json = (String) m.get("data");

				if (json != null) {
					try {
						// 2018-11-14 김원태 변경 알람메세지 
						data = "";
						mdata = mapper.readValue(json, new TypeReference<Map<String, Map<String, String>>>() {
						});
						Iterator<String> keys = mdata.keySet().iterator();

						while (keys.hasNext()) {
							String channelid = keys.next();
							mvalue = mdata.get(channelid);

							Iterator<String> mkeys = mvalue.keySet().iterator();
							while (mkeys.hasNext()) {
								String code = mkeys.next();
								String unit = "";
								String typeCode = "";
								String v = mvalue.get(code);
								String key = prefix + code;
								String channelName = "";

								try {
									String tmp = "channel.code_" + channelid; // 임시
									// System.err.println("tmp=" + tmp);
									channelName = messageSource.getMessage(tmp, null, LocaleContextHolder.getLocale());
								} catch (Exception ec) {
									channelName = channelid;
								}

								try {
									String tmp = "alarm.code." + code;
									code = messageSource.getMessage(tmp, null, LocaleContextHolder.getLocale());

									String tmp2 = "type.code_" + channelid;
									unit = messageSource.getMessage(tmp2, null, LocaleContextHolder.getLocale());

								} catch (Exception ec) {

								}
								if (v.equals("")) {
									// 0번알람 (시스템 영역은 현재값이라는게 없음)
									data += String.format("%s:%s", channelName, code);
								} else {
									if (v.equals("ERR")) {
										data += String.format("%s:%s(%s)", channelName, code, v);
									} else {
										data += String.format("%s:%s(%s)%s", channelName, code, v, unit);
									}
								}

								if (mkeys.hasNext()) {
									data += ",";
								} else {

								}

							}

							if (keys.hasNext()) {
								data += ",";
							} else {

							}
						}
						// 2018-11-14 김원태 변경 알람메세지  end

					} catch (Exception ex) {
						data = "";
					}
				}

				if (data.length() > 2) {
					//data = data.substring(0, data.length() - 2); //2018-11-14 김원태 변경 
					m.put("data", data);
				}
			}

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//ret.setResult(false);
			//ret.setMessage("error");
			ret.setData(null);
			return ret;
		}

		
		
		//여기서부터 그리드 형태로 바꾸는 것 데이터 생성
		List<Object> result2 = new ArrayList<Object>();
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		while (e2.hasNext()) {
			AlarmMsgGridVO test = new AlarmMsgGridVO();
			HashMap<String, Object> m = e2.next();
		
			int seqnoInt = (int) m.get("seqno");
			long seqno = Long.valueOf(seqnoInt);
			String regdate = (String) m.get("regdate");
			String devicename = (String) m.get("devicename");
			String data = (String) m.get("data");
			
			test.setSeqno(seqno);
			test.setRegdate(regdate);
			test.setDevicename(devicename);
			test.setAlarmmessage(data);
			result2.add(test);
			
		}
		
		ret.setData(result2);

		return ret;
		
	}
	
	
}
