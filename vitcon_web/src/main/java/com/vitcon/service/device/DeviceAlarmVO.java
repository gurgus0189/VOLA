package com.vitcon.service.device;

import java.util.Date;

public class DeviceAlarmVO {

	private String deviceid;
	private Date regdate;
	private String data;
	private String json;
	
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	
	@Override
	public String toString() {
		return "DeviceAlarm [deviceid=" + deviceid + ", regdate=" + regdate + ", data=" + data + ", json=" + json + "]";
	}

}
