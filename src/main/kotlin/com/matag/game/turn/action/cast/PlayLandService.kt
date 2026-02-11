package com.matag.game.turn.action.cast

import com.matag.cards.properties.Type
import com.matag.game.message.MessageException
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService
import com.matag.game.turn.phases.PhaseUtils
import org.springframework.stereotype.Component

@Component
class PlayLandService(
    private val enterCardIntoBattlefieldService: EnterCardIntoBattlefieldService
) {

    fun playLand(gameStatus: GameStatus, cardId: Int) {
        val turn = gameStatus.turn
        val activePlayer = gameStatus.activePlayer

        if (!PhaseUtils.isMainPhase(turn.currentPhase!!)) {
            throw MessageException("You can only play lands during main phases.")
        } else if (turn.cardsPlayedWithinTurn
                .map { it.card?.types }
                .any { it?.contains(Type.LAND) == true }
        ) {
            throw MessageException("You already played a land this turn.")
        } else {
            var cardInstance = activePlayer.hand.findCardById(cardId)
            if (cardInstance.isOfType(Type.LAND)) {
                cardInstance = activePlayer.hand.extractCardById(cardId)
                cardInstance.controller = activePlayer.name
                turn.addCardToCardsPlayedWithinTurn(cardInstance)
                enterCardIntoBattlefieldService.enter(gameStatus, cardInstance)
            } else {
                throw MessageException("Playing " + cardInstance.idAndName + " as land.")
            }
        }
    }
}
