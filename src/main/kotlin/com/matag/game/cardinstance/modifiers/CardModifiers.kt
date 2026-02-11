package com.matag.game.cardinstance.modifiers

import com.matag.cards.ability.Ability
import com.matag.cards.properties.PowerToughness

data class CardModifiers(
    var permanentId: Int = 0,
    var isTapped: Boolean = false,
    var doesNotUntapNextTurn: Boolean = false,
    var isSummoningSickness: Boolean = false,
    var isAttacking: Boolean = false,
    var blockingCardId: Int? = null,
    var damage: Int = 0,
    val abilities: List<Ability> = listOf(),
    var targets: List<Any> = listOf(),
    var attachedToId: Int = 0,
    val controller: String? = null,
    val counters: Counters = Counters(),
    var modifiersUntilEndOfTurn: CardModifiersUntilEndOfTurn = CardModifiersUntilEndOfTurn(),
) {


    fun cleanupUntilEndOfTurnModifiers() {
        damage = 0
        modifiersUntilEndOfTurn = CardModifiersUntilEndOfTurn()
    }

    val isUntapped: Boolean
        get() = !this.isTapped

    fun doesNotUntapNextTurn() {
        this.doesNotUntapNextTurn = true
    }

    val isBlocking: Boolean
        get() = blockingCardId != null

    fun unsetBlockingCardId() {
        this.blockingCardId = null
    }

    fun dealDamage(damage: Int) {
        this.damage += damage
    }

    fun unsetAttachedId() {
        attachedToId = 0
    }

    val extraPowerToughnessFromCounters: PowerToughness
        get() {
            val countersExtra = counters.plus1Counters - counters.minus1Counters
            return PowerToughness(countersExtra, countersExtra)
        }

    fun resetTargets() {
        targets = listOf()
    }

    fun addTargets(targetsIds: List<Any>) {
        targets = targets + targetsIds
    }
}

