package com.matag.game.status;

import com.matag.game.security.SecurityHelper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameStatusRepository {
  private final SecurityHelper securityHelper;

  public GameStatusRepository(SecurityHelper securityHelper) {
    this.securityHelper = securityHelper;
  }

  private Map<String, GameStatus> games = new HashMap<>();

  public boolean contains(String gameId) {
    return games.containsKey(gameId);
  }

  public void save(String gameId, GameStatus gameStatus) {
    games.put(gameId, gameStatus);
  }

  public GameStatus getUnsecure(String gameId) {
    return games.get(gameId);
  }

  public GameStatus get(String gameId, String sessionId) {
    GameStatus gameStatus = games.get(gameId);
    securityHelper.isPlayerAllowedToExecuteAction(gameStatus, sessionId);
    return gameStatus;
  }

  public void clear() {
    games.clear();
  }
}
