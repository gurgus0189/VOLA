package com.vitcon.openapi.sensor.controlrecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.controlrecord.ControlRecordService;

@Controller
@RequestMapping("/openapi/control")
public class ControlRecordController {

	@Autowired
	private ControlRecordService controlRecordService;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		//DateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,true));
	 }	
	
	
	@ResponseBody
	@RequestMapping("/controlrecordlist")
	public ResponseObject getControlRecord(@RequestParam("regdate") String regdate
				 ,@RequestParam("groupid") String groupid
				 ,@RequestParam("devicetypecd") String devicetypecd) {
		
		ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<>();
		
		if(regdate == null || groupid == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}
		
		List<HashMap<String,Object>> list = null;

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
	    
	    try {
			map.put("userid", userid);
			map.put("regdate", regdate);
			map.put("groupid", groupid);
			map.put("devicetypecd", devicetypecd);
			list = controlRecordService.getControlRecord(map);			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			
			return ret;
		}
	    
	    ret.setReturnCode(StatusCode.OK);
		ret.setData(list);
	    
		return ret;
	}
	
	
	
	
}
