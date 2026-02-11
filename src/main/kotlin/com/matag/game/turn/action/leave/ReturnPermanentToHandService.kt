package com.matag.game.turn.action.leave

import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component

@Component
class ReturnPermanentToHandService(
    private val leaveBattlefieldService: LeaveBattlefieldService,
    private val putIntoHandService: PutIntoHandService
) {
    
    fun markAsToBeReturnedToHand(gameStatus: GameStatus, targetId: Int) {
        val cardInstance = gameStatus.findCardByIdFromAnyBattlefield(targetId)

        if (cardInstance != null) {
            cardInstance.modifiers.modifiersUntilEndOfTurn.isToBeReturnedToHand = true
        }
    }

    fun returnPermanentToHand(gameStatus: GameStatus, targetId: Int): Boolean {
        val cardInstance = gameStatus.findCardByIdFromAnyBattlefield(targetId)

        if (cardInstance != null) {
            leaveBattlefieldService.leaveTheBattlefield(gameStatus, targetId)
            putIntoHandService.returnToHand(gameStatus, cardInstance)
            return true
        }

        return false
    }
}
