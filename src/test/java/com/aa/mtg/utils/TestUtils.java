package com.aa.mtg.utils;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public class TestUtils {

  public static SimpMessageHeaderAccessor sessionHeader(String sessionId) {
    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
    headerAccessor.setSessionId(sessionId);
    return headerAccessor;
  }
}