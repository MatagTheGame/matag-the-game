package com.matag.game.turn.action.combat

import com.matag.cards.ability.trigger.TriggerSubtype
import com.matag.game.cardinstance.CardInstance
import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.ContinueService
import com.matag.game.turn.action._continue.InputRequiredActions.DECLARE_BLOCKERS
import com.matag.game.turn.action.trigger.WhenTriggerService
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import org.springframework.stereotype.Component

@Component
class DeclareBlockerService(
    private val continueService: ContinueService,
    private val blockerChecker: BlockerChecker,
    private val whenTriggerService: WhenTriggerService,
) {
    
    fun declareBlockers(
        gameStatus: GameStatus,
        blockingCreaturesIdsForAttackingCreaturesIds: Map<Int, List<Int>>
    ) {
        val turn = gameStatus.turn
        val currentPlayer = gameStatus.currentPlayer
        val nonCurrentPlayer = gameStatus.nonCurrentPlayer

        if (turn.currentPhase != DeclareBlockersPhase.DB || DECLARE_BLOCKERS != turn.inputRequiredAction) {
            throw RuntimeException("Cannot declare blockers. Phase=" + turn.currentPhase)
        }

        blockingCreaturesIdsForAttackingCreaturesIds.forEach { (blockingCreatureId: Int?, blockedCreaturesIds: List<Int>) ->
            val attacker = currentPlayer.battlefield.findCardById(blockedCreaturesIds[0])
            val blockers = findBlockers(blockingCreaturesIdsForAttackingCreaturesIds, nonCurrentPlayer, attacker)
            blockerChecker.checkIfCanBlock(attacker, blockers)
        }

        blockingCreaturesIdsForAttackingCreaturesIds.forEach { (blockingCreatureId: Int, blockedCreaturesIds: List<Int>) ->
            val attackingCreatureId = blockedCreaturesIds[0]
            currentPlayer.battlefield.findCardById(attackingCreatureId).modifiers.modifiersUntilEndOfTurn.isBlocked = true
            val blockingCreature: CardInstance = nonCurrentPlayer.battlefield.findCardById(blockingCreatureId)
            blockingCreature.declareAsBlocker(attackingCreatureId)
            whenTriggerService.whenTriggered(gameStatus, blockingCreature, TriggerSubtype.WHEN_BLOCK)
        }

        turn.inputRequiredAction = null
        turn.currentPhaseActivePlayer = turn.currentTurnPlayer
        continueService.set(gameStatus)
    }

    private fun findBlockers(
        blockingCreaturesIdsForAttackingCreaturesIds: Map<Int, List<Int>>,
        nonCurrentPlayer: Player,
        attacker: CardInstance
    ): List<CardInstance> {
        return blockingCreaturesIdsForAttackingCreaturesIds.keys
            .map { nonCurrentPlayer.battlefield.findCardById(it) }
            .filter { blockingCreaturesIdsForAttackingCreaturesIds[it.id]!!.indexOf(attacker.id) == 0 }
    }
}
