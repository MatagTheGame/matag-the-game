package com.matag.game.turn.phases.main2

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import com.matag.game.turn.phases.ending.EndTurnPhase
import org.springframework.stereotype.Component

@Component
class Main2Phase(autocontinueChecker: AutocontinueChecker, private val endTurnPhase: EndTurnPhase) :
    AbstractPhase(autocontinueChecker) {

    override val name = M2

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        return endTurnPhase
    }

    companion object {
        const val M2: String = "M2"
    }
}
