package com.matag.game.cardinstance

import com.matag.cards.ability.selector.PowerToughnessConstraint
import com.matag.cards.ability.selector.PowerToughnessConstraintUtils
import com.matag.cards.ability.selector.TurnStatusType
import com.matag.cards.ability.trigger.TriggerSubtype
import com.matag.cards.ability.type.AbilityType
import com.matag.cards.properties.Color
import com.matag.cards.properties.Subtype
import com.matag.cards.properties.Type
import com.matag.game.status.GameStatus
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.Stream

class CardInstanceSearch(
    val cards: List<CardInstance>
) {
    
    fun withId(cardId: Int): CardInstance? =
        cards.firstOrNull { it.id == cardId }

    fun withIdAsList(cardId: Int): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.id == cardId })

    fun notWithId(cardId: Int): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.id != cardId })

    fun withName(name: String?): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.card!!.name == name })

    fun ofType(type: Type): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.isOfType(type) })

    fun ofAnyOfTheTypes(types: List<Type>): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.ofAnyOfTheTypes(types) })

    fun notOfTypes(types: List<Type>): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.ofAnyOfTheTypes(types) })

    fun ofAllOfTheTypes(types: List<Type>): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.ofAllOfTheTypes(types) })

    fun ofAnyOfTheSubtypes(subtypes: List<Subtype>): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.ofAnyOfTheSubtypes(subtypes) })

    fun notOfSubtypes(subtypes: List<Subtype>): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.ofAnyOfTheSubtypes(subtypes) })

    fun ofAnyOfTheColors(colors: List<Color>): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.ofAnyOfTheColors(colors) })

    fun colorless(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.isColorless })

    fun multicolor(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.isMulticolor })

    fun tapped(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.modifiers.isTapped })

    fun untapped(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.modifiers.isUntapped })

    fun withSummoningSickness(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.isSummoningSickness })

    fun withoutSummoningSickness(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.isSummoningSickness })

    fun attacking(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.modifiers.isAttacking })

    fun blocking(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.modifiers.isBlocking })

    fun attackingOrBlocking(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.modifiers.isAttacking || it.modifiers.isBlocking })

    fun attacked(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.modifiers.modifiersUntilEndOfTurn.isAttacked })

    fun blocked(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.modifiers.modifiersUntilEndOfTurn.isBlocked })

    fun attackedOrBlocked(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.modifiers.modifiersUntilEndOfTurn.isAttacked || it.modifiers.modifiersUntilEndOfTurn.isBlocked })

    fun ofPowerToughnessConstraint(powerToughnessConstraint: PowerToughnessConstraint): CardInstanceSearch =
        CardInstanceSearch(cards.filter { PowerToughnessConstraintUtils.check(powerToughnessConstraint, it) })

    fun controlledBy(playerName: String?): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.controller == playerName })

    fun notControlledBy(playerName: String?): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.controller != playerName })

    fun withFixedAbility(abilityType: AbilityType?): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.hasFixedAbility(abilityType) })

    fun withAnyFixedAbility(abilityTypes: List<AbilityType>): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.hasAnyFixedAbility(abilityTypes) })

    fun withoutFixedAbility(abilityType: AbilityType?): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.hasFixedAbility(abilityType) })

    fun withTriggerSubtype(triggerSubtype: TriggerSubtype): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.hasFixedAbilityWithTriggerSubType(triggerSubtype) })

    fun onTurnStatusType(turnStatusType: TurnStatusType, gameStatus: GameStatus): CardInstanceSearch =
        when (turnStatusType) {
            TurnStatusType.YOUR_TURN -> controlledBy(gameStatus.currentPlayer.name)
            TurnStatusType.OPPONENT_TURN -> controlledBy(gameStatus.nonCurrentPlayer?.name)
        }

    fun attachedToId(attachedToId: Int): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.modifiers.attachedToId == attachedToId })

    fun notAcknowledged(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.notAcknowledged() })

    fun canAnyCreatureAttack(): Boolean =
        ofType(Type.CREATURE).untapped().withoutSummoningSickness().isNotEmpty()

    fun canAnyCreatureBlock(): Boolean =
        ofType(Type.CREATURE).untapped().isNotEmpty()

    fun withInstantSpeed(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.isInstantSpeed })

    fun historic(): CardInstanceSearch =
        CardInstanceSearch(cards.filter { it.isHistoric })

    fun concat(moreCards: List<CardInstance>): CardInstanceSearch =
        CardInstanceSearch(cards + moreCards)

    fun concat(moreCards: CardInstanceSearch): CardInstanceSearch =
        concat(moreCards.cards)

    fun isEmpty(): Boolean =
        cards.isEmpty()

    fun isNotEmpty(): Boolean =
        cards.isNotEmpty()
}
