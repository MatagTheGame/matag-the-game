package com.matag.game.turn.action.leave

import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component

@Component
class PutIntoHandService {
    fun returnToHand(gameStatus: GameStatus, cardInstance: CardInstance) {
        val owner = gameStatus.getPlayerByName(cardInstance.owner)
        cardInstance.resetAllModifiers()
        owner.hand.addCard(cardInstance)
    }
}
