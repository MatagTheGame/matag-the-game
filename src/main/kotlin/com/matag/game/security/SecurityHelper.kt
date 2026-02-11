package com.matag.game.security

import com.matag.game.player.PlayerService
import com.matag.game.status.GameStatus
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Component
import java.util.*

@Component
class SecurityHelper(
    private val playerService: PlayerService
) {

    fun extractSecurityToken(headerAccessor: SimpMessageHeaderAccessor): SecurityToken {
        val sessionId = headerAccessor.sessionId
        val token = Objects.requireNonNull(headerAccessor.getNativeHeader("token"))!!.single()
        val gameId = Objects.requireNonNull(headerAccessor.getNativeHeader("gameId"))!!.single()
        return SecurityToken(sessionId!!, token, gameId)
    }

    fun isPlayerAllowedToExecuteAction(gameStatus: GameStatus, sessionId: String?) {
        val currentPlayer = playerService.getPlayerBySessionId(gameStatus, sessionId)
        if (gameStatus.turn.currentPhaseActivePlayer != currentPlayer.name) {
            throw SecurityException("Player " + currentPlayer.name + " is not allowed to execute an action. turn=[" + gameStatus.turn + "]")
        }
    }
}
