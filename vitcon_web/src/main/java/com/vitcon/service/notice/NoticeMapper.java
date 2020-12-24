package com.vitcon.service.notice;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.MultiValueMap;

import com.vitcon.service.device.DeviceTypeVO;
import com.vitcon.service.device.DeviceVO;

@Mapper
public interface NoticeMapper {
	
	public List<HashMap<String, Object>> getSelectNotice(HashMap<String, Object> map) throws Throwable;

	/*public NoticeVO getSelectNotice(NoticeVO noticeVO) throws Throwable;*/
	
	/*public void getSelectDetailNotice(NoticeVO noticeVO) throws Throwable;*/
	
	//public List<HashMap<String, Object>> getSelectDetailNotice(HashMap<String, Object> map) throws Throwable;
	
	public List<NoticeVO> getSelectNoticeContent(NoticeVO noticeVO);

	/*public void updateNotice(String string) throws Throwable;

	public List<HashMap<String, Object>> getSelectNoticeExcelAll(HashMap<String, Object> map) throws Throwable;*/

	

	

	//grid 업데이트
	/*public MultiValueMap<String, NoticeUpdateVO> updateNotice(MultiValueMap<String, NoticeUpdateVO> formData) throws Throwable;*/
	
	/*public void deleteNotice(NoticeVO noticeVO) throws Throwable;

	public void updateNotice(HashMap<String, Object> map) throws Throwable;
	
	public void insertNotice(NoticeVO noticeVO) throws Throwable;*/

	

	

}
