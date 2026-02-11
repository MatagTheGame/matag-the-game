package com.matag.game.turn.action.permanent

import com.matag.game.cardinstance.CardInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GainControlPermanentService {
    fun gainControlUntilEndOfTurn(target: CardInstance, newControllerName: String?) {
        target.modifiers.modifiersUntilEndOfTurn.newController = newControllerName
        LOGGER.info("Changed controller until end of turn of " + target.idAndName + " to " + newControllerName)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(GainControlPermanentService::class.java)
    }
}
