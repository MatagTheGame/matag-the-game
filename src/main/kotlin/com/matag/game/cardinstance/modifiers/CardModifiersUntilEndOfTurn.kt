package com.matag.game.cardinstance.modifiers

import com.matag.cards.ability.Ability
import com.matag.cards.properties.PowerToughness

class CardModifiersUntilEndOfTurn {
    val isAttacked = false
    var isBlocked = false
    var extraAbilities: List<Ability> = listOf()
    var extraPowerToughness = PowerToughness(0, 0)
    var newController: String? = null
    var isToBeDestroyed = false
    var isToBeReturnedToHand = false

    fun addExtraPowerToughnessUntilEndOfTurn(extraPowerToughness: PowerToughness) {
        val newPower = this.extraPowerToughness.power + extraPowerToughness.power
        val newToughness = this.extraPowerToughness.toughness + extraPowerToughness.toughness
        this.extraPowerToughness = PowerToughness(newPower, newToughness)
    }
}
