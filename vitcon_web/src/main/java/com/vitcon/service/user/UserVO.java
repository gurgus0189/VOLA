package com.vitcon.service.user;

public class UserVO {

	private String userid;
	private String username;
	private String passwd;
	private Integer grade;
	private String puserid;
	private String startdate;
	private String enddate;
	private String mobile;
	private Integer pushyn;
	private Integer pushsysyn;
	private String localecd;
	private String portalid;
	private String organization;
	private String registrationtype;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public String getPuserid() {
		return puserid;
	}
	public void setPuserid(String puserid) {
		this.puserid = puserid;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getPushyn() {
		return pushyn;
	}
	public void setPushyn(Integer pushyn) {
		this.pushyn = pushyn;
	}
	public Integer getPushsysyn() {
		return pushsysyn;
	}
	public void setPushsysyn(Integer pushsysyn) {
		this.pushsysyn = pushsysyn;
	}
	public String getLocalecd() {
		return localecd;
	}
	public void setLocalecd(String localecd) {
		this.localecd = localecd;
	}
	public String getPortalid() {
		return portalid;
	}
	public void setPortalid(String portalid) {
		this.portalid = portalid;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getRegistrationtype() {
		return registrationtype;
	}
	public void setRegistrationtype(String registrationtype) {
		this.registrationtype = registrationtype;
	}
	
	@Override
	public String toString() {
		return "UserVO [userid=" + userid + ", username=" + username + ", passwd=" + passwd + ", grade=" + grade
				+ ", puserid=" + puserid + ", startdate=" + startdate + ", enddate=" + enddate + ", mobile=" + mobile
				+ ", pushyn=" + pushyn + ", pushsysyn=" + pushsysyn + ", localecd=" + localecd + ", portalid="
				+ portalid + ", organization=" + organization + ", registrationtype=" + registrationtype + "]";
	}
	
	/*@Override
	public String toString() {
		return "UserVO [userid=" + userid + ", username=" + username + ", passwd=" + passwd + ", grade=" + grade
				+ ", puserid=" + puserid + ", startdate=" + startdate + ", enddate=" + enddate + ", mobile=" + mobile
				+ ", pushyn=" + pushyn + ", localecd=" + localecd + ", portalid=" + portalid + ", organization="
				+ organization + ", registrationtype=" + registrationtype + "]";
	}*/
	
	

	

}
