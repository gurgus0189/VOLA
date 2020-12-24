package com.vitcon.service.notice;

public class NoticeVO {

	private int seqno;
	private String classification;
	private String noticetitle;
	private String noticecontent;
	private String regdate;
	private String localecd;
	
	public int getSeqno() {
		return seqno;
	}
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getNoticetitle() {
		return noticetitle;
	}
	public void setNoticetitle(String noticetitle) {
		this.noticetitle = noticetitle;
	}
	public String getNoticecontent() {
		return noticecontent;
	}
	public void setNoticecontent(String noticecontent) {
		this.noticecontent = noticecontent;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getLocalecd() {
		return localecd;
	}
	public void setLocalecd(String localecd) {
		this.localecd = localecd;
	}
	
}
