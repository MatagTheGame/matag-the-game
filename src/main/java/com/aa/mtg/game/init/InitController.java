package com.aa.mtg.game.init;

import com.aa.mtg.cards.model.CardInstance;
import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import com.aa.mtg.game.deck.DeckRetrieverService;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusRepository;
import com.aa.mtg.security.SecurityToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.aa.mtg.security.SecurityHelper.extractSecurityToken;

@Controller
public class InitController {
    private Logger LOGGER = LoggerFactory.getLogger(InitController.class);

    private final EventSender eventSender;
    private final GameStatusRepository gameStatusRepository;
    private final DeckRetrieverService deckRetrieverService;

    public InitController(EventSender eventSender, GameStatusRepository gameStatusRepository, DeckRetrieverService deckRetrieverService) {
        this.eventSender = eventSender;
        this.gameStatusRepository = gameStatusRepository;
        this.deckRetrieverService = deckRetrieverService;
    }

    @MessageMapping("/game/init")
    void init(SimpMessageHeaderAccessor headerAccessor) {
        SecurityToken token = extractSecurityToken(headerAccessor);
        LOGGER.info("Init request received for sessionId '{}', gameId '{}'", token.getSessionId(), token.getGameId());

        if (!gameStatusRepository.contains(token.getGameId())) {
            GameStatus gameStatus = new GameStatus(token.getGameId());
            Library library = deckRetrieverService.retrieveDeckForUser(token, gameStatus);
            gameStatus.setPlayer1(new Player(token.getSessionId(), "Pippo", library));
            gameStatusRepository.save(token.getGameId(), gameStatus);
            eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_WAITING_OPPONENT"));

        } else {
            GameStatus gameStatus = gameStatusRepository.getUnsecure(token.getGameId());
            if (gameStatus.getPlayer2() == null) {
                Library library = deckRetrieverService.retrieveDeckForUser(token, gameStatus);
                gameStatus.setPlayer2(new Player(token.getSessionId(), "Pluto", library));

                gameStatus.getTurn().init(gameStatus.getPlayer1().getName());

                eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("OPPONENT_JOINED"));

                eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(gameStatus.getPlayer1())));
                eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getPlayer2())));
                eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("UPDATE_TURN", gameStatus.getTurn()));

                eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(gameStatus.getPlayer2())));
                eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getPlayer1())));
                eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("UPDATE_TURN", gameStatus.getTurn()));

            } else {
                throw new RuntimeException("Game is full");
            }
        }
    }

}
