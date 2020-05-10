package com.matag.game.player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.game.cardinstance.CardInstance;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@JsonDeserialize(builder = PlayerUpdateEvent.PlayerUpdateEventBuilder.class)
@Builder(toBuilder = true)
public class PlayerUpdateEvent {
  String name;
  int life;
  int librarySize;
  List<CardInstance> battlefield;
  List<CardInstance> graveyard;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PlayerUpdateEventBuilder {

  }
}
