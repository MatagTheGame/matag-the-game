package com.matag.game.player;

import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class PlayerService {
  public Player getPlayerBySessionId(GameStatus gameStatus, String sessionId) {
    if (gameStatus.getPlayer1().getToken().getSessionId().equals(sessionId)) {
      return gameStatus.getPlayer1();
    } else if (gameStatus.getPlayer2().getToken().getSessionId().equals(sessionId)) {
      return gameStatus.getPlayer2();
    }
    throw new RuntimeException("Not allowed to access gameId: " + gameStatus.getGameId());
  }

  public Player getPlayerByToken(GameStatus gameStatus, String token) {
    if (gameStatus.getPlayer1().getToken().getAdminToken().equals(token)) {
      return gameStatus.getPlayer1();

    } else if (gameStatus.getPlayer2().getToken().getAdminToken().equals(token)) {
      return gameStatus.getPlayer2();

    } else {
      throw new RuntimeException("Not allowed to access gameId: " + gameStatus.getGameId());
    }
  }
}
