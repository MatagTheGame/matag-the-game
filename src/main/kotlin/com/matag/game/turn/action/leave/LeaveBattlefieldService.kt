package com.matag.game.turn.action.leave

import com.matag.cards.properties.Type
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.attach.AttachService
import com.matag.game.turn.action.attach.AttachmentsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class LeaveBattlefieldService @Lazy constructor(
    private val attachService: AttachService,
    private val attachmentsService: AttachmentsService,
    private val destroyPermanentService: DestroyPermanentService
) {
    fun leaveTheBattlefield(gameStatus: GameStatus, permanentId: Int) {
        val cardInstance = gameStatus.extractCardByIdFromAnyBattlefield(permanentId)
        cardInstance!!.resetAllModifiers()

        for (attachedCard in attachmentsService.getAttachedCards(gameStatus, cardInstance)) {
            if (attachedCard!!.isOfType(Type.ENCHANTMENT)) {
                destroyPermanentService.markToBeDestroyed(gameStatus, attachedCard.id)
            } else if (attachedCard.isOfType(Type.ARTIFACT)) {
                attachService.detach(cardInstance, attachedCard)
            }
        }

        LOGGER.info("{} left the battlefield.", cardInstance.idAndName)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LeaveBattlefieldService::class.java)
    }
}
