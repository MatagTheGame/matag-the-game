package com.matag.admin.user.profile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = CurrentUserProfileDto.CurrentUserProfileDtoBuilder.class)
@Builder(toBuilder = true)
public class CurrentUserProfileDto {
  private final String username;
  private final String type;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CurrentUserProfileDtoBuilder {

  }
}
