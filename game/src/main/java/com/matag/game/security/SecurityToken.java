package com.matag.game.security;

public class SecurityToken {
  private final String sessionId;
  private final String gameId;

  public SecurityToken(String sessionId, String gameId) {
    this.sessionId = sessionId;
    this.gameId = gameId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public String getGameId() {
    return gameId;
  }
}
