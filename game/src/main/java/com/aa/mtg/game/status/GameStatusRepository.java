package com.aa.mtg.game.status;

import com.aa.mtg.game.security.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameStatusRepository {

  private final SecurityHelper securityHelper;

  @Autowired
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
}
