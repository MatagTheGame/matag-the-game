package com.matag.game.turn

import com.matag.game.message.MessageEvent
import com.matag.game.message.MessageException
import com.matag.game.security.SecurityHelper
import com.matag.game.status.GameStatusRepository
import com.matag.game.status.GameStatusUpdaterService
import com.matag.game.turn.action._continue.ConsolidateStatusService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller
import java.util.function.Function
import java.util.stream.Collectors

@Controller
class TurnController(
    private val securityHelper: SecurityHelper,
    private val gameStatusRepository: GameStatusRepository,
    private val gameStatusUpdaterService: GameStatusUpdaterService,
    private val turnService: TurnService,
    private val consolidateStatusService: ConsolidateStatusService
) {

    @MessageMapping("/healthcheck")
    fun healthcheck(headerAccessor: SimpMessageHeaderAccessor) {
        gameStatusUpdaterService.sendHealthcheck(headerAccessor.sessionId!!)
    }

    @MessageMapping("/game/turn")
    fun turn(headerAccessor: SimpMessageHeaderAccessor, request: TurnRequest) {
        val token = securityHelper.extractSecurityToken(headerAccessor)
        LOGGER.info(
            "Turn request received for sessionId '{}', gameId '{}': {}",
            token.sessionId,
            token.gameId,
            request
        )
        val gameStatus = gameStatusRepository.get(token.gameId, token.sessionId)
        if (gameStatus.turn.isEnded()) {
            throw RuntimeException("Game is ended, no more actions are permitted.")
        }

        if ("CONTINUE_TURN" == request.action) {
            if (gameStatus.turn.inputRequiredAction != null) {
                throw MessageException("Cannot continue: " + gameStatus.turn.inputRequiredAction)
            }
            turnService.continueTurn(gameStatus)
        } else if ("PLAY_LAND" == request.action) {
            turnService.playLand(gameStatus, request.cardIds.single())
        } else if ("CAST" == request.action) {
            turnService.cast(
                gameStatus,
                request.cardIds.get(0),
                request.mana,
                request.targetsIdsForCardIds,
                request.playedAbility
            )
        } else if ("RESOLVE" == request.action) {
            turnService.resolve(
                gameStatus,
                request.inputRequiredAction!!,
                request.inputRequiredChoices,
                request.cardIds,
                request.targetsIdsForCardIds
            )
        } else if ("DECLARE_ATTACKERS" == request.action) {
            turnService.declareAttackers(gameStatus, request.cardIds)
        } else if ("DECLARE_BLOCKERS" == request.action) {
            turnService.declareBlockers(gameStatus, toMapListInteger(request.targetsIdsForCardIds))
        }

        consolidateStatusService.consolidate(gameStatus)
        gameStatusUpdaterService.sendUpdateGameStatus(gameStatus)
    }

    @MessageExceptionHandler
    fun handleException(headerAccessor: SimpMessageHeaderAccessor, e: MessageException) {
        gameStatusUpdaterService.sendMessage(headerAccessor.sessionId!!, MessageEvent(e.message, true))
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(TurnController::class.java)

        fun toMapListInteger(mapListObject: Map<Int, List<Any>>): Map<Int, List<Int>> {
            return mapListObject.mapValues { toListInteger(it.value) }
        }

        private fun toListInteger(listObject: List<Any>): List<Int> {
            return listObject.map { it as Int }
        }
    }
}
