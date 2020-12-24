package com.vitcon.core.auth;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author : 김관우
 * @Date : 2016/12/07
 * 
 *  Copyright (c) 2016 Nexmotion, Inc All rights reserved.
 *
 *  REVISION HISTORY (reverse chronological order)
 *  ============================================================================= 
 *  2016/12/07 김관우 작성 : UserAuthentication.java
 *  =============================================================================
 */
public class UserAuthentication implements Authentication {

  private String userid;
  private Collection<? extends GrantedAuthority> authorities;
  private boolean authenticated;

  public UserAuthentication(String userid, Collection<? extends GrantedAuthority> authorities,
      boolean authenticated) {
    // TODO Auto-generated constructor stub
    this.userid = userid;
    this.authorities = authorities;
    this.authenticated = authenticated;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.security.Principal#getName()
   */
  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return userid;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.Authentication#getAuthorities()
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // TODO Auto-generated method stub
    return this.authorities;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.Authentication#getCredentials()
   */
  @Override
  public Object getCredentials() {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.Authentication#getDetails()
   */
  @Override
  public Object getDetails() {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.Authentication#getPrincipal()
   */
  @Override
  public Object getPrincipal() {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.Authentication#isAuthenticated()
   */
  @Override
  public boolean isAuthenticated() {
    // TODO Auto-generated method stub
    return this.authenticated;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.Authentication#setAuthenticated(boolean)
   */
  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    // TODO Auto-generated method stub
    this.authenticated = isAuthenticated;
  }

}
