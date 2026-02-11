package com.matag.game.turn.phases.combat

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import com.matag.game.turn.phases.main2.Main2Phase
import org.springframework.stereotype.Component

@Component
class EndOfCombatPhase(autocontinueChecker: AutocontinueChecker, private val main2Phase: Main2Phase) :
    AbstractPhase(autocontinueChecker) {

    override val name = EC

    override fun action(gameStatus: GameStatus) {
        super.action(gameStatus)
        gameStatus.currentPlayer.battlefield.removeAttacking()
        gameStatus.nonCurrentPlayer.battlefield.removeBlocking()
    }

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        return main2Phase
    }

    companion object {
        const val EC: String = "EC"
    }
}
