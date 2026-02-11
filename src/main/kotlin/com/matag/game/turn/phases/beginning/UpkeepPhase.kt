package com.matag.game.turn.phases.beginning

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import org.springframework.stereotype.Component

@Component
class UpkeepPhase(autocontinueChecker: AutocontinueChecker?, private val drawPhase: DrawPhase) :
    AbstractPhase(autocontinueChecker) {

    override val name = UP

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        return drawPhase
    }

    companion object {
        const val UP: String = "UP"
    }
}
