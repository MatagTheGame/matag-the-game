package com.matag.game.turn.action.player

import com.matag.cards.ability.AbilityService
import com.matag.game.cardinstance.CardInstance
import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.damage.DealDamageToPlayerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class PlayerGetService(
    private val abilityService: AbilityService,
    private val dealDamageToPlayerService: DealDamageToPlayerService,
    private val lifeService: LifeService,
    private val drawXCardsService: DrawXCardsService,
    private val scryXCardsService: ScryXCardsService,
) {
    
    fun thatPlayerGets(
        cardInstance: CardInstance,
        gameStatus: GameStatus,
        parameters: List<String>,
        player: Player
    ) {
        parameters.forEach(Consumer { parameter: String? ->
            thatPlayerGets(
                cardInstance,
                gameStatus,
                player,
                parameter!!
            )
        })
    }

    private fun thatPlayerGets(cardInstance: CardInstance, gameStatus: GameStatus, player: Player, parameter: String) {
        dealDamageToPlayerService.dealDamageToPlayer(gameStatus, abilityService.damageFromParameter(parameter), player)
        lifeService.add(player, abilityService.lifeFromParameter(parameter), gameStatus)
        drawXCardsService.drawXCards(player, abilityService.drawFromParameter(parameter), gameStatus)
        scryXCardsService.scryXCardsTrigger(player, abilityService.scryFromParameter(parameter), gameStatus)
        LOGGER.info(
            "AbilityActionExecuted: card {}, parameter {}, player {}",
            cardInstance.idAndName,
            parameter,
            player.name
        )
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PlayerGetService::class.java)
    }
}
