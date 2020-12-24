package com.vitcon.service.device;

public class DeviceGroupVO {
	private Integer groupid;
    private String groupname;
    private String userid;
    private String defaultyn;
    private int count ;
 
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Integer getGroupid() {
   	 return groupid;
    }
    public void setGroupid(Integer groupid) {
    	this.groupid = groupid;
    }
    public String getGroupname() {
    	return groupname;
    }
    public void setGroupname(String groupname) {
    	this.groupname = groupname;
    }
    public String getUserid() {
    	return userid;
    }
    public void setUserid(String userid) {
    	this.userid = userid;
    }
    public String getDefaultyn() {
    	return defaultyn;
    }
    public void setDefaultyn(String defaultyn) {
    	this.defaultyn = defaultyn;
    }
	@Override
	public String toString() {
		return "DeviceGroupVO [groupid=" + groupid + ", groupname=" + groupname + ", userid=" + userid + ", defaultyn="
				+ defaultyn + ", count=" + count + "]";
	}
    
   
   
    
    
    
}
