package com.matag.game.turn.action.player

import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.finish.FinishGameService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DrawXCardsService(
    private val finishGameService: FinishGameService
) {
    fun drawXCards(player: Player, cardsToDraw: Int, gameStatus: GameStatus) {
        if (cardsToDraw > 0) {
            val effectiveCardsToDraw = cardsToDraw.coerceAtMost(player.library.size())
            for (i in 0..<effectiveCardsToDraw) {
                val drawnCardInstance = player.library.draw()
                player.hand.addCard(drawnCardInstance)
            }

            LOGGER.info("{} drew {} cards.", player.name, effectiveCardsToDraw)

            if (cardsToDraw > effectiveCardsToDraw) {
                val winner = gameStatus.getOtherPlayer(player)
                LOGGER.info(
                    "Player {} lose (needed to draw {} cards), Player {} win",
                    player.name,
                    cardsToDraw,
                    winner.name
                )
                finishGameService.setWinner(gameStatus, winner)
            }
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DrawXCardsService::class.java)
    }
}
