package com.vitcon.web.sensor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriTemplate;

@Controller
public class WebSensorController {
	
	@RequestMapping("/*/{url}.do")
	public String getSensorList(HttpSession session, Model model, @PathVariable String url, HttpServletRequest request) {	
		
		Integer grade = (Integer) session.getAttribute("grade");
		
		if (grade != null && grade < 20) {
			//구매자
			model.addAttribute("mAddShow", true);			
		}
		model.addAttribute("grade", grade);
		
		String returnUrl = "";
		String restOfTheUrl = (String) request.getAttribute(
	    HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	    UriTemplate template = new UriTemplate("/{value}/{url}");        
	    boolean isTemplateMatched = template.matches(restOfTheUrl);
	    if (isTemplateMatched) {
	        Map<String, String> matchTemplate = new HashMap<String, String>();
	        matchTemplate = template.match(restOfTheUrl);
	        returnUrl += matchTemplate.get("value");
	    }
	    returnUrl += "/" + url;
		return returnUrl;
	}
}
