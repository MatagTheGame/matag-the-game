package com.matag.game.turn.phases.combat

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.action.combat.CombatService
import com.matag.game.turn.phases.AbstractPhase
import com.matag.game.turn.phases.Phase
import org.springframework.stereotype.Component

@Component
class CombatDamagePhase(
    autocontinueChecker: AutocontinueChecker,
    private val combatService: CombatService,
    private val endOfCombatPhase: EndOfCombatPhase
) : AbstractPhase(autocontinueChecker) {

    override val name = CD

    override fun getNextPhase(gameStatus: GameStatus): Phase {
        return endOfCombatPhase
    }

    override fun action(gameStatus: GameStatus) {
        super.action(gameStatus)
        combatService.dealCombatDamage(gameStatus)
    }

    companion object {
        const val CD: String = "CD"
    }
}
