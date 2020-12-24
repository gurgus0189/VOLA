package com.vitcon.service.device;

import com.vitcon.service.user.UserVO;

public class AllDeviceListVO {
	
	private DeviceVO device;
	private DeviceTypeVO devicetype;
	private DeviceGroupVO devicegroup;
	private UserVO user;
	private ChannelNameVO channelname;
	private DeviceChannelVO devicechannel;
	private DeviceChannelRangeVO devicechannelrange;
	
	public DeviceChannelRangeVO getDevicechannelrange() {
		return devicechannelrange;
	}
	public void setDevicechannelrange(DeviceChannelRangeVO devicechannelrange) {
		this.devicechannelrange = devicechannelrange;
	}
	public ChannelNameVO getChannelname() {
		return channelname;
	}
	public void setChannelname(ChannelNameVO channelname) {
		this.channelname = channelname;
	}
	public DeviceChannelVO getDevicechannel() {
		return devicechannel;
	}
	public void setDevicechannel(DeviceChannelVO devicechannel) {
		this.devicechannel = devicechannel;
	}
	public DeviceVO getDevice() {
		return device;
	}
	public void setDevice(DeviceVO device) {
		this.device = device;
	}
	public DeviceTypeVO getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(DeviceTypeVO devicetype) {
		this.devicetype = devicetype;
	}
	public DeviceGroupVO getDevicegroup() {
		return devicegroup;
	}
	public void setDevicegroup(DeviceGroupVO devicegroup) {
		this.devicegroup = devicegroup;
	}
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "AllDeviceListVO [device=" + device + ", devicetype=" + devicetype + ", devicegroup=" + devicegroup
				+ ", user=" + user + ", channelname=" + channelname + ", devicechannel=" + devicechannel
				+ ", devicechannelrange=" + devicechannelrange + "]";
	}

	
	

}
