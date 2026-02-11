package com.matag.game.turn.phases.ending

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import org.springframework.stereotype.Component

@Component
class EndTurnPhase(autocontinueChecker: AutocontinueChecker, private val cleanupPhase: CleanupPhase) :
    AbstractPhase(autocontinueChecker) {
    override val name = ET

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        return cleanupPhase
    }

    companion object {
        const val ET: String = "ET"
    }
}
