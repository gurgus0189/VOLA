package com.vitcon.service.device;

public class DeviceChannelRangeVO {
	private String deviceid;
	private Integer devicetypecd;
	private Integer channelid;
	private String channelrangedata;
	private String channelrangedatauser;
	private String channelrangedatadefault;
	
	public String getChannelrangedatauser() {
		return channelrangedatauser;
	}
	public void setChannelrangedatauser(String channelrangedatauser) {
		this.channelrangedatauser = channelrangedatauser;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public Integer getDevicetypecd() {
		return devicetypecd;
	}
	public void setDevicetypecd(Integer devicetypecd) {
		this.devicetypecd = devicetypecd;
	}
	public Integer getChannelid() {
		return channelid;
	}
	public void setChannelid(Integer channelid) {
		this.channelid = channelid;
	}
	public String getChannelrangedata() {
		return channelrangedata;
	}
	public void setChannelrangedata(String channelrangedata) {
		this.channelrangedata = channelrangedata;
	}
	@Override
	public String toString() {
		return "DeviceChannelRangeVO [deviceid=" + deviceid + ", devicetypecd=" + devicetypecd + ", channelid="
				+ channelid + ", channelrangedata=" + channelrangedata + "]";
	}
	public String getChannelrangedatadefault() {
		return channelrangedatadefault;
	}
	public void setChannelrangedatadefault(String channelrangedatadefault) {
		this.channelrangedatadefault = channelrangedatadefault;
	}
	
	
}
