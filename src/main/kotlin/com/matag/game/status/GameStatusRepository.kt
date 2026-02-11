package com.matag.game.status

import com.matag.game.security.SecurityHelper
import org.springframework.stereotype.Component

@Component
class GameStatusRepository(private val securityHelper: SecurityHelper) {
    private val games: MutableMap<String?, GameStatus> = HashMap<String?, GameStatus>()

    fun contains(gameId: String?): Boolean {
        return games.containsKey(gameId)
    }

    fun save(gameId: String?, gameStatus: GameStatus?) {
        games.put(gameId, gameStatus!!)
    }

    fun getUnsecure(gameId: String?): GameStatus? {
        return games.get(gameId)
    }

    fun get(gameId: String?, sessionId: String?): GameStatus {
        val gameStatus: GameStatus = games.get(gameId)!!
        securityHelper.isPlayerAllowedToExecuteAction(gameStatus, sessionId)
        return gameStatus
    }

    fun clear() {
        games.clear()
    }
}
