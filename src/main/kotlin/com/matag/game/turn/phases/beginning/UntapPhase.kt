package com.matag.game.turn.phases.beginning

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.action.tap.TapPermanentService
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import org.springframework.stereotype.Component

@Component
class UntapPhase(
    autocontinueChecker: AutocontinueChecker?,
    private val tapPermanentService: TapPermanentService,
    private val upkeepPhase: UpkeepPhase
) : AbstractPhase(autocontinueChecker) {

    override val name = UT

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        return upkeepPhase
    }

    override fun action(gameStatus: GameStatus) {
        super.action(gameStatus)

        for (cardInstance in gameStatus.currentPlayer.battlefield.search().tapped().cards) {
            if (cardInstance.modifiers.doesNotUntapNextTurn) {
                cardInstance.modifiers.doesNotUntapNextTurn = false
            } else {
                tapPermanentService.untap(gameStatus, cardInstance.id)
            }
        }

        gameStatus.currentPlayer.battlefield.search().withSummoningSickness().cards
            .forEach({ cardInstance -> cardInstance.modifiers.isSummoningSickness = false })
    }

    companion object {
        const val UT: String = "UT"
    }
}
