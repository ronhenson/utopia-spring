package com.smoothstack.orchestrator.security;

import org.springframework.security.core.Authentication;

public class SecurityUtils {

  public static String getRole(Authentication auth) {
    return auth.getAuthorities().iterator().next().getAuthority();
  }
  
}
