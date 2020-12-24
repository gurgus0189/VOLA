package com.vitcon.service.device;

import com.vitcon.service.kindrecord.CalcDataVO;
import com.vitcon.service.user.UserVO;

public class StatisVO {

	private UserVO user;
	private DeviceVO device;
	private DeviceGroupVO devicegroup;
	private DeviceAlarmVO devicealarm;
	private DeviceDataVO devicedata;
	private CalcDataVO calcdata;
	
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
	public DeviceVO getDevice() {
		return device;
	}
	public void setDevice(DeviceVO device) {
		this.device = device;
	}
	public DeviceGroupVO getDevicegroup() {
		return devicegroup;
	}
	public void setDevicegroup(DeviceGroupVO devicegroup) {
		this.devicegroup = devicegroup;
	}
	public DeviceAlarmVO getDevicealarm() {
		return devicealarm;
	}
	public void setDevicealarm(DeviceAlarmVO devicealarm) {
		this.devicealarm = devicealarm;
	}
	public DeviceDataVO getDevicedata() {
		return devicedata;
	}
	public void setDevicedata(DeviceDataVO devicedata) {
		this.devicedata = devicedata;
	}
	public CalcDataVO getCalcdata() {
		return calcdata;
	}
	public void setCalcdata(CalcDataVO calcdata) {
		this.calcdata = calcdata;
	}

}
