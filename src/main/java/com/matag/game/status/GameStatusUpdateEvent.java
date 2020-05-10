package com.matag.game.status;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.PlayerUpdateEvent;
import com.matag.game.turn.Turn;
import lombok.Builder;
import lombok.Value;

import java.util.LinkedList;
import java.util.Set;

@Value
@JsonDeserialize(builder = GameStatusUpdateEvent.GameStatusUpdateEventBuilder.class)
@Builder(toBuilder = true)
public class GameStatusUpdateEvent {
  Turn turn;
  LinkedList<CardInstance> stack;
  Set<PlayerUpdateEvent> playersUpdateEvents;

  @JsonPOJOBuilder(withPrefix = "")
  public static class GameStatusUpdateEventBuilder {

  }
}
