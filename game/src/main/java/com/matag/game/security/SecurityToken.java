package com.matag.game.security;

import lombok.Value;

@Value
public class SecurityToken {
  private final String sessionId;
  private final String token;
  private final String gameId;
}
