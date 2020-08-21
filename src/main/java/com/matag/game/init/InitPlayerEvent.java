package com.matag.game.init;

import lombok.Data;

@Data
class InitPlayerEvent {
  private final String name;
  private final GameConfig gameConfig;
}
