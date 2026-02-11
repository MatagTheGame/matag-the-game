package com.matag.cards.ability.selector

import com.matag.cards.ability.selector.PowerToughnessConstraint.PowerOrToughness
import com.matag.game.cardinstance.CardInstance

object PowerToughnessConstraintUtils {
    @JvmStatic
    fun check(powerToughnessConstraint: PowerToughnessConstraint, cardInstance: CardInstance): Boolean {
        val value = getValue(powerToughnessConstraint.powerOrToughness, cardInstance)

        return when (powerToughnessConstraint.powerToughnessConstraintType) {
            PowerToughnessConstraintType.EQUAL -> value == powerToughnessConstraint.value
            PowerToughnessConstraintType.LESS -> value < powerToughnessConstraint.value
            PowerToughnessConstraintType.LESS_OR_EQUAL -> value <= powerToughnessConstraint.value
            PowerToughnessConstraintType.GREATER -> value > powerToughnessConstraint.value
            PowerToughnessConstraintType.GREATER_OR_EQUAL -> value >= powerToughnessConstraint.value
        }
    }

    private fun getValue(powerOrToughness: PowerOrToughness, cardInstance: CardInstance): Int {
        return when (powerOrToughness) {
            PowerOrToughness.POWER -> cardInstance.power
            PowerOrToughness.TOUGHNESS -> cardInstance.toughness
        }
    }
}
