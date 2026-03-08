package com.matag.game.cardinstance.modifiers

import com.matag.cards.ability.Ability
import com.matag.cards.ability.type.AbilityType
import kotlin.math.min

data class Counters(
    var plus1Counters: Int = 0,
    var minus1Counters: Int = 0,
    var keywordCounters: List<AbilityType> = listOf()
) {

    fun addPlus1Counters(n: Int) {
        plus1Counters += n
        balanceCounters()
    }

    fun addMinus1Counters(n: Int) {
        minus1Counters += n
        balanceCounters()
    }

    fun addKeywordCounter(abilityType: AbilityType) {
        keywordCounters = keywordCounters + abilityType
    }

    val keywordCountersAbilities: List<Ability>
        get() = keywordCounters
            .map { Ability(it) }

    private fun balanceCounters() {
        val min = min(plus1Counters, minus1Counters)
        plus1Counters -= min
        minus1Counters -= min
    }
}
