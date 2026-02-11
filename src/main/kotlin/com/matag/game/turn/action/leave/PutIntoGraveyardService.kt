package com.matag.game.turn.action.leave

import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component

@Component
class PutIntoGraveyardService {
    fun putIntoGraveyard(gameStatus: GameStatus, cards: List<CardInstance>) {
        for (card in cards) {
            val owner = gameStatus.getPlayerByName(card.owner)
            card.resetAllModifiers()
            owner.graveyard.addCard(card)
        }
    }

    fun putIntoGraveyard(gameStatus: GameStatus, cardInstance: CardInstance) {
        putIntoGraveyard(gameStatus, listOf(cardInstance))
    }
}
