package com.matag.game.turn.action.damage

import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DealDamageToCreatureService {
    fun dealDamageToCreature(
        gameStatus: GameStatus?,
        cardInstance: CardInstance,
        damage: Int,
        deathtouch: Boolean,
        damageDealer: CardInstance
    ) {
        if (damage > 0) {
            LOGGER.info("{} is getting {} damage from {}.", cardInstance.idAndName, damage, damageDealer.idAndName)
            cardInstance.modifiers.dealDamage(damage)
            if (cardInstance.toughness - cardInstance.modifiers.damage <= 0 || deathtouch) {
                LOGGER.info("{} marked to be destroyed.", cardInstance.idAndName)
                cardInstance.modifiers.modifiersUntilEndOfTurn.isToBeDestroyed = true
            }
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DealDamageToCreatureService::class.java)
    }
}
