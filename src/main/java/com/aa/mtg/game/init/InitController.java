package com.aa.mtg.game.init;

import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusRepository;
import com.aa.mtg.game.turn.phases.PlayerPhasesConfig;
import com.aa.mtg.security.SecurityToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static com.aa.mtg.security.SecurityHelper.extractSecurityToken;

@Controller
public class InitController {
    private Logger LOGGER = LoggerFactory.getLogger(InitController.class);

    private EventSender eventSender;
    private GameStatusRepository gameStatusRepository;

    public InitController(EventSender eventSender, GameStatusRepository gameStatusRepository) {
        this.eventSender = eventSender;
        this.gameStatusRepository = gameStatusRepository;
    }

    @MessageMapping("/game/init")
    void init(SimpMessageHeaderAccessor headerAccessor) {
        SecurityToken token = extractSecurityToken(headerAccessor);
        LOGGER.info("Init request received for sessionId '{}', gameId '{}'", token.getSessionId(), token.getGameId());

        if (!gameStatusRepository.contains(token.getGameId())) {
            GameStatus gameStatus = new GameStatus(token.getGameId());
            gameStatus.setPlayer1(new Player(token.getSessionId(), "Pippo"));
            gameStatusRepository.save(token.getGameId(), gameStatus);
            eventSender.sendToUser(token.getSessionId(), new Event("INIT_WAITING_OPPONENT"));

        } else {
            GameStatus gameStatus = gameStatusRepository.getUnsecure(token.getGameId());
            if (gameStatus.getPlayer2() == null) {
                gameStatus.setPlayer2(new Player(token.getSessionId(), "Pluto"));

                gameStatus.getTurn().init(gameStatus.getPlayer1().getName());

                String player1SessionId = gameStatus.getPlayer1().getSessionId();
                String player2SessionId = gameStatus.getPlayer2().getSessionId();

                eventSender.sendToUser(player1SessionId, new Event("OPPONENT_JOINED"));

                eventSender.sendToUser(player1SessionId, new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(gameStatus.getPlayer1())));
                eventSender.sendToUser(player1SessionId, new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getPlayer2())));
                eventSender.sendToUser(player1SessionId, new Event("INIT_PHASES_CONFIG", new InitPlayerPhasesConfigEvent(PlayerPhasesConfig.defaultPlayerPhasesConfig().getConfig())));
                eventSender.sendToUser(player1SessionId, new Event("UPDATE_TURN", gameStatus.getTurn()));

                eventSender.sendToUser(player2SessionId, new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(gameStatus.getPlayer2())));
                eventSender.sendToUser(player2SessionId, new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getPlayer1())));
                eventSender.sendToUser(player2SessionId, new Event("INIT_PHASES_CONFIG", new InitPlayerPhasesConfigEvent(PlayerPhasesConfig.defaultPlayerPhasesConfig().getConfig())));
                eventSender.sendToUser(player2SessionId, new Event("UPDATE_TURN", gameStatus.getTurn()));

            } else {
                throw new RuntimeException("Game is full");
            }
        }
    }

}
