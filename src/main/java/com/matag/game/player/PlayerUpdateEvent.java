package com.matag.game.player;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.game.cardinstance.CardInstance;

import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = PlayerUpdateEvent.PlayerUpdateEventBuilder.class)
@Builder
public class PlayerUpdateEvent {
  String name;
  int life;
  int librarySize;
  List<CardInstance> visibleLibrary;
  List<CardInstance> hand;
  List<CardInstance> battlefield;
  List<CardInstance> graveyard;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PlayerUpdateEventBuilder {}
}
