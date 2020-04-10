package com.matag.admin.game;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = JoinGameResponse.JoinGameResponseBuilder.class)
@Builder(toBuilder = true)
public class JoinGameResponse {
  private final Long gameId;
  private final String error;
  private final Long activeGameId;

  @JsonPOJOBuilder(withPrefix = "")
  public static class JoinGameResponseBuilder {

  }
}
