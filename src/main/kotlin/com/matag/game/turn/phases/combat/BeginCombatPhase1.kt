package com.matag.game.turn.phases.combat

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import com.matag.game.turn.phases.main2.Main2Phase
import org.springframework.stereotype.Component

@Component
class BeginCombatPhase(
    autocontinueChecker: AutocontinueChecker,
    private val declareAttackersPhase: DeclareAttackersPhase,
    private val main2Phase: Main2Phase
) : AbstractPhase(autocontinueChecker) {
    override val name = BC

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        if (gameStatus.currentPlayer.battlefield.search().canAnyCreatureAttack()) {
            return declareAttackersPhase
        } else {
            return main2Phase
        }
    }

    companion object {
        const val BC: String = "BC"
    }
}
