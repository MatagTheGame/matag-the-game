package com.matag.admin.user.profile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = MatagCurrentUserProfileDto.MatagCurrentUserProfileDtoBuilder.class)
@Builder(toBuilder = true)
public class MatagCurrentUserProfileDto {
  private final String username;

  @JsonPOJOBuilder(withPrefix = "")
  public static class MatagCurrentUserProfileDtoBuilder {

  }
}
