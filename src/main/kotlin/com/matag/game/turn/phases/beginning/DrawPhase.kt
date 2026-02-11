package com.matag.game.turn.phases.beginning

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.action.player.DrawXCardsService
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import com.matag.game.turn.phases.main1.Main1Phase
import org.springframework.stereotype.Component

@Component
class DrawPhase(
    autocontinueChecker: AutocontinueChecker,
    private val drawXCardsService: DrawXCardsService,
    private val main1Phase: Main1Phase
) : AbstractPhase(autocontinueChecker) {

    override val name = DR

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        return main1Phase
    }

    override fun action(gameStatus: GameStatus) {
        super.action(gameStatus)

        if (gameStatus.turn.turnNumber > 1) {
            drawXCardsService.drawXCards(gameStatus.currentPlayer, 1, gameStatus)
        }
    }

    companion object {
        const val DR: String = "DR"
    }
}
