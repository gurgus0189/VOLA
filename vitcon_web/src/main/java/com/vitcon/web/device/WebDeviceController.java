package com.vitcon.web.device;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.service.device.DeviceService;
import com.vitcon.service.device.DeviceVO;

@Controller
@RequestMapping("/device/")
public class WebDeviceController {
	
	@Autowired 
	private DeviceService deviceService;
	
	//존재한다면(Y) : 두번째 페이지로 이동한다.(http://www.nexmotion.co.kr/vitcon/07.html참고)
	@RequestMapping("manual.do")
	public String manual(@RequestParam int devicetypecd, @RequestParam String devicetypename, Model model) {
		model.addAttribute("devicetypecd", devicetypecd);
		model.addAttribute("devicetypename", devicetypename);
		return "device/manual";
	}
	
}
