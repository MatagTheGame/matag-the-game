package com.matag.game.turn.phases

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.phases.ending.CleanupPhase

abstract class AbstractPhase(
    private val autocontinueChecker: AutocontinueChecker? = null
) : Phase {

    override fun action(gameStatus: GameStatus) {}

    override fun set(gameStatus: GameStatus) {
        if (!autocontinueChecker!!.canPerformAnyAction(gameStatus)) {
            next(gameStatus)
        }
    }

    override fun next(gameStatus: GameStatus) {
        if (gameStatus.turn.isEnded()) {
            return
        }

        moveNext(gameStatus)
    }

    private fun moveNext(gameStatus: GameStatus) {
        if (PhaseUtils.isPriorityAllowed(name)) {
            if (gameStatus.isCurrentPlayerActive) {
                moveToNextPlayer(gameStatus)
            } else {
                moveToNextPhase(gameStatus)
            }
        } else {
            moveToNextPhase(gameStatus)
        }
    }

    private fun moveToNextPlayer(gameStatus: GameStatus) {
        gameStatus.turn.passPriority(gameStatus)

        if (!autocontinueChecker!!.canPerformAnyAction(gameStatus)) {
            moveToNextPhase(gameStatus)
        }
    }

    private fun moveToNextPhase(gameStatus: GameStatus) {
        if (name == CleanupPhase.Companion.CL) {
            gameStatus.turn.currentTurnPlayer = gameStatus.nonCurrentPlayer.name
            gameStatus.turn.increaseTurnNumber()
        }

        val nextPhase = getNextPhase(gameStatus)
        gameStatus.turn.currentPhase = nextPhase.name
        gameStatus.turn.currentPhaseActivePlayer = gameStatus.turn.currentTurnPlayer
        nextPhase.action(gameStatus)

        if (!autocontinueChecker!!.canPerformAnyAction(gameStatus)) {
            nextPhase.next(gameStatus)
        }
    }
}
