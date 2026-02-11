package com.matag.game.turn.action.tap

import com.matag.game.status.GameStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TapPermanentService {
    fun tap(gameStatus: GameStatus, targetId: Int) {
        val cardToTap = gameStatus.findCardByIdFromAnyBattlefield(targetId)
        if (cardToTap != null) {
            cardToTap.modifiers.isTapped = true
            LOGGER.info("{} tapped.", cardToTap.idAndName)
        } else {
            LOGGER.info("target {} is not anymore valid.", targetId)
        }
    }

    fun doesNotUntapNextTurn(gameStatus: GameStatus, targetId: Int) {
        val cardToTap = gameStatus.findCardByIdFromAnyBattlefield(targetId)
        if (cardToTap != null) {
            cardToTap.modifiers.doesNotUntapNextTurn()
            LOGGER.info("{} does not untap next turn.", cardToTap.idAndName)
        } else {
            LOGGER.info("target {} is not anymore valid.", targetId)
        }
    }

    fun untap(gameStatus: GameStatus, targetId: Int) {
        val cardToTap = gameStatus.findCardByIdFromAnyBattlefield(targetId)
        if (cardToTap != null) {
            cardToTap.modifiers.isTapped = false
            LOGGER.info("{} tapped.", cardToTap.idAndName)
        } else {
            LOGGER.info("target {} is not anymore valid.", targetId)
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(TapPermanentService::class.java)
    }
}
