package com.matag.game.status;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.game.turn.Turn;
import lombok.Builder;
import lombok.Data;

@Data
@JsonDeserialize(builder = GameStatusUpdateEvent.GameStatusUpdateEventBuilder.class)
@Builder(toBuilder = true)
public class GameStatusUpdateEvent {
  private Turn turn;

  @JsonPOJOBuilder(withPrefix = "")
  public static class GameStatusUpdateEventBuilder {

  }
}
