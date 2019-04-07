package com.aa.mtg.game.security;

import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.Objects;

public class SecurityHelper {

    public static SecurityToken extractSecurityToken(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String gameId = Objects.requireNonNull(headerAccessor.getNativeHeader("gameId")).get(0);
        return new SecurityToken(sessionId, gameId);
    }

    public static void isPlayerAllowedToExecuteAction(GameStatus gameStatus, String sessionId) {
        Player currentPlayer = gameStatus.getPlayerBySessionId(sessionId);
        if (!gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(currentPlayer.getName())) {
            throw new SecurityException("Player " + currentPlayer.getName() + " is not allowed to execute an action. turn=[" + gameStatus.getTurn() + "]");
        }
    }
}
