package com.vitcon.openapi.requestrules;

/**
 * @author : 이진우
 * @Date : 2017/12/20
 * 
 *       Copyright (c) 2016 Nexmotion, Inc All rights reserved.
 *
 *       REVISION HISTORY (reverse chronological order)
 *       =============================================================================
 *       이진우 작성 : RequestRulesMapper.java
 *       =============================================================================
 */
public interface RequestRulesMapper {
  /**
   * <pre>
   * 메소드 기능 : 규칙 입력
   * </pre>
   * 
   * @param requestRule
   * @throws Throwable
   */
  public void insertRequestRule(RequestRules requestRule) throws Throwable;

  /**
   * <pre>
   * 메소드 기능 : 마지막에 등록된 규칙을 가져온다.
   * </pre>
   * 
   * @return
   * @throws Throwable
   */
  public RequestRules getLastRequestRule() throws Throwable;
}