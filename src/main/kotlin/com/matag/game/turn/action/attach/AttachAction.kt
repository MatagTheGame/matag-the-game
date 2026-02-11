package com.matag.game.turn.action.attach

import com.matag.cards.ability.Ability
import com.matag.cards.ability.AbilityAction
import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component

@Component
class AttachAction(
    private val attachService: AttachService
) : AbilityAction {
    override fun perform(cardInstance: CardInstance, gameStatus: GameStatus, ability: Ability?) {
        val target = cardInstance.modifiers.targets[0].toString()
        val attachedToId: Int = target.toInt()
        attachService.attach(gameStatus, cardInstance, attachedToId)
    }
}
