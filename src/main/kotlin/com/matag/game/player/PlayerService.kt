package com.matag.game.player

import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component

@Component
class PlayerService {
    fun getPlayerBySessionId(gameStatus: GameStatus, sessionId: String?): Player {
        if (gameStatus.player1?.token?.sessionId == sessionId) {
            return gameStatus.player1!!
        } else if (gameStatus.player2?.token?.sessionId == sessionId) {
            return gameStatus.player2!!
        }
        throw RuntimeException("Not allowed to access gameId: " + gameStatus.gameId)
    }

    fun getPlayerByToken(gameStatus: GameStatus, token: String?): Player {
        if (gameStatus.player1?.token?.adminToken == token) {
            return gameStatus.player1!!
        } else if (gameStatus.player2?.token?.adminToken == token) {
            return gameStatus.player2!!
        } else {
            throw RuntimeException("Not allowed to access gameId: " + gameStatus.gameId)
        }
    }
}
