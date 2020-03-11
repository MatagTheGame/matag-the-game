package com.matag.admin.auth.login;

import lombok.Value;

@Value
public class LoginResponse {
  private final String token;

  public LoginResponse(String token) {
    this.token = token;
  }
}
