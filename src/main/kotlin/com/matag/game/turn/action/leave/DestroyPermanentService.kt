package com.matag.game.turn.action.leave

import com.matag.cards.ability.trigger.TriggerSubtype
import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.trigger.WhenTriggerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DestroyPermanentService(
    private val leaveBattlefieldService: LeaveBattlefieldService,
    private val putIntoGraveyardService: PutIntoGraveyardService,
    private val whenTriggerService: WhenTriggerService,
) {
    
    fun markToBeDestroyed(gameStatus: GameStatus, permanentId: Int) {
        val cardInstance = gameStatus.findCardByIdFromAnyBattlefield(permanentId)

        if (cardInstance != null) {
            cardInstance.modifiers.modifiersUntilEndOfTurn.isToBeDestroyed = true
        }
    }

    fun destroy(gameStatus: GameStatus, permanentId: Int): Boolean {
        val cardInstance = gameStatus.findCardByIdFromAnyBattlefield(permanentId)

        if (cardInstance == null) {
            return false
        }

        if (cardInstance.toughness <= 0) {
            return destroyAction(gameStatus, permanentId, cardInstance)
        }

        if (cardInstance.hasAbilityType(AbilityType.INDESTRUCTIBLE)) {
            LOGGER.info("{} has indestructible and cannot be destroyed.", cardInstance.idAndName)
            return false
        }

        return destroyAction(gameStatus, permanentId, cardInstance)
    }

    private fun destroyAction(gameStatus: GameStatus, permanentId: Int, cardInstance: CardInstance): Boolean {
        whenTriggerService.whenTriggered(gameStatus, cardInstance, TriggerSubtype.WHEN_DIE)
        leaveBattlefieldService.leaveTheBattlefield(gameStatus, permanentId)
        putIntoGraveyardService.putIntoGraveyard(gameStatus, cardInstance)
        LOGGER.info("{} has been destroyed.", cardInstance.idAndName)
        return true
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DestroyPermanentService::class.java)
    }
}
