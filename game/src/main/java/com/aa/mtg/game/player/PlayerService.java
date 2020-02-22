package com.aa.mtg.game.player;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class PlayerService {
  public Player getPlayerBySessionId(GameStatus gameStatus, String sessionId) {
    if (gameStatus.getPlayer1().getSessionId().equals(sessionId)) {
      return gameStatus.getPlayer1();
    } else if (gameStatus.getPlayer2().getSessionId().equals(sessionId)) {
      return gameStatus.getPlayer2();
    }
    throw new RuntimeException("SessionId " + sessionId + " is not linked to game " + gameStatus.getGameId() + " .");
  }
}
