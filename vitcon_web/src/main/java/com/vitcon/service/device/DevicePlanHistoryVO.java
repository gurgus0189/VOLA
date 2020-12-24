package com.vitcon.service.device;

public class DevicePlanHistoryVO {

	private String deviceid;
	private String regdate;
	private int plantype;
	private String planexp;
	private String note;
	
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public int getPlantype() {
		return plantype;
	}
	public void setPlantype(int plantype) {
		this.plantype = plantype;
	}
	public String getPlanexp() {
		return planexp;
	}
	public void setPlanexp(String planexp) {
		this.planexp = planexp;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
