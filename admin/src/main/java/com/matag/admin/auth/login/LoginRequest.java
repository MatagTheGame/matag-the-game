package com.matag.admin.auth.login;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = LoginRequest.LoginRequestBuilder.class)
@Builder(toBuilder = true)
public class LoginRequest {
  private final String email;
  private final String password;

  @JsonPOJOBuilder(withPrefix = "")
  public static class LoginRequestBuilder {

  }
}
