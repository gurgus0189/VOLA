package com.vitcon.service.device;

import java.util.Date;

public class DevicePlanVO {

	private String deviceid;
	private String regdate;
	private int plantype;
	private String plantypestr;
	private String planexp;
	
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
	public String getPlantypestr() {
		return plantypestr;
	}
	public void setPlantypestr(String plantypestr) {
		this.plantypestr = plantypestr;
	}
	public String getPlanexp() {
		return planexp;
	}
	public void setPlanexp(String planexp) {
		this.planexp = planexp;
	}
	
}
