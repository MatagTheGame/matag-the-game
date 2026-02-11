package com.matag.game.turn.action.player

import com.matag.game.cardinstance.CardInstance
import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.InputRequiredActions.SCRY
import org.springframework.stereotype.Component
import kotlin.math.abs

@Component
class ScryXCardsService {
    fun scryXCardsTrigger(player: Player?, cardsToScry: Int, gameStatus: GameStatus) {
        if (cardsToScry > 0) {
            gameStatus.turn.inputRequiredAction = SCRY
            gameStatus.turn.inputRequiredActionParameter = cardsToScry.toString()
        }
    }

    fun scryXCards(gameStatus: GameStatus, positions: List<Int>) {
        val cards = gameStatus.currentPlayer.library.cards.toMutableList()
        val extractedCards = mutableListOf<CardInstance>()
        for (i in positions.indices) {
            extractedCards.add(cards.removeAt(0))
        }

        for (i in positions.indices) {
            val position = positions.get(i)
            val card = extractedCards.get(i)
            for (j in MAX_CARDS_TO_SCRY downTo 1) {
                if (j == abs(position)) {
                    if (position > 0) {
                        cards.add(0, card)
                    } else {
                        cards.add(card)
                    }
                }
            }
        }
        gameStatus.currentPlayer.library.cards = cards
    }

    companion object {
        private const val MAX_CARDS_TO_SCRY = 5
    }
}
