package com.matag.admin.game.session;

import com.matag.admin.game.game.Game;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GameSessionService {
  private final GameSessionRepository gameSessionRepository;

  public GamePlayers loadPlayers(Game game) {
    List<GameSession> gameSessions = gameSessionRepository.findByGame(game);
    GameSession playerGameSession = gameSessions.get(0);
    GameSession opponentGameSession = null;
    if (gameSessions.size() > 1) {
      opponentGameSession = gameSessions.get(1);
    }

    return new GamePlayers(playerGameSession, opponentGameSession);
  }
}
