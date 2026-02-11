package com.matag.game.status

import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.event.Event
import com.matag.game.event.EventSender
import com.matag.game.message.MessageEvent
import com.matag.game.player.Player
import com.matag.game.player.PlayerUpdateEvent
import com.matag.game.turn.action._continue.InputRequiredActions
import org.springframework.stereotype.Component

@Component
class GameStatusUpdaterService(
    private val eventSender: EventSender,
    private val cardInstanceFactory: CardInstanceFactory
) {

    fun sendHealthcheck(sessionId: String) {
        eventSender!!.sendToUser(sessionId, Event("HEALTHCHECK"))
    }

    fun sendUpdateGameStatus(gameStatus: GameStatus) {
        eventSender!!.sendToPlayer(
            gameStatus.player1!!,
            Event(
                "UPDATE_GAME_STATUS",
                gameStatus.player1!!,
                gameStatusUpdateEvent(gameStatus, gameStatus.player1)
            )
        )

        eventSender.sendToPlayer(
            gameStatus.player2!!,
            Event(
                "UPDATE_GAME_STATUS",
                gameStatus.player2!!,
                gameStatusUpdateEvent(gameStatus, gameStatus.player2)
            )
        )
    }

    private fun gameStatusUpdateEvent(gameStatus: GameStatus, forPlayer: Player?): GameStatusUpdateEvent {
        return GameStatusUpdateEvent(
            turn = gameStatus.turn,
            stack = gameStatus.stack.items,
            playersUpdateEvents = setOf(
                playerUpdateEvent(gameStatus, gameStatus.player1!!, forPlayer),
                playerUpdateEvent(gameStatus, gameStatus.player2!!, forPlayer)
            )
        )
    }

    private fun playerUpdateEvent(gameStatus: GameStatus, player: Player, forPlayer: Player?): PlayerUpdateEvent {
        return PlayerUpdateEvent(
            name = player.name,
            life = player.life,
            librarySize = player.library.size(),
            visibleLibrary = playerVisibleLibraryToThem(gameStatus, player, forPlayer),
            battlefield = player.battlefield.cards,
            graveyard = player.graveyard.cards,
            hand = playerHand(player, forPlayer)
        )
    }

    private fun playerVisibleLibraryToThem(
        gameStatus: GameStatus,
        player: Player,
        forPlayer: Player?
    ): List<CardInstance> {
        if (InputRequiredActions.SCRY == gameStatus.turn.inputRequiredAction) {
            if (gameStatus.turn.currentPhaseActivePlayer == player.name) {
                val cardsToScry = gameStatus.turn.inputRequiredActionParameter!!.toInt()
                val cardsScried = player.library.peek(cardsToScry)
                if (player == forPlayer) {
                    return cardsScried
                } else {
                    return cardInstanceFactory.mask(cardsScried)
                }
            }
        }
        return listOf()
    }

    private fun playerHand(player: Player, forPlayer: Player?): List<CardInstance> {
        if (player == forPlayer) {
            return player.hand.cards
        } else {
            return player.hand.maskedHand()
        }
    }

    fun sendMessage(sessionId: String, messageEvent: MessageEvent?) {
        eventSender.sendToUser(sessionId, Event("MESSAGE", messageEvent))
    }
}
