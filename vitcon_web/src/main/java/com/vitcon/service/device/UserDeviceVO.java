package com.vitcon.service.device;

public class UserDeviceVO {
	private String userid;
	private String deviceid;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	@Override
	public String toString() {
		return "UserDeviceVO [userid=" + userid + ", deviceid=" + deviceid + "]";
	}
	
}
