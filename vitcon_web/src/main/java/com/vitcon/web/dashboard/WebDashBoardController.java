package com.vitcon.web.dashboard;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vitcon.service.device.DeviceService;

@Controller
@RequestMapping("/dashboard/")
public class WebDashBoardController {
	
	@Autowired 
	private DeviceService deviceService;
	
	/**
	 * 대쉬보드
	 * @return
	 */
	@RequestMapping("listview.do")
	public String listView(HttpSession session, Model model, HttpServletRequest request) {
		Integer grade = (Integer) session.getAttribute("grade");
		
		if (grade != null && grade < 20) {
			//구매자
			model.addAttribute("mAddShow", true);			
		}
		model.addAttribute("grade", grade);
		
		if (request.getParameter("menu_listview") == null) {
			return "redirect:/dashboard/listview.do?menu_listview=1";
		}
		/*model.addAttribute("menu_listview", 1);*/
				
		return "dashboard/listview";		
	}
	
	@RequestMapping("listviewdetail.do")
	public String listViewDetail(HttpSession session, Model model, @RequestParam String deviceid,
			@RequestParam String iconpath) {
		
		Integer grade = (Integer) session.getAttribute("grade");
		
		if (grade != null && grade < 20) {
			//구매자
			model.addAttribute("mAddShow", true);			
		}
		model.addAttribute("grade", grade);
		
		HashMap<String, Object> deviceVO;
		
		String devicename = "";
		String groupname = "";
		String devicetypecd = ""; 
		
		try {			
			deviceVO = deviceService.getDevice(deviceid);
			devicename = (String) deviceVO.get("devicename");
			groupname = (String) deviceVO.get("groupname");
			devicetypecd = String.valueOf(deviceVO.get("devicetypecd"));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("deviceid", deviceid);
		model.addAttribute("devicename", devicename);		
		model.addAttribute("groupname", groupname);
		model.addAttribute("devicetypecd", devicetypecd);
		model.addAttribute("iconpath", iconpath);
		
		return "dashboard/listviewdetail";
	}
}
