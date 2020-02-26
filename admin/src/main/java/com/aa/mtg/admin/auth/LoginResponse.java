package com.aa.mtg.admin.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {
  private final String token;

  @JsonCreator
  public LoginResponse(@JsonProperty("token") String token) {
    this.token = token;
  }
}
