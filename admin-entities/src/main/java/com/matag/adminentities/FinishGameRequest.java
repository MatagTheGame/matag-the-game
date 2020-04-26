package com.matag.adminentities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = FinishGameRequest.FinishGameRequestBuilder.class)
@Builder(toBuilder = true)
public class FinishGameRequest {
  private final String winnerSessionId;

  @JsonPOJOBuilder(withPrefix = "")
  public static class FinishGameRequestBuilder {

  }
}
