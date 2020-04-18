package com.matag.admin.game.session;

import lombok.Value;

@Value
public class GamePlayers {
  private final GameSession playerSession;
  private final GameSession opponentSession;
}
