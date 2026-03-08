package com.matag.game.status

import com.matag.game.security.SecurityHelper
import org.springframework.stereotype.Component

@Component
class GameStatusRepository(private val securityHelper: SecurityHelper) {
    private val games: MutableMap<String, GameStatus> = HashMap()

    fun contains(gameId: String): Boolean {
        return games.containsKey(gameId)
    }

    fun save(gameId: String, gameStatus: GameStatus) {
        games[gameId] = gameStatus
    }

    fun getUnsecure(gameId: String): GameStatus? {
        return games[gameId]
    }

    fun get(gameId: String, sessionId: String): GameStatus {
        val gameStatus = games[gameId]!!
        securityHelper.isPlayerAllowedToExecuteAction(gameStatus, sessionId)
        return gameStatus
    }

    fun clear() {
        games.clear()
    }
}
