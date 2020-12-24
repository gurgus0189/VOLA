package com.vitcon.service.notice;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.vitcon.service.device.DeviceManualVO;
import com.vitcon.service.device.DeviceVO;

@Service
public class NoticeService implements NoticeMapper {
	
	@Autowired 
	private NoticeMapper mapper;
	
	@Override
	public List<HashMap<String, Object>> getSelectNotice(HashMap<String, Object> map) throws Throwable {
		return mapper.getSelectNotice(map);
	}
	
	/*@Override
	public NoticeVO getSelectNotice(NoticeVO noticeVO) throws Throwable {
		return mapper.getSelectNotice(noticeVO);
	}*/
	
	/*@Override
	public List<HashMap<String, Object>> getSelectDetailNotice(HashMap<String, Object> map) throws Throwable {
		return mapper.getSelectDetailNotice(map);
	}*/
	
	public List<NoticeVO> getSelectNoticeContent(NoticeVO noticeVO) {
		return mapper.getSelectNoticeContent(noticeVO);
	}

	/*@Override
	public void updateNotice(String string) throws Throwable {
		mapper.updateNotice(string);
		
	}

	public List<HashMap<String, Object>> getSelectNoticeExcelAll(HashMap<String, Object> map) throws Throwable {
		return mapper.getSelectNoticeExcelAll(map);
	}*/
	
	
	
	// grid 업데이트
	/*public MultiValueMap<String, NoticeUpdateVO> updateNotice(MultiValueMap<String, NoticeUpdateVO> formData)  throws Throwable {
		return mapper.updateNotice(formData);
	}*/
	
	/*@Override
	public void deleteNotice(NoticeVO noticeVO) throws Throwable {
		mapper.deleteNotice(noticeVO);
	}

	@Override
	public void updateNotice(HashMap<String, Object> map) throws Throwable {
		mapper.updateNotice(map);
	}
	
	@Override
	public void insertNotice(NoticeVO noticeVO) throws Throwable {
		mapper.insertNotice(noticeVO);
	}*/

}
