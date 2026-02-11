package com.matag.game.turn.action.enter

import com.matag.cards.ability.trigger.TriggerSubtype
import com.matag.cards.properties.Type
import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.trigger.WhenTriggerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EnterCardIntoBattlefieldService(
    private val entersTheBattlefieldWithService: EntersTheBattlefieldWithService,
    private val whenTriggerService: WhenTriggerService
) {


    fun enter(gameStatus: GameStatus, cardInstance: CardInstance) {
        val controller = cardInstance.controller
        gameStatus.getPlayerByName(controller).battlefield.addCard(cardInstance)

        cardInstance.modifiers.permanentId = gameStatus.nextCardId()

        if (cardInstance.isOfType(Type.CREATURE)) {
            cardInstance.modifiers.isSummoningSickness = true
        }

        LOGGER.info("{} entered the battlefield.", cardInstance.idAndName)

        entersTheBattlefieldWithService.entersTheBattlefieldWith(gameStatus, cardInstance)
        whenTriggerService.whenTriggered(gameStatus, cardInstance, TriggerSubtype.WHEN_ENTER_THE_BATTLEFIELD)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(EnterCardIntoBattlefieldService::class.java)
    }
}
