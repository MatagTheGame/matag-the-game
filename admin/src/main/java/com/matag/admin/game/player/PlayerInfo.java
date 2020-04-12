package com.matag.admin.game.player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = PlayerInfo.PlayerInfoBuilder.class)
@Builder(toBuilder = true)
public class PlayerInfo {
  private final String playerName;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PlayerInfoBuilder {

  }
}
