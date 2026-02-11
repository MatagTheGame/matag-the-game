package com.matag.game.turn.action.enter

import com.matag.cards.ability.AbilityService
import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.cast.ManaCountService
import com.matag.game.turn.action.player.DrawXCardsService
import com.matag.game.turn.action.tap.TapPermanentService
import org.springframework.stereotype.Component

@Component
class EntersTheBattlefieldWithService(
    private val manaCountService: ManaCountService,
    private val drawXCardsService: DrawXCardsService,
    private val abilityService: AbilityService,
    private val tapPermanentService: TapPermanentService,
) {
    
    fun entersTheBattlefieldWith(gameStatus: GameStatus, cardInstance: CardInstance) {
        val parameters = addEntersTheBattlefieldWithParameters(cardInstance).toMutableList()
        parameters += addAdamantEntersTheBattlefieldWithParameters(gameStatus, cardInstance)
        executeParameters(gameStatus, cardInstance, parameters)
    }

    private fun addEntersTheBattlefieldWithParameters(cardInstance: CardInstance): List<String> {
        val entersTheBattlefieldWith = cardInstance.getAbilitiesByType(AbilityType.ENTERS_THE_BATTLEFIELD_WITH)
        return entersTheBattlefieldWith.flatMap { it.parameters }
    }

    private fun addAdamantEntersTheBattlefieldWithParameters(
        gameStatus: GameStatus,
        cardInstance: CardInstance
    ): List<String> {
        val parameters = ArrayList<String>()
        val adamantAbilities = cardInstance.getAbilitiesByType(AbilityType.ADAMANT)

        for (adamant in adamantAbilities) {
            val manaPaid = gameStatus.turn.lastManaPaid
            val manaPaidByColor = manaCountService.countManaPaid(manaPaid)
            val adamantColor: String = adamant.parameters.first()
            val adamantFulfilled = isAdamantFulfilled(manaPaidByColor, adamantColor)

            if (adamantFulfilled) {
                parameters.addAll(adamant.ability!!.parameters)
            }
        }

        return parameters
    }

    private fun isAdamantFulfilled(manaPaidByColor: Map<String, Int>, adamantColor: String): Boolean {
        if (adamantColor == "SAME" && manaPaidByColor.values.stream()
                .anyMatch { key: Int? -> key!! >= ADAMANT_THRESHOLD }
        ) {
            return true
        } else if (manaPaidByColor.containsKey(adamantColor) && manaPaidByColor.get(adamantColor)!! >= ADAMANT_THRESHOLD) {
            return true
        } else {
            return false
        }
    }

    private fun executeParameters(gameStatus: GameStatus, cardInstance: CardInstance, parameters: List<String>) {
        for (parameter in parameters) {
            if (abilityService!!.tappedFromParameter(parameter)) {
                tapPermanentService!!.tap(gameStatus, cardInstance.id)
            }

            val plus1Counters = abilityService.plus1CountersFromParameter(parameter)
            cardInstance.modifiers.counters.addPlus1Counters(plus1Counters)

            val minus1Counters = abilityService.minus1CountersFromParameter(parameter)
            cardInstance.modifiers.counters.addMinus1Counters(minus1Counters)

            val cardsToDraw = abilityService.drawFromParameter(parameter)
            drawXCardsService!!.drawXCards(gameStatus.getPlayerByName(cardInstance.controller), cardsToDraw, gameStatus)
        }
    }

    companion object {
        private const val ADAMANT_THRESHOLD = 3
    }
}
