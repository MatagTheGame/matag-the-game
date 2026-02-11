package com.matag.game.turn.phases.ending

import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.action.player.DiscardXCardsService
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import com.matag.game.turn.phases.beginning.UntapPhase
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class CleanupPhase(
    autocontinueChecker: AutocontinueChecker,
    @param:Lazy private val untapPhase: UntapPhase,
    private val discardXCardsService: DiscardXCardsService
) : AbstractPhase(autocontinueChecker) {

    override val name = CL

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        return untapPhase
    }

    override fun action(gameStatus: GameStatus) {
        super.action(gameStatus)

        val handSize = gameStatus.currentPlayer.hand.size()
        if (handSize > 7) {
            if (gameStatus.turn.inputRequiredAction == null) {
                discardXCardsService.discardXCardsTrigger(gameStatus, handSize - 7)
            }
        } else {
            gameStatus.turn.cardsPlayedWithinTurn = listOf()
            gameStatus.allBattlefieldCards.forEach(Consumer { obj: CardInstance? -> obj!!.cleanup() })
        }
    }

    companion object {
        const val CL: String = "CL"
    }
}
