package com.matag.game.turn.action.cast

import com.matag.cards.ability.type.AbilityType
import com.matag.cards.properties.Color
import com.matag.cards.properties.Cost
import com.matag.game.message.MessageException
import com.matag.game.player.Player
import org.springframework.stereotype.Component

@Component
class ManaCountService {
    fun verifyManaPaid(mana: Map<Int, List<String>>, currentPlayer: Player): List<Cost> {
        val paidCost = mutableListOf<Cost>()
        for (cardInstanceId in mana.keys) {
            val requestedManas: List<String> = mana[cardInstanceId]!!

            for (requestedMana in requestedManas) {
                val cardInstanceToTap = currentPlayer.battlefield.findCardById(cardInstanceId)
                if (!cardInstanceToTap.hasAbilityType(AbilityType.TAP_ADD_MANA)) {
                    throw MessageException(cardInstanceToTap.idAndName + " cannot be tapped for mana.")
                } else if (cardInstanceToTap.modifiers.isTapped) {
                    throw MessageException(cardInstanceToTap.idAndName + " is already tapped.")
                } else if (requestedMana != "COLORLESS" && !cardInstanceToTap.canProduceMana(Color.valueOf(requestedMana))) {
                    throw MessageException(cardInstanceToTap.idAndName + " cannot produce " + requestedMana)
                } else {
                    paidCost.add(Cost.valueOf(requestedMana))
                }
            }
        }
        return paidCost
    }

    fun countManaPaid(mana: Map<Int, List<String>>): Map<String, Int> {
        val map = HashMap<String, Int>()

        for (values in mana.values) {
            for (value in values) {
                if (!map.containsKey(value)) {
                    map[value] = 0
                }
                map[value] = map[value]!! + 1
            }
        }

        return map
    }
}
