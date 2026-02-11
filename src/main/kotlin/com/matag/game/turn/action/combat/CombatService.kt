package com.matag.game.turn.action.combat

import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.damage.DealDamageToCreatureService
import com.matag.game.turn.action.damage.DealDamageToPlayerService
import com.matag.game.turn.action.player.LifeService
import com.matag.game.turn.phases.combat.FirstStrikePhase
import org.springframework.stereotype.Component
import kotlin.math.min

@Component
class CombatService(
    private val lifeService: LifeService,
    private val dealDamageToCreatureService: DealDamageToCreatureService,
    private val dealDamageToPlayerService: DealDamageToPlayerService
) {
    
    fun dealCombatDamage(gameStatus: GameStatus) {
        val currentPlayer = gameStatus.currentPlayer
        val nonCurrentPlayer = gameStatus.nonCurrentPlayer

        val attackingCreatures = currentPlayer.battlefield.attackingCreatures

        var damageFromUnblockedCreatures = 0
        var lifelink = 0
        for (attackingCreature in attackingCreatures) {
            val blockingCreaturesFor = nonCurrentPlayer.battlefield.getBlockingCreaturesFor(attackingCreature.id)
            val remainingDamage = dealCombatDamageForOneAttackingCreature(gameStatus, attackingCreature, blockingCreaturesFor)

            val isBlocked = attackingCreature.modifiers.modifiersUntilEndOfTurn.isBlocked
            if (!isBlocked || attackingCreature.hasAbilityType(AbilityType.TRAMPLE)) {
                if (shouldDealDamage(gameStatus, attackingCreature)) {
                    damageFromUnblockedCreatures += remainingDamage
                }
            }

            if (attackingCreature.hasAbilityType(AbilityType.LIFELINK)) {
                if (!isBlocked || !blockingCreaturesFor.isEmpty()) {
                    lifelink += attackingCreature.power
                }
            }
        }

        if (damageFromUnblockedCreatures > 0) {
            dealDamageToPlayerService.dealDamageToPlayer(gameStatus, damageFromUnblockedCreatures, nonCurrentPlayer)
        }

        if (lifelink > 0) {
            lifeService.add(currentPlayer, lifelink, gameStatus)
        }
    }

    private fun dealCombatDamageForOneAttackingCreature(
        gameStatus: GameStatus,
        attackingCreature: CardInstance,
        blockingCreatures: List<CardInstance>
    ): Int {
        var remainingDamageForAttackingCreature = attackingCreature.power
        for (blockingCreature in blockingCreatures) {
            val damageToCurrentBlocker = min(remainingDamageForAttackingCreature, blockingCreature.toughness)

            if (shouldDealDamage(gameStatus, blockingCreature)) {
                dealDamageToCreatureService.dealDamageToCreature(
                    gameStatus,
                    attackingCreature,
                    blockingCreature.power,
                    blockingCreature.hasAbilityType(AbilityType.DEATHTOUCH),
                    blockingCreature
                )
                remainingDamageForAttackingCreature -= damageToCurrentBlocker
            } else {
                remainingDamageForAttackingCreature = 0
            }

            if (shouldDealDamage(gameStatus, attackingCreature)) {
                dealDamageToCreatureService!!.dealDamageToCreature(
                    gameStatus,
                    blockingCreature,
                    damageToCurrentBlocker,
                    attackingCreature.hasAbilityType(AbilityType.DEATHTOUCH),
                    attackingCreature
                )
            }
        }
        return remainingDamageForAttackingCreature
    }

    private fun shouldDealDamage(gameStatus: GameStatus, cardInstance: CardInstance): Boolean {
        if (gameStatus.turn.currentPhase.equals(FirstStrikePhase.FS)) {
            return cardInstance.hasAnyFixedAbility(
                listOf(
                    AbilityType.FIRST_STRIKE,
                    AbilityType.DOUBLE_STRIKE
                )
            )
        } else {
            return !cardInstance.hasFixedAbility(AbilityType.FIRST_STRIKE)
        }
    }
}
