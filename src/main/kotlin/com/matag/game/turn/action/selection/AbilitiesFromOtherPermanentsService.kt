package com.matag.game.turn.action.selection

import com.matag.cards.ability.Ability
import com.matag.cards.ability.AbilityService
import com.matag.cards.ability.trigger.TriggerType
import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.ability.CardInstanceAbilityFactory
import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component

@Component
class AbilitiesFromOtherPermanentsService(
    private val magicInstancePermanentSelectorService: MagicInstancePermanentSelectorService,
    private val abilityService: AbilityService,
    private val cardInstanceAbilityFactory: CardInstanceAbilityFactory,
) {

    fun getPowerFromOtherPermanents(gameStatus: GameStatus, cardInstance: CardInstance): Int =
        getParametersFromOtherPermanents(gameStatus, cardInstance)
            .sumOf { abilityService.powerToughnessFromParameter(it!!).power }

    fun getToughnessFromOtherPermanents(gameStatus: GameStatus, cardInstance: CardInstance): Int =
        getParametersFromOtherPermanents(gameStatus, cardInstance)
            .sumOf { abilityService.powerToughnessFromParameter(it!!).toughness }

    fun getAbilitiesFormOtherPermanents(gameStatus: GameStatus, cardInstance: CardInstance): List<Ability> {
        return cardInstanceAbilityFactory.abilitiesFromParameters(getParametersFromOtherPermanents(gameStatus, cardInstance))
    }

    private fun getParametersFromOtherPermanents(
        gameStatus: GameStatus,
        cardInstance: CardInstance
    ): List<String> {
        val parameters = mutableListOf<String>()
        val cards = gameStatus.allBattlefieldCardsSearch.withFixedAbility(AbilityType.SELECTED_PERMANENTS_GET).cards

        for (card in cards) {
            for (ability in card.getFixedAbilitiesByType(AbilityType.SELECTED_PERMANENTS_GET)) {
                if (ability!!.trigger!!.type == TriggerType.STATIC) {
                    if (magicInstancePermanentSelectorService.select(gameStatus, card, ability.magicInstanceSelector!!)!!.withId(cardInstance.id) != null) {
                        parameters.addAll(ability.parameters)
                    }
                }
            }
        }

        return parameters
    }
}
