package com.matag.game.turn.action._continue

import com.matag.cards.properties.Type
import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.leave.DestroyPermanentService
import com.matag.game.turn.action.leave.ReturnPermanentToHandService
import org.springframework.stereotype.Component

@Component
class ConsolidateStatusService(
    private val destroyPermanentService: DestroyPermanentService,
    private val returnPermanentToHandService: ReturnPermanentToHandService
) {
    // Destroy all creatures with toughness - damage > 0.
    // If a creature is destroyed the entire GameStatus needs to be reevaluated as that creature
    // might have "other creatures get +1/+1" and keeping them alive.
    fun consolidate(gameStatus: GameStatus) {
        var repeat: Boolean
        do {
            repeat = false
            for (card in gameStatus.allBattlefieldCards) {
                if (isToBeDestroyed(card)) {
                    val destroyed = destroyPermanentService.destroy(gameStatus, card.id)
                    if (destroyed) {
                        repeat = true
                    }
                }

                if (card.modifiers.modifiersUntilEndOfTurn.isToBeReturnedToHand) {
                    val returnedToHand = returnPermanentToHandService.returnPermanentToHand(gameStatus, card.id)
                    if (returnedToHand) {
                        repeat = true
                    }
                }
            }
        } while (repeat)
    }

    private fun isToBeDestroyed(card: CardInstance): Boolean {
        return card.isPermanent && card.modifiers.modifiersUntilEndOfTurn.isToBeDestroyed ||
                card.isOfType(Type.CREATURE) && card.toughness - card.modifiers.damage <= 0
    }
}
