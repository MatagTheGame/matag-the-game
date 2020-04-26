package com.matag.admin.auth.admin;

import com.matag.admin.config.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class AdminAuthValidator {
  private final ConfigService configService;

  public void check(String adminPassword) {

  }
}
