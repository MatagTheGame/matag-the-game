package com.matag.game.turn.action.player

import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.finish.FinishGameService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LifeService(
    private val finishGameService: FinishGameService
) {

    fun add(player: Player, amount: Int, gameStatus: GameStatus) {
        if (amount != 0) {
            player.addLife(amount)
            LOGGER.info("Player {} add {} life bringing it to {}", player.name, amount, player.life)

            if (player.life <= 0) {
                val winner = gameStatus.getOtherPlayer(player)
                LOGGER.info("Player {} lose, Player {} win", player.name, winner.name)
                finishGameService.setWinner(gameStatus, winner)
            }
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LifeService::class.java)
    }
}
