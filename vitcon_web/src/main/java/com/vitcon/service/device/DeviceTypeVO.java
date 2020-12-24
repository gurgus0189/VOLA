package com.vitcon.service.device;

public class DeviceTypeVO {
	private int devicetypecd;
	private String devicetypename;
	private int deviceiconid;
	public int getDevicetypecd() {
		return devicetypecd;
	}
	public void setDevicetypecd(int devicetypecd) {
		this.devicetypecd = devicetypecd;
	}
	public String getDevicetypename() {
		return devicetypename;
	}
	public void setDevicetypename(String devicetypename) {
		this.devicetypename = devicetypename;
	}
	public int getDeviceiconid() {
		return deviceiconid;
	}
	public void setDeviceiconid(int deviceiconid) {
		this.deviceiconid = deviceiconid;
	}
	@Override
	public String toString() {
		return "DeviceTypeVO [devicetypecd=" + devicetypecd + ", devicetypename=" + devicetypename + ", deviceiconid="
				+ deviceiconid + "]";
	}
	
	

}
