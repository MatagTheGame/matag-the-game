package com.aa.mtg.game.turn;

import com.aa.mtg.event.EventSender;
import com.aa.mtg.game.status.GameStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class TurnController {
    private Logger LOGGER = LoggerFactory.getLogger(TurnController.class);

    private EventSender eventSender;
    private GameStatusRepository gameStatusRepository;

    public TurnController(EventSender eventSender, GameStatusRepository gameStatusRepository) {
        this.eventSender = eventSender;
        this.gameStatusRepository = gameStatusRepository;
    }

    @MessageMapping("/game/turn")
    void turn(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        LOGGER.info("Turn request received from sessionId '{}'", sessionId);
    }

}
