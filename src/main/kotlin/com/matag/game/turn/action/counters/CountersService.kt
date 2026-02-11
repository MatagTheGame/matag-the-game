package com.matag.game.turn.action.counters

import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CountersService {
    fun addPlus1Counters(target: CardInstance, counters: Int) {
        if (counters > 0) {
            target.modifiers.counters.addPlus1Counters(1)
            LOGGER.info(target.idAndName + " got " + counters + " +1/+1 counters")
        }
    }

    fun addMinus1Counters(target: CardInstance, counters: Int) {
        if (counters > 0) {
            target.modifiers.counters.addMinus1Counters(1)
            LOGGER.info(target.idAndName + " got " + counters + " -1/-1 counters")
        }
    }

    fun addKeywordCounter(target: CardInstance, keywordCounter: AbilityType) {
        target.modifiers.counters.addKeywordCounter(keywordCounter)
        LOGGER.info(target.idAndName + " got " + keywordCounter + " counters")
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(CountersService::class.java)
    }
}
