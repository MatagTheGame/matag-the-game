package com.matag.game.security;

import com.matag.game.player.Player;
import com.matag.game.player.PlayerService;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class SecurityHelper {
  private final PlayerService playerService;

  public SecurityToken extractSecurityToken(SimpMessageHeaderAccessor headerAccessor) {
    String sessionId = headerAccessor.getSessionId();
    String gameId = Objects.requireNonNull(headerAccessor.getNativeHeader("gameId")).get(0);
    return new SecurityToken(sessionId, gameId);
  }

  public void isPlayerAllowedToExecuteAction(GameStatus gameStatus, String sessionId) {
    Player currentPlayer = playerService.getPlayerBySessionId(gameStatus, sessionId);
    if (!gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(currentPlayer.getName())) {
      throw new SecurityException("Player " + currentPlayer.getName() + " is not allowed to execute an action. turn=[" + gameStatus.getTurn() + "]");
    }
  }
}
