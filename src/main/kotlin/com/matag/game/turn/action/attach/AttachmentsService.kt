package com.matag.game.turn.action.attach

import com.matag.cards.ability.Ability
import com.matag.cards.ability.AbilityService
import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.ability.CardInstanceAbilityFactory
import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component

@Component
class AttachmentsService(
    private val abilityService: AbilityService,
    private val cardInstanceAbilityFactory: CardInstanceAbilityFactory
) {

    fun getAttachedCards(gameStatus: GameStatus, cardInstance: CardInstance): List<CardInstance> {
        return gameStatus.allBattlefieldCardsSearch.attachedToId(cardInstance.id).cards
    }

    fun getAttachmentsPower(gameStatus: GameStatus, cardInstance: CardInstance): Int {
        return getAttachedCardsAbilities(gameStatus, cardInstance)
            .map { abilityService.powerToughnessFromParameters(it!!.parameters) }
            .sumOf { it.power }
    }

    fun getAttachmentsToughness(gameStatus: GameStatus, cardInstance: CardInstance): Int {
        return getAttachedCardsAbilities(gameStatus, cardInstance)
            .map { abilityService.powerToughnessFromParameters(it.parameters) }
            .sumOf { it.toughness }
    }

    fun getAttachmentsAbilities(gameStatus: GameStatus, cardInstance: CardInstance): List<Ability> {
        return getAttachedCardsAbilities(gameStatus, cardInstance)
            .flatMap { cardInstanceAbilityFactory.abilitiesFromParameters(it.parameters) }
    }

    private fun getAttachedCardsAbilities(gameStatus: GameStatus, cardInstance: CardInstance): List<Ability> {
        return getAttachedCards(gameStatus, cardInstance)
            .flatMap { it.card!!.abilities }
            .map { Ability(it) }
            .filter { ATTACHED_ABILITY_TYPES.contains(it.abilityType) }
    }

    companion object {
        private val ATTACHED_ABILITY_TYPES: List<AbilityType> = listOf(AbilityType.ENCHANT, AbilityType.EQUIP)
    }
}
