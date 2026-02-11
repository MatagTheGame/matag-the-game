package com.matag.game.turn.action.attach

import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AttachService {
    fun attach(gameStatus: GameStatus, cardInstance: CardInstance, attachedToId: Int) {
        val attachTo = gameStatus.findCardByIdFromAnyBattlefield(attachedToId)
        cardInstance.modifiers.attachedToId = attachedToId
        LOGGER.info("attached {} to {}", cardInstance.idAndName, attachTo!!.idAndName)
    }

    fun detach(cardToDestroy: CardInstance, attachedCard: CardInstance) {
        attachedCard.modifiers.unsetAttachedId()
        LOGGER.info("detached {} from {}", attachedCard.idAndName, cardToDestroy.idAndName)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(AttachService::class.java)
    }
}
