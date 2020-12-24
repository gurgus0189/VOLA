package com.vitcon.openapi.requestrules;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author : 이진우
 * @Date : 2017/12/20
 * 
 *       Copyright (c) 2016 Nexmotion, Inc All rights reserved.
 *
 *       REVISION HISTORY (reverse chronological order)
 *       ============================================================================= 
 *       2017/12/20 이진우 작성 : RequestRule.java
 *       =============================================================================
 */
public class RequestRules implements Serializable {

  /**
   * serialize version uid
   */
  private static final long serialVersionUID = -5239518179030673441L;

  private int id;
  private String json;
  private Timestamp regdate;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public Timestamp getRegdate() {
    return regdate;
  }

  public void setRegdate(Timestamp regdate) {
    this.regdate = regdate;
  }
}