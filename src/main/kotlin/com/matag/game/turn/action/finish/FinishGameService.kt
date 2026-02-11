package com.matag.game.turn.action.finish

import com.matag.game.adminclient.AdminClient
import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component

@Component
class FinishGameService(
    private val adminClient: AdminClient
) {
    fun setWinner(gameStatus: GameStatus, player: Player) {
        gameStatus.turn.winner = player.name
        adminClient.finishGame(gameStatus.gameId, player)
    }
}
