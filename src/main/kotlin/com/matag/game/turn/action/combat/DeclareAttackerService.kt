package com.matag.game.turn.action.combat

import com.matag.cards.ability.trigger.TriggerSubtype
import com.matag.cards.ability.type.AbilityType
import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.ContinueService
import com.matag.game.turn.action._continue.InputRequiredActions.DECLARE_ATTACKERS
import com.matag.game.turn.action.tap.TapPermanentService
import com.matag.game.turn.action.trigger.WhenTriggerService
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import org.springframework.stereotype.Component

@Component
class DeclareAttackerService(
    private val continueService: ContinueService,
    private val tapPermanentService: TapPermanentService,
    private val whenTriggerService: WhenTriggerService
) {
    
    fun declareAttackers(gameStatus: GameStatus, cardIds: List<Int>) {
        val turn = gameStatus.turn
        val currentPlayer = gameStatus.currentPlayer

        if (turn.currentPhase != DeclareAttackersPhase.DA || DECLARE_ATTACKERS != turn.inputRequiredAction) {
            throw RuntimeException("Cannot declare attackers. Phase=" + turn.currentPhase)
        }

        cardIds.forEach { checkIfCanAttack(currentPlayer, it) }
        cardIds.forEach { declareAsAttacker(gameStatus, currentPlayer, it) }

        turn.inputRequiredAction = null
        continueService.set(gameStatus)
    }

    private fun checkIfCanAttack(currentPlayer: Player, cardId: Int) {
        currentPlayer.battlefield.findCardById(cardId).checkIfCanAttack()
    }

    private fun declareAsAttacker(gameStatus: GameStatus, currentPlayer: Player, cardId: Int) {
        val cardInstance = currentPlayer.battlefield.findCardById(cardId)
        if (!cardInstance.hasAbilityType(AbilityType.VIGILANCE)) {
            tapPermanentService!!.tap(gameStatus, cardId)
        }
        cardInstance.declareAsAttacker()
        whenTriggerService!!.whenTriggered(gameStatus, cardInstance, TriggerSubtype.WHEN_ATTACK)
    }
}
