package com.matag.admin.auth.login;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
  private final String username;
  private final String password;

  @JsonCreator
  public LoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password) {
    this.username = username;
    this.password = password;
  }
}
