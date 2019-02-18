package com.aa.mtg.game.turn;

import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusRepository;
import com.aa.mtg.security.SecurityToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static com.aa.mtg.security.SecurityHelper.extractSecurityToken;

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
    public void turn(SimpMessageHeaderAccessor headerAccessor, TurnRequest request) {
        SecurityToken token = extractSecurityToken(headerAccessor);
        LOGGER.info("Turn request received for sessionId '{}', gameId '{}'", token.getSessionId(), token.getGameId());

        GameStatus gameStatus = gameStatusRepository.get(token.getGameId(), token);
        LOGGER.info("Request: {}", request);

        if (request.getAction().equals("PASS_PRIORITY")) {
            gameStatus.passPriority();
        }

        eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("UPDATE_TURN", gameStatus.getTurn()));
        eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("UPDATE_TURN", gameStatus.getTurn()));
    }

}
