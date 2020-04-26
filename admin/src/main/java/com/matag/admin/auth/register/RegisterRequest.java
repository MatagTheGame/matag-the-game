package com.matag.admin.auth.register;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = RegisterRequest.RegisterRequestBuilder.class)
@Builder(toBuilder = true)
public class RegisterRequest {
  private final String email;
  private final String username;
  private final String password;

  @JsonPOJOBuilder(withPrefix = "")
  public static class RegisterRequestBuilder {

  }
}
