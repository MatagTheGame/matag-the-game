package com.aa.mtg.event;

import com.aa.mtg.game.player.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Sends events to the browser.
 */
@Component
public class EventSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventSender.class);

    private final SimpMessagingTemplate webSocketTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventSender(SimpMessagingTemplate simpleMessagingTemplate, ObjectMapper objectMapper) {
        this.webSocketTemplate = simpleMessagingTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendToAll(Event event) {
        String eventString = serializeToString(event);
        LOGGER.info("Sending event to all: {}", eventString);
        webSocketTemplate.convertAndSend("/topic/events", eventString);
    }

    public void sendToPlayer(Player player, Event event) {
        String eventString = serializeToString(event);
        LOGGER.info("Sending event to {}: {}", player.getSessionId(), eventString);
        webSocketTemplate.convertAndSendToUser(player.getSessionId(), "/events", eventString);
    }

    private String serializeToString(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
