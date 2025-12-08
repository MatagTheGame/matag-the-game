package com.matag.game.player;

import com.matag.game.cardinstance.CardInstance;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlayerUpdateEvent {
  String name;
  int life;
  int librarySize;
  List<CardInstance> visibleLibrary;
  List<CardInstance> hand;
  List<CardInstance> battlefield;
  List<CardInstance> graveyard;
}
