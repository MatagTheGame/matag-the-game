package com.matag.game.cardinstance.ability

import com.fasterxml.jackson.annotation.JsonProperty
import com.matag.cards.Card
import com.matag.cards.ability.Ability
import com.matag.cards.ability.AbilityService
import com.matag.cards.ability.selector.SelectorType
import com.matag.cards.ability.transpose
import com.matag.cards.ability.type.AbilityType

data class CardInstanceAbility(
    val ability: Ability
) {
    constructor(abilityType: AbilityType) : this(Ability(abilityType))

    constructor(cardInstanceAbility: CardInstanceAbility) : this(
        Ability(
            cardInstanceAbility.ability.abilityType,
            cardInstanceAbility.ability.targets,
            cardInstanceAbility.ability.magicInstanceSelector,
            cardInstanceAbility.ability.parameters,
            cardInstanceAbility.ability.trigger,
            cardInstanceAbility.ability.ability,
            cardInstanceAbility.ability.sorcerySpeed,
            cardInstanceAbility.ability.optional
        )
    )

    @get:JsonProperty
    val abilityTypeText: String
        get() {
            val parametersString: String = AbilityService().parametersAsString(ability.parameters!!)

            if (ability.abilityType == AbilityType.SELECTED_PERMANENTS_GET) {
                val magicInstanceSelector = ability.magicInstanceSelector!!
                if (magicInstanceSelector.selectorType == SelectorType.PLAYER) {
                    return String.format(ability.abilityType.text, magicInstanceSelector.text(), parametersString) + "."
                } else {
                    return String.format(ability.abilityType.text, magicInstanceSelector.text(), parametersString) + " until end of turn."
                }
            } else {
                return String.format(ability.abilityType.text, parametersString)
            }
        }

    fun getParameter(i: Int): String? {
        if (ability.parameters!!.size > i) {
            return ability.parameters!![i]
        }
        return null
    }

    fun requiresTarget(): Boolean {
        return !ability.targets!!.isEmpty()
    }

    companion object {
        @JvmStatic
        fun getCardInstanceAbilities(card: Card?): List<CardInstanceAbility?> =
            card?.abilities.orEmpty()
                .map { getCardInstanceAbility(it) }

        @JvmStatic
        fun getCardInstanceAbility(ability: Ability?): CardInstanceAbility? =
            ability?.let {
                CardInstanceAbility(it.transpose())
        }
    }
}
