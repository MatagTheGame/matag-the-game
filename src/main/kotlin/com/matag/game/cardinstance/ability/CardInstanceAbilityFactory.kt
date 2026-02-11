package com.matag.game.cardinstance.ability

import com.matag.cards.ability.Ability
import com.matag.cards.ability.type.AbilityType
import org.springframework.stereotype.Component

@Component
class CardInstanceAbilityFactory {
    private fun get(abilityType: String): Ability {
        return Ability(Ability(AbilityType.valueOf(abilityType)))
    }

    fun abilitiesFromParameters(parameters: List<String>): List<Ability> {
        return parameters
            .filter { !it.contains("/") && !it.contains(":") }
            .map { this.get(it) }
    }

    fun abilityFromParameter(parameter: String): Ability? =
         abilitiesFromParameters(listOf(parameter)).firstOrNull()
}
