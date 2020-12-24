package com.vitcon.service.device;

public class DeviceChannelVO {
	private int devicetypecd;	
	private int channelid;	            
	private String channelnamecd;	
	private String commandcd;
	public int getDevicetypecd() {
		return devicetypecd;
	}
	public void setDevicetypecd(int devicetypecd) {
		this.devicetypecd = devicetypecd;
	}
	public int getChannelid() {
		return channelid;
	}
	public void setChannelid(int channelid) {
		this.channelid = channelid;
	}
	public String getChannelnamecd() {
		return channelnamecd;
	}
	public void setChannelnamecd(String channelnamecd) {
		this.channelnamecd = channelnamecd;
	}
	public String getCommandcd() {
		return commandcd;
	}
	public void setCommandcd(String commandcd) {
		this.commandcd = commandcd;
	}
	@Override
	public String toString() {
		return "DeviceChannelVO [devicetypecd=" + devicetypecd + ", channelid=" + channelid + ", channelnamecd="
				+ channelnamecd + ", commandcd=" + commandcd + "]";
	}
	
	
}
