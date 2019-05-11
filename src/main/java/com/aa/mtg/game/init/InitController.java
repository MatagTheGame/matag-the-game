package com.aa.mtg.game.init;

import com.aa.mtg.game.deck.DeckRetrieverService;
import com.aa.mtg.game.event.Event;
import com.aa.mtg.game.event.EventSender;
import com.aa.mtg.game.message.MessageEvent;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.security.SecurityToken;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusRepository;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static com.aa.mtg.game.security.SecurityHelper.extractSecurityToken;

@Controller
public class InitController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitController.class);

    private final EventSender eventSender;
    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final InitTestService initTestService;
    private final GameStatusRepository gameStatusRepository;
    private final DeckRetrieverService deckRetrieverService;

    @Autowired
    public InitController(EventSender eventSender, GameStatusUpdaterService gameStatusUpdaterService, GameStatusRepository gameStatusRepository, DeckRetrieverService deckRetrieverService, @Autowired(required = false) InitTestService initTestService) {
        this.eventSender = eventSender;
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.gameStatusRepository = gameStatusRepository;
        this.deckRetrieverService = deckRetrieverService;
        this.initTestService = initTestService;
    }

    @MessageMapping("/game/init")
    void init(SimpMessageHeaderAccessor headerAccessor) {
        SecurityToken token = extractSecurityToken(headerAccessor);
        LOGGER.info("Init request received for sessionId '{}', gameId '{}'.", token.getSessionId(), token.getGameId());

        if (!gameStatusRepository.contains(token.getGameId())) {
            GameStatus gameStatus = new GameStatus(token.getGameId());
            String playerName = "Pippo";
            Library library = deckRetrieverService.retrieveDeckForUser(token, playerName, gameStatus);
            gameStatus.setPlayer1(new Player(token.getSessionId(), playerName, library));
            gameStatusRepository.save(token.getGameId(), gameStatus);
            eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_WAITING_OPPONENT"));

        } else {
            GameStatus gameStatus = gameStatusRepository.getUnsecure(token.getGameId());
            if (gameStatus.getPlayer2() == null) {
                String playerName = "Pluto";
                Library library = deckRetrieverService.retrieveDeckForUser(token, playerName, gameStatus);
                gameStatus.setPlayer2(new Player(token.getSessionId(), playerName, library));

                gameStatus.getTurn().init(gameStatus.getPlayer1().getName());

                eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("OPPONENT_JOINED"));

                eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(gameStatus.getPlayer1())));
                eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getPlayer2())));

                eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(gameStatus.getPlayer2())));
                eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getPlayer1())));

                gameStatusUpdaterService.sendUpdateTurn(gameStatus);

            } else {
                eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("MESSAGE", new MessageEvent("Game is full.", true)));
            }
        }

        if (initTestService != null && gameStatusRepository.getUnsecure(token.getGameId()).getPlayer2() != null) {
            initTestService.initGameStatusForTest(gameStatusRepository.getUnsecure(token.getGameId()));
        }
    }

}
