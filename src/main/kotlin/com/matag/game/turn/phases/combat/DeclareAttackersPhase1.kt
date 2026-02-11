package com.matag.game.turn.phases.combat

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.action._continue.InputRequiredActions.DECLARE_ATTACKERS
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import com.matag.game.turn.phases.main2.Main2Phase
import org.springframework.stereotype.Component

@Component
class DeclareAttackersPhase(
    autocontinueChecker: AutocontinueChecker,
    private val declareBlockersPhase: DeclareBlockersPhase,
    private val main2Phase: Main2Phase
) : AbstractPhase(autocontinueChecker) {
    override val name = DA

    override fun action(gameStatus: GameStatus) {
        gameStatus.turn.inputRequiredAction = DECLARE_ATTACKERS
    }

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        if (gameStatus.currentPlayer.battlefield.attackingCreatures.isEmpty()) {
            return main2Phase
        } else {
            return declareBlockersPhase
        }
    }

    companion object {
        const val DA: String = "DA"
    }
}
