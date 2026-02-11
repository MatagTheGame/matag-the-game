package com.matag.game.turn.action.player

import com.matag.game.message.MessageException
import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.InputRequiredActions.DISCARD_A_CARD
import com.matag.game.turn.action.leave.PutIntoGraveyardService
import org.springframework.stereotype.Component

@Component
class DiscardXCardsService(
    private val putIntoGraveyardService: PutIntoGraveyardService
) {

    fun discardXCardsTrigger(gameStatus: GameStatus, cardsToDiscard: Int) {
        gameStatus.turn.inputRequiredAction = DISCARD_A_CARD
        gameStatus.turn.inputRequiredActionParameter = cardsToDiscard.toString()
    }

    fun discardXCards(discardedCardsIds: List<Int>, gameStatus: GameStatus) {
        try {
            val cardsToDiscard = gameStatus.turn.inputRequiredActionParameter!!.toInt()
            val cards = gameStatus.currentPlayer.hand.extractCardsByIds(discardedCardsIds)

            if (cards.size != cardsToDiscard) {
                throw MessageException("Needs discarding " + cardsToDiscard + " cards, not " + cards.size)
            }

            putIntoGraveyardService.putIntoGraveyard(gameStatus, cards)
        } catch (e: Exception) {
            throw MessageException(e.message)
        }
    }
}
