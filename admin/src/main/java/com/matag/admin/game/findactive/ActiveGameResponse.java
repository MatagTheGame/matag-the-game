package com.matag.admin.game.findactive;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = ActiveGameResponse.ActiveGameResponseBuilder.class)
@Builder(toBuilder = true)
public class ActiveGameResponse {
  private final Long gameId;
  private final LocalDateTime createdAt;
  private final String playerName;
  private final String playerOptions;
  private final String opponentName;
  private final String opponentOptions;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ActiveGameResponseBuilder {

  }
}
