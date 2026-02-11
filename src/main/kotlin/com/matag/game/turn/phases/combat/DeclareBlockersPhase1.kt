package com.matag.game.turn.phases.combat

import com.matag.cards.ability.type.AbilityType
import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.action._continue.InputRequiredActions.DECLARE_BLOCKERS
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import org.springframework.stereotype.Component
import java.util.List

@Component
class DeclareBlockersPhase(
    autocontinueChecker: AutocontinueChecker,
    private val firstStrikePhase: FirstStrikePhase,
    private val combatDamagePhase: CombatDamagePhase
) : AbstractPhase(autocontinueChecker) {
    override val name = DB

    override fun action(gameStatus: GameStatus) {
        if (gameStatus.nonCurrentPlayer.battlefield.search().canAnyCreatureBlock()) {
            gameStatus.turn.inputRequiredAction = DECLARE_BLOCKERS
            gameStatus.turn.currentPhaseActivePlayer = gameStatus.nonCurrentPlayer.name
        }
    }

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        val hasFirstStrike = gameStatus.currentPlayer.battlefield.search()
                .withAnyFixedAbility(listOf (AbilityType.FIRST_STRIKE, AbilityType.DOUBLE_STRIKE))
        .isNotEmpty()

        if (hasFirstStrike) {
            return firstStrikePhase
        } else {
            return combatDamagePhase
        }
    }

    companion object {
        const val DB: String = "DB"
    }
}
