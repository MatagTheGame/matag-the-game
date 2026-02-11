package com.matag.game.turn.action._continue

import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.PhaseFactory
import org.springframework.stereotype.Component

@Component
class ContinueService(
    private val phaseFactory: PhaseFactory
) {

    fun set(gameStatus: GameStatus) {
        val phase = phaseFactory.get(gameStatus.turn.currentPhase!!)
        phase.set(gameStatus)
    }

    fun next(gameStatus: GameStatus) {
        val phase = phaseFactory.get(gameStatus.turn.currentPhase!!)
        phase.next(gameStatus)
    }
}
