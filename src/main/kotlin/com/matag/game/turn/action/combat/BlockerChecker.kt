package com.matag.game.turn.action.combat

import com.matag.cards.ability.type.AbilityType
import com.matag.cards.properties.Type
import com.matag.game.cardinstance.CardInstance
import com.matag.game.message.MessageException
import org.springframework.stereotype.Component

@Component
class BlockerChecker {
    fun checkIfCanBlock(attacker: CardInstance, blockers: List<CardInstance>) {
        for (blocker in blockers) {
            checkIfCanBlock(attacker, blocker)
        }

        if (attacker.hasAbilityType(AbilityType.MENACE) && blockers.size == 1) {
            throw MessageException(blockers.get(0).idAndName + " cannot block " + attacker.idAndName + " alone as it has menace.")
        }
    }

    private fun checkIfCanBlock(attacker: CardInstance, blocker: CardInstance) {
        if (!blocker.isOfType(Type.CREATURE)) {
            throw MessageException(blocker.idAndName + " is not of type Creature.")
        }

        if (blocker.modifiers.isTapped) {
            throw MessageException(blocker.idAndName + " is tapped and cannot block.")
        }

        if (attacker.hasAbilityType(AbilityType.FLYING)) {
            if (!(blocker.hasAbilityType(AbilityType.FLYING) || blocker.hasAbilityType(AbilityType.REACH))) {
                throw MessageException(blocker.idAndName + " cannot block " + attacker.idAndName + " as it has flying.")
            }
        }
    }
}
