package com.vitcon.service.statis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

	public boolean isMSIE58(String userAgent) {
		String pattern = "/MSIE [5-8]/i";
		return Pattern.matches(pattern, userAgent);
	}

	
	// 엑셀 다운로드 헤더 부분 설정
	public boolean setHeader(HttpServletResponse response, HttpServletRequest request, String filename) {
		try {		
		    String userAgent = request.getHeader("User-Agent");			
		    String hdfile = filename + ".csv";
		    						 
		    if (userAgent != null && isMSIE58(userAgent)) {
		      response.setHeader("Cache-Control", "private, must-revalidate, post-check=0, pre-check=0");
		      response.setHeader("Expires","Sat, 01 Jan 2000 00:00:00 GMT");
		    } else {
		      response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
		      response.setHeader("Expires","Sat, 01 Jan 2000 00:00:00 GMT");
		    }
		    
		    response.setHeader("Expires", "Sat, 01 Jan 2000 00:00:00 GMT");
		    response.setHeader("Accept-Ranges", "bytes");
		    
		    response.setContentType("Content-Type: application/octet-stream");
		    response.setHeader("Content-Disposition" , "attachment;filename=\""
		    		+new String(hdfile.getBytes("UTF-8"),"iso-8859-1")+"\"");			
		    response.setHeader("Cache-Control","no-cache");
		    response.setHeader("Expires", "Sat,01 jan 2000 00:00:00 GMT");
		} catch(Exception e) {			
			return false;
		}
		
		return true;
	}

	// 엑셀을 그려내기 시작한다.
	public boolean excelDown(HttpServletRequest request, HttpServletResponse response, String filename, ArrayList<String> list) {		
		if (!setHeader(response, request, filename))
			return false;
		
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream(); 
			//일본어 BOM떄문에 추가
			out.write(0xEF);
			out.write(0xBB);
			out.write(0xBF);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		Iterator<String> e = list.iterator();
		while (e.hasNext()) {
			String row = e.next();
			try {
				row += "\n";
//				out.write(row.getBytes("EUC-KR"));
				out.write(row.getBytes("UTF-8"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		return true;
	}
	
	
	
}
