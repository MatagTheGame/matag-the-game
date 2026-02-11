package com.matag.game.turn.action.cast

import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import org.springframework.stereotype.Component

@Component
class InstantSpeedService {
    fun isAtInstantSpeed(cardToCast: CardInstance, playedAbility: String?): Boolean {
        if (playedAbility != null) {
            val abilities = cardToCast.getAbilitiesByType(AbilityType.valueOf(playedAbility))
            for (ability in abilities) {
                if (!ability!!.sorcerySpeed) {
                    return true
                }
            }
        }

        if (cardToCast.isInstantSpeed) {
            return true
        }

        return false
    }
}
