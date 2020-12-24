package com.vitcon.service.device;

public class UserAppVO {
	
   private String regdate;
   private String appid;
   private String userid;
   private String token;
   private String appversion;
   private String osversion;
   private String modelname;
   private String apptype;
   
   public String getRegdate() {
      return regdate;
   }
   public void setRegdate(String regdate) {
      this.regdate = regdate;
   }
   public String getAppid() {
      return appid;
   }
   public void setAppid(String appid) {
      this.appid = appid;
   }
   public String getUserid() {
      return userid;
   }
   public void setUserid(String userid) {
      this.userid = userid;
   }
   public String getToken() {
      return token;
   }
   public void setToken(String token) {
      this.token = token;
   }
   public String getAppversion() {
      return appversion;
   }
   public void setAppversion(String appversion) {
      this.appversion = appversion;
   }
   public String getOsversion() {
      return osversion;
   }
   public void setOsversion(String osversion) {
      this.osversion = osversion;
   }
   public String getModelname() {
      return modelname;
   }
   public void setModelname(String modelname) {
      this.modelname = modelname;
   }
   public String getApptype() {
      return apptype;
   }
   public void setApptype(String apptype) {
      this.apptype = apptype;
   }
   @Override
   public String toString() {
      return "UserAppVO [appid=" + appid + ", userid=" + userid + ", token=" + token + ", appversion=" + appversion
            + ", osversion=" + osversion + ", modelname=" + modelname + ", apptype=" + apptype + "]";
   }
}
	
	

