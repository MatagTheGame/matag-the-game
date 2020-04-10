package com.matag.admin.auth;

import com.matag.admin.session.MatagSession;
import com.matag.admin.user.MatagUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHolderHelper {
  public MatagUser getUser() {
    return (MatagUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public MatagSession getSession() {
    return (MatagSession) SecurityContextHolder.getContext().getAuthentication().getCredentials();
  }
}
