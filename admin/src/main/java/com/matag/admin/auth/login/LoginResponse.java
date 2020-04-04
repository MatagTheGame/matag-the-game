package com.matag.admin.auth.login;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.admin.user.profile.MatagCurrentUserProfileDto;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = LoginResponse.LoginResponseBuilder.class)
@Builder(toBuilder = true)
public class LoginResponse {
  private final String token;
  private final MatagCurrentUserProfileDto profile;
  private final String error;

  @JsonPOJOBuilder(withPrefix = "")
  public static class LoginResponseBuilder {

  }
}
