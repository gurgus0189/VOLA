package com.vitcon.openapi.device.manual;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.device.DeviceManualVO;
import com.vitcon.service.device.manual.DeviceManualService;

@RestController
@RequestMapping("/openapi/device/manual")
public class DeviceManualController {
	
	@Autowired
	private DeviceManualService deviceMannualService;
	
	@RequestMapping("/categorylist")
	public ResponseObject categorylist() {
		ResponseObject ret = new ResponseObject();
		try {
			ret.setData(deviceMannualService.list());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}
	
	@RequestMapping("/list")
	public ResponseObject list(@ModelAttribute DeviceManualVO vo) {
		ResponseObject ret = new ResponseObject();
		try {
			ret.setData(deviceMannualService.manuallist(vo));
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}
	
}
