package com.matag.game.turn.phases.combat

import com.matag.cards.ability.type.AbilityType
import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.action._continue.ConsolidateStatusService
import com.matag.game.turn.action.combat.CombatService
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import org.springframework.stereotype.Component

@Component
class FirstStrikePhase(
    autocontinueChecker: AutocontinueChecker,
    private val combatService: CombatService,
    private val consolidateStatusService: ConsolidateStatusService,
    private val combatDamagePhase: CombatDamagePhase
) : AbstractPhase(autocontinueChecker) {
    override val name = FS

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        return combatDamagePhase
    }

    override fun action(gameStatus: GameStatus) {
        super.action(gameStatus)
        val executePhase = gameStatus.currentPlayer.battlefield.search()
                .withAnyFixedAbility(listOf (AbilityType.FIRST_STRIKE, AbilityType.DOUBLE_STRIKE))
                .isNotEmpty()

        if (executePhase) {
            combatService.dealCombatDamage(gameStatus)
            consolidateStatusService.consolidate(gameStatus)
        }
    }

    companion object {
        const val FS: String = "FS"
    }
}
