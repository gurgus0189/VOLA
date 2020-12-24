package com.vitcon.service.device;

import java.util.Date;
import java.util.List;

public class DeviceVO {
    private String deviceid;
	private String devicename;
	private String wifissid;
	private String wifipasswd;
	private String deviceinfo;
	private String userid;
	private Date createdate;
	private Date updatedate;
	private Integer devicetypecd;
	private Integer groupid;
	private String devicedesc;
	private String deleteflag;
	private int count;
	private int saveinterval;
	private String pushenable;
	private int pushinterval;
	private int pushrepeat;
	private String pushsysenable;
	
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	public String getWifissid() {
		return wifissid;
	}
	public void setWifissid(String wifissid) {
		this.wifissid = wifissid;
	}
	public String getWifipasswd() {
		return wifipasswd;
	}
	public void setWifipasswd(String wifipasswd) {
		this.wifipasswd = wifipasswd;
	}
	public String getDeviceinfo() {
		return deviceinfo;
	}
	public void setDeviceinfo(String deviceinfo) {
		this.deviceinfo = deviceinfo;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}
	public Integer getDevicetypecd() {
		return devicetypecd;
	}
	public void setDevicetypecd(Integer devicetypecd) {
		this.devicetypecd = devicetypecd;
	}
	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	public String getDevicedesc() {
		return devicedesc;
	}
	public void setDevicedesc(String devicedesc) {
		this.devicedesc = devicedesc;
	}
	public String getDeleteflag() {
		return deleteflag;
	}
	public void setDeleteflag(String deleteflag) {
		this.deleteflag = deleteflag;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getSaveinterval() {
		return saveinterval;
	}
	public void setSaveinterval(int saveinterval) {
		this.saveinterval = saveinterval;
	}
	public String getPushenable() {
		return pushenable;
	}
	public void setPushenable(String pushenable) {
		this.pushenable = pushenable;
	}
	public int getPushinterval() {
		return pushinterval;
	}
	public void setPushinterval(int pushinterval) {
		this.pushinterval = pushinterval;
	}
	public int getPushrepeat() {
		return pushrepeat;
	}
	public void setPushrepeat(int pushrepeat) {
		this.pushrepeat = pushrepeat;
	}
	public String getPushsysenable() {
		return pushsysenable;
	}
	public void setPushsysenable(String pushsysenable) {
		this.pushsysenable = pushsysenable;
	}
	
	@Override
	public String toString() {
		return "DeviceVO [deviceid=" + deviceid + ", devicename=" + devicename + ", wifissid=" + wifissid
				+ ", wifipasswd=" + wifipasswd + ", deviceinfo=" + deviceinfo + ", userid=" + userid + ", createdate="
				+ createdate + ", updatedate=" + updatedate + ", devicetypecd=" + devicetypecd + ", groupid=" + groupid
				+ ", devicedesc=" + devicedesc + ", deleteflag=" + deleteflag + ", count=" + count + ", saveinterval="
				+ saveinterval + ", pushenable=" + pushenable + ", pushinterval=" + pushinterval + ", pushrepeat="
				+ pushrepeat + ", pushsysenable=" + pushsysenable + "]";
	}
	                                                                    
}
