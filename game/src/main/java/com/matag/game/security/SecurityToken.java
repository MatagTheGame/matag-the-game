package com.matag.game.security;

import lombok.Value;

@Value
public class SecurityToken {
  private final String sessionId;
  private final String adminToken;
  private final String gameId;

  @Override
  public String toString() {
    return "SecurityToken{" +
      "sessionId='********'" +
      ", adminToken='********'" +
      ", gameId='" + gameId + '\'' +
      '}';
  }
}
