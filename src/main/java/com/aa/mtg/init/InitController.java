package com.aa.mtg.init;

import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import com.aa.mtg.status.GameStatus;
import com.aa.mtg.status.GameStatusRepository;
import com.aa.mtg.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class InitController {
    private Logger LOGGER = LoggerFactory.getLogger(InitController.class);

    private EventSender eventSender;
    private GameStatusRepository gameStatusRepository;

    public InitController(EventSender eventSender, GameStatusRepository gameStatusRepository) {
        this.eventSender = eventSender;
        this.gameStatusRepository = gameStatusRepository;
    }

    @MessageMapping("/init")
    void init(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        LOGGER.info("Init request received for sessionId '{}'", sessionId);

        GameStatus gameStatus = gameStatusRepository.get();

        if (gameStatus.getPlayer1() == null) {
            gameStatus.setPlayer1(new Player(sessionId, "Pippo"));
            eventSender.sendToUser(sessionId, new Event("INIT_WAITING_OPPONENT"));

        } else if (gameStatus.getPlayer2() == null) {
            gameStatus.setPlayer2(new Player (sessionId, "Pluto"));

            String player1SessionId = gameStatus.getPlayer1().getSessionId();
            String player2SessionId = gameStatus.getPlayer2().getSessionId();

            eventSender.sendToUser(player1SessionId, new Event("OPPONENT_JOINED"));
            eventSender.sendToUser(player1SessionId, new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(gameStatus.getPlayer1())));
            eventSender.sendToUser(player2SessionId, new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(gameStatus.getPlayer2())));
            eventSender.sendToUser(player1SessionId, new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getPlayer2())));
            eventSender.sendToUser(player2SessionId, new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getPlayer1())));

        } else {
            throw new RuntimeException("Game is full");
        }
    }

}
