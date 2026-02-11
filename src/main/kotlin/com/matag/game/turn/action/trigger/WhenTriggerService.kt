package com.matag.game.turn.action.trigger

import com.matag.cards.ability.trigger.TriggerSubtype
import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.selection.MagicInstancePermanentSelectorService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class WhenTriggerService(
    private val magicInstancePermanentSelectorService: MagicInstancePermanentSelectorService
) {

    fun whenTriggered(gameStatus: GameStatus, cardInstance: CardInstance, triggerSubtype: TriggerSubtype) {
        val cardsWithTriggerAbility = gameStatus.allBattlefieldCardsSearch.withTriggerSubtype(triggerSubtype).cards

        for (cardWithTriggerAbility in cardsWithTriggerAbility) {
            for (ability in cardWithTriggerAbility.getAbilitiesByTriggerSubType(triggerSubtype)) {
                val selectionForCardWithTriggeredAbility = magicInstancePermanentSelectorService!!.select(
                    gameStatus,
                    cardWithTriggerAbility,
                    ability!!.trigger!!.magicInstanceSelector!!
                )
                if (selectionForCardWithTriggeredAbility.withId(cardInstance.id) != null) {
                    cardWithTriggerAbility.triggeredAbilities += ability
                }
            }

            if (!cardWithTriggerAbility.triggeredAbilities.isEmpty()) {
                LOGGER.info(
                    "{} triggered {} because of {}.",
                    cardInstance.idAndName,
                    triggerSubtype,
                    cardInstance.idAndName
                )
                gameStatus.stack.push(cardWithTriggerAbility)
            }
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(WhenTriggerService::class.java)
    }
}
