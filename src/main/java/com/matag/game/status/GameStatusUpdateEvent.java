package com.matag.game.status;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.PlayerUpdateEvent;
import com.matag.game.turn.Turn;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.Set;

@Data
@Builder
public class GameStatusUpdateEvent {
  Turn turn;
  LinkedList<CardInstance> stack;
  Set<PlayerUpdateEvent> playersUpdateEvents;
}
