package com.matag.game.turn.action.damage

import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.player.LifeService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DealDamageToPlayerService(
    private val lifeService: LifeService
) {

    fun dealDamageToPlayer(gameStatus: GameStatus, damage: Int, player: Player) {
        if (damage > 0) {
            lifeService.add(player, -damage, gameStatus)
            LOGGER.info("AbilityActionExecuted: deals {} damage to {}", damage, player.name)
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DealDamageToPlayerService::class.java)
    }
}
