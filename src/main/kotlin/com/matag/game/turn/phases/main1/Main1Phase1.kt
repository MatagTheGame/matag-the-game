package com.matag.game.turn.phases.main1

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import com.matag.game.turn.phases.combat.BeginCombatPhase
import org.springframework.stereotype.Component

@Component
class Main1Phase(autocontinueChecker: AutocontinueChecker, private val beginCombatPhase: BeginCombatPhase) :
    AbstractPhase(autocontinueChecker) {

    override val name = M1

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        return beginCombatPhase
    }

    companion object {
        const val M1: String = "M1"
    }
}
