package com.matag.admin.game.cancel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = CancelGameResponse.CancelGameResponseBuilder.class)
@Builder(toBuilder = true)
public class CancelGameResponse {
  private final String error;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CancelGameResponseBuilder {

  }
}
