package com.matag.game.turn.action._continue

import com.matag.cards.ability.trigger.TriggerType
import com.matag.game.cardinstance.cost.CostService
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.cast.InstantSpeedService
import com.matag.game.turn.phases.PhaseUtils
import org.springframework.stereotype.Component

@Component
class AutocontinueChecker(
    private val costService: CostService,
    private val instantSpeedService: InstantSpeedService
) {
    
    fun canPerformAnyAction(gameStatus: GameStatus): Boolean {
        if (PhaseUtils.isMainPhase(gameStatus.turn.currentPhase!!) && gameStatus.isCurrentPlayerActive) {
            return true
        }

        if (gameStatus.turn.inputRequiredAction != null) {
            return true
        }

        val player = gameStatus.activePlayer

        val instants = player.hand.search().withInstantSpeed().cards
        for (instant in instants) {
            if (costService.canAfford(instant, null, gameStatus)) {
                return true
            }
        }

        val cards = player.battlefield.cards
        for (card in cards) {
            val cardAbilities = card.abilities
            for (cardAbility in cardAbilities) {
                if (cardAbility.trigger != null && cardAbility.trigger?.type == TriggerType.ACTIVATED_ABILITY
                ) {
                    val ability = cardAbility.abilityType.toString()
                    if (instantSpeedService.isAtInstantSpeed(card, ability)) {
                        if (costService.canAfford(card, ability, gameStatus)) {
                            return true
                        }
                    }
                }
            }
        }

        if (gameStatus.stack.search().notAcknowledged().isNotEmpty()) {
            return true
        }


        // TODO Autocontinue main phases in future
        return false
    }
}
