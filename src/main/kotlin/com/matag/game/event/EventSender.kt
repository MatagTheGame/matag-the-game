package com.matag.game.event

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.matag.game.player.Player
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import java.util.function.Consumer

/**
 * Sends events to the browser.
 */
@Component
class EventSender {
    private val webSocketTemplate: SimpMessagingTemplate? = null
    private val objectMapper: ObjectMapper? = null

    fun sendToUser(sessionId: String, username: String?, event: Event) {
        val eventString = serializeToString(event)
        if (event.type != "HEALTHCHECK") {
            if (username != null) {
                LOGGER.info("Sending event to {} - {}: {}", sessionId, username, eventString)
            } else {
                LOGGER.info("Sending event to {}: {}", sessionId, eventString)
            }
        }
        webSocketTemplate!!.convertAndSendToUser(sessionId, "/events", eventString)
    }

    fun sendToUser(sessionId: String, event: Event) {
        sendToUser(sessionId, null, event)
    }

    fun sendToPlayer(player: Player, event: Event) {
        sendToUser(player.token.sessionId, player.name, event)
    }

    fun sendToPlayers(players: MutableList<Player?>, event: Event) {
        players.forEach(Consumer { player: Player? -> sendToPlayer(player!!, event) })
    }

    private fun serializeToString(event: Any?): String {
        try {
            return objectMapper!!.writeValueAsString(event)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(EventSender::class.java)
    }
}
