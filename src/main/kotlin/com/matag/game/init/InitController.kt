package com.matag.game.init

import com.matag.game.cardinstance.CardInstance
import com.matag.game.config.ConfigService
import com.matag.game.deck.DeckFactory
import com.matag.game.deck.DeckRetrieverService
import com.matag.game.event.Event
import com.matag.game.event.EventSender
import com.matag.game.player.Player
import com.matag.game.player.PlayerFactory
import com.matag.game.player.PlayerService
import com.matag.game.player.playerInfo.PlayerInfoRetriever
import com.matag.game.security.SecurityHelper
import com.matag.game.security.SecurityToken
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusFactory
import com.matag.game.status.GameStatusRepository
import com.matag.game.status.GameStatusUpdaterService
import com.matag.game.turn.action._continue.ContinueService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class InitController(
    private val securityHelper: SecurityHelper,
    private val eventSender: EventSender,
    private val configService: ConfigService,
    private val gameStatusFactory: GameStatusFactory,
    private val playerInfoRetriever: PlayerInfoRetriever,
    private val playerFactory: PlayerFactory,
    private val playerService: PlayerService,
    private val gameStatusUpdaterService: GameStatusUpdaterService,
    private val gameStatusRepository: GameStatusRepository,
    private val deckRetrieverService: DeckRetrieverService,
    private val deckFactory: DeckFactory,
    private val continueService: ContinueService
) {
    @MessageMapping("/game/init")
    fun init(headerAccessor: SimpMessageHeaderAccessor) {
        val token = securityHelper.extractSecurityToken(headerAccessor)
        LOGGER.info("Init request received for sessionId '{}', gameId '{}'.", token.sessionId, token.gameId)

        if (!gameStatusRepository.contains(token.gameId)) {
            val gameStatus = gameStatusFactory.create(token.gameId)
            val player1 = retrievePlayer(token)
            gameStatus.player1 = player1
            gameStatus.player1!!.library.addCards(retrieveDeck(player1, gameStatus))
            gameStatus.player1!!.drawHand()
            gameStatusRepository.save(token.gameId, gameStatus)
            eventSender.sendToPlayer(gameStatus.player1!!, Event("INIT_WAITING_OPPONENT"))
        } else {
            val gameStatus = gameStatusRepository.getUnsecure(token.gameId)
            if (gameStatus!!.player2 == null && gameStatus.player1!!.token
                    .sessionId != token.sessionId
            ) {
                val player2 = retrievePlayer(token)
                gameStatus.player2 = player2
                gameStatus.player2!!.library.addCards(retrieveDeck(player2, gameStatus))
                gameStatus.player2!!.drawHand()

                gameStatus.turn.init(gameStatus.player1!!.name!!)

                eventSender.sendToPlayer(gameStatus.player1!!, Event("OPPONENT_JOINED"))

                eventSender.sendToPlayer(
                    gameStatus.player1!!,
                    Event(
                        "INIT_PLAYER_AND_OPPONENT",
                        initPlayerAndOpponentEvent(gameStatus.player1!!, gameStatus.player2!!)
                    )
                )
                eventSender.sendToPlayer(
                    gameStatus.player2!!,
                    Event(
                        "INIT_PLAYER_AND_OPPONENT",
                        initPlayerAndOpponentEvent(gameStatus.player2!!, gameStatus.player1!!)
                    )
                )

                continueService.set(gameStatus)

                addDelayBeforeSendingUpdateGameStatusEvent()
                gameStatusUpdaterService.sendUpdateGameStatus(gameStatus)
            } else {
                val player = playerService.getPlayerByToken(gameStatus, token.adminToken)
                player.token = token

                eventSender.sendToPlayer(
                    player,
                    Event(
                        "INIT_PLAYER_AND_OPPONENT",
                        initPlayerAndOpponentEvent(player, gameStatus.getOtherPlayer(player))
                    )
                )

                gameStatusUpdaterService.sendUpdateGameStatus(gameStatus)
            }
        }
    }

    private fun retrievePlayer(token: SecurityToken?): Player {
        val playerInfo = playerInfoRetriever.retrieve(token)
        return playerFactory.create(token!!, playerInfo!!)
    }

    private fun retrieveDeck(player: Player, gameStatus: GameStatus): List<CardInstance> {
        val deckInfo = deckRetrieverService.retrieveDeckForUser(player.token)
        return deckFactory.create(player.name, gameStatus, deckInfo)
    }

    private fun addDelayBeforeSendingUpdateGameStatusEvent() {
        Thread.sleep(100)
    }

    private fun initPlayerAndOpponentEvent(player: Player, opponent: Player): Map<String, InitPlayerEvent> {
        val gameConfig = GameConfig(configService.matagAdminPath)
        return mapOf(
            "player" to InitPlayerEvent(player.name, gameConfig),
            "opponent" to InitPlayerEvent(opponent.name, null)
        )
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(InitController::class.java)
    }
}
