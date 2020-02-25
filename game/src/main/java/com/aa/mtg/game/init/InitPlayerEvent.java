package com.aa.mtg.game.init;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
class InitPlayerEvent {
  private final int librarySize;
  private final List<CardInstance> hand;
  private final List<CardInstance> battlefield;
  private final List<CardInstance> graveyard;
  private final String name;
  private final int life;
  private final String resolution;

  static InitPlayerEvent createForPlayer(Player player) {
    return new InitPlayerEvent(
      player.getLibrary().size(),
      player.getHand().getCards(),
      player.getBattlefield().getCards(),
      player.getGraveyard().getCards(),
      player.getName(),
      player.getLife(),
      player.getResolution()
    );
  }

  static InitPlayerEvent createForOpponent(Player player) {
    return new InitPlayerEvent(
      player.getLibrary().size(),
      player.getHand().maskedHand(),
      player.getBattlefield().getCards(),
      player.getGraveyard().getCards(),
      player.getName(),
      player.getLife(),
      null
    );
  }
}
