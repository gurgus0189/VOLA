package com.vitcon.service.device;

public class DeviceManualVO {
	private int seqno;
	private int devicetypecd; 
	private String imagepath;
	public int getSeqno() {
		return seqno;
	}
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}
	public int getDevicetypecd() {
		return devicetypecd;
	}
	public void setDevicetypecd(int devicetypecd) {
		this.devicetypecd = devicetypecd;
	}
	public String getImagepath() {
		return imagepath;
	}
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	@Override
	public String toString() {
		return "DeviceManualVO [seqno=" + seqno + ", devicetypecd=" + devicetypecd + ", imagepath=" + imagepath + "]";
	}

	
}
