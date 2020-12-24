package com.vitcon.openapi.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.notice.NoticeMapper;
import com.vitcon.service.notice.NoticeService;
import com.vitcon.service.notice.NoticeVO;
import com.vitcon.service.tui.grid.CommonMapper;
import com.vitcon.service.tui.grid.Data;
import com.vitcon.service.tui.grid.Pagination;
import com.vitcon.service.tui.grid.ResponseGridVO;





@RestController
@RequestMapping("/openapi/notice/")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	NoticeMapper noticeMapper;

	@Autowired
	private CommonMapper commonmapper;
	
	@RequestMapping("list")
	public ResponseGridVO getNoticeList(HashMap<String, Object> map,
			@RequestParam("page") int page,
			@RequestParam("perPage") int perPage,
			@RequestParam("localecd") String localecd,HttpSession session, Model model) throws Throwable {
		
		int total =0;
		if(page <=0) {
			page = 1;
		}
		ResponseGridVO result = new ResponseGridVO();
		List<Object> result2 = new ArrayList<Object>();
		
		//HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("page", ((page-1)*perPage));
		map.put("perPage", perPage);
		map.put("localecd",localecd);
		
		List<HashMap<String, Object>> list = noticeService.getSelectNotice(map);
		total = commonmapper.pagingTotal();
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		
		while (e2.hasNext()) {
			NoticeVO testVO = new NoticeVO();
			HashMap<String, Object> m = e2.next();
			int seqno = (int) m.get("seqno");
			String classification = (String) m.get("classification");
			String noticetitle = (String) m.get("noticetitle");
			String noticecontent = (String) m.get("noticecontent");
			String regdate = (String) m.get("regdate");
			
			testVO.setSeqno(seqno);
			testVO.setClassification(classification);
			testVO.setNoticetitle(noticetitle);
			testVO.setNoticecontent(noticecontent);
			testVO.setRegdate(regdate);
			result2.add(testVO);
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
	
	/*@RequestMapping("detaillist")
	public ResponseGridVO getDetailNoticeList(HashMap<String, Object> map,
			@RequestParam("seqno") int number) throws Throwable {
		
		ResponseGridVO result = new ResponseGridVO();
		List<Object> result2 = new ArrayList<Object>();
		
		map.put("seqno", number); // js에서 파라미터로 넘겨준 seqno값
		
		List<HashMap<String, Object>> list = noticeService.getSelectDetailNotice(map);
		Iterator<HashMap<String, Object>> e2 = list.iterator();
		
		while (e2.hasNext()) {
			NoticeVO testVO = new NoticeVO();
			HashMap<String, Object> m = e2.next();
			int seqno = (int) m.get("seqno");
			String classification = (String) m.get("classification");
			String noticetitle = (String) m.get("noticetitle");
			String noticecontent = (String) m.get("noticecontent");
			String regdate = (String) m.get("regdate");
			
			testVO.setSeqno(seqno);
			testVO.setClassification(classification);
			testVO.setNoticetitle(noticetitle);
			testVO.setNoticecontent(noticecontent);
			testVO.setRegdate(regdate);
			result2.add(testVO);
		}
	
		result.setResult(true);
		
		Data data2 = new Data();
		data2.setContents(result2);
		result.setData(data2);
		
		return result;
	}*/
	
	/*@RequestMapping("noticecontent")
	public ResponseObject getSelectNoticeContentList (@ModelAttribute NoticeVO noticeVO,
			@RequestParam("seqno") int number) {
		ResponseObject ret = new ResponseObject();
		//HashMap<String, Object> map = new HashMap<>();
		
		//noticeVO.setSeqno(number);
		
		NoticeVO testVO = new NoticeVO();
		testVO.setSeqno(number);
		
		//ret.setData(deviceMannualService.manuallist(vo));
		try {
			noticeService.getSelectNoticeContent(testVO);
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return ret;
	}*/

	@RequestMapping("noticecontent")
	public ResponseObject getSelectNoticeContentList (@ModelAttribute NoticeVO noticeVO) {
		ResponseObject ret = new ResponseObject();

		try {
			ret.setData(noticeService.getSelectNoticeContent(noticeVO));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	/*@RequestMapping(value="update",
			method=RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseObject updateNotice (@RequestBody MultiValueMap<String, String> formData) {
		ResponseObject ret = new ResponseObject();
	
		String updateData = formData.getFirst("updatedRows");
		//System.out.println(updateData);
		
		
		
		try {
			JSONArray array = new JSONArray(updateData);
			System.out.println(array);
			
			int array_size = array.length();
			
			String[] seqnoArr = new String[array_size];
			
			for(int i=0; i<array_size; i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				seqnoArr[i] = jsonObject.getString("classification");
				System.out.println(seqnoArr[i]);
				
				
				
				
				noticeService.updateNotice(seqnoArr[i]);
				
				
			}
			
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//mapData = formData.getFirst("updatedRows");
		//System.out.println(mapData);
		
		return ret;
	}
	
	
	
	
	
	
	@RequestMapping("excelAll")
	public ResponseObject excelAll (HashMap<String, Object> map) {
		ResponseObject ret = new ResponseObject();

		System.out.println(map);
		
		try {
			ret.setData(noticeService.getSelectNoticeExcelAll(map));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}*/
	
	
	
	
	
	
	/*@RequestMapping("deleteNotice")
	public ResponseObject deleteNotice (NoticeVO noticeVO) {
		ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<>();
		
		try {
			noticeService.deleteNotice(noticeVO);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	@RequestMapping("updateNotice")
	public ResponseObject updateNotice (NoticeVO noticeVO) {
		ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<>();
	
		map.put("classification", noticeVO.getClassification());
		map.put("noticecontent", noticeVO.getNoticecontent());

		try {
			noticeService.updateNotice(map);
			//noticeService.updateNotice(map);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	@RequestMapping("insertNotice")
	public ResponseObject insertNotice (NoticeVO noticeVO) {
		ResponseObject ret = new ResponseObject();
		HashMap<String, Object> map = new HashMap<>();
		
		try {
			noticeService.insertNotice(noticeVO);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}*/
	
}