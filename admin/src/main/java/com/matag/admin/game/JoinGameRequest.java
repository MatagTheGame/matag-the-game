package com.matag.admin.game;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = JoinGameRequest.JoinGameRequestBuilder.class)
@Builder(toBuilder = true)
public class JoinGameRequest {
  private final GameType gameType;
  private final String playerOptions;

  @JsonPOJOBuilder(withPrefix = "")
  public static class JoinGameRequestBuilder {

  }
}
