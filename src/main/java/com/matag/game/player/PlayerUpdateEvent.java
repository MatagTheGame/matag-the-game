package com.matag.game.player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = PlayerUpdateEvent.PlayerUpdateEventBuilder.class)
@Builder(toBuilder = true)
public class PlayerUpdateEvent {
  private String name;
  private int life;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PlayerUpdateEventBuilder {

  }
}
