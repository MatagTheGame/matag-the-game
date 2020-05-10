package com.matag.game.init;

import com.matag.game.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class InitPlayerEvent {
  private final String name;

  static InitPlayerEvent create(Player player) {
    return new InitPlayerEvent(player.getName());
  }
}
