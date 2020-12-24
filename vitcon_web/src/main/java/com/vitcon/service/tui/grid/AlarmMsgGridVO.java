package com.vitcon.service.tui.grid;

import java.util.Date;

public class AlarmMsgGridVO {
		
   private long seqno;	
   private String regdate;
   private String devicename;
   private String alarmmessage;
   
   public long getSeqno() {
	  return seqno;
   }
   public void setSeqno(long seqno) {
	  this.seqno = seqno;
   }
   public String getRegdate() {
      return regdate;
   }
   public void setRegdate(String regdate) {
      this.regdate = regdate;
   }
   public String getDevicename() {
      return devicename;
   }
   public void setDevicename(String devicename) {
      this.devicename = devicename;
   }
   public String getAlarmmessage() {
      return alarmmessage;
   }
   public void setAlarmmessage(String alarmmessage) {
      this.alarmmessage = alarmmessage;
   }

}
