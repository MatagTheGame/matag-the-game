package com.matag.game.init;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.deck.DeckFactory;
import com.matag.game.deck.DeckInfo;
import com.matag.game.deck.DeckRetrieverService;
import com.matag.game.event.Event;
import com.matag.game.event.EventSender;
import com.matag.game.init.test.InitTestService;
import com.matag.game.player.Player;
import com.matag.game.player.PlayerFactory;
import com.matag.game.player.PlayerService;
import com.matag.game.player.playerInfo.PlayerInfo;
import com.matag.game.player.playerInfo.PlayerInfoRetriever;
import com.matag.game.security.SecurityHelper;
import com.matag.game.security.SecurityToken;
import com.matag.game.status.GameStatus;
import com.matag.game.status.GameStatusFactory;
import com.matag.game.status.GameStatusRepository;
import com.matag.game.status.GameStatusUpdaterService;
import com.matag.game.turn.action._continue.ContinueTurnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class InitController {
  private static final Logger LOGGER = LoggerFactory.getLogger(InitController.class);

  private final SecurityHelper securityHelper;
  private final EventSender eventSender;
  private final GameStatusFactory gameStatusFactory;
  private final PlayerInfoRetriever playerInfoRetriever;
  private final PlayerFactory playerFactory;
  private final PlayerService playerService;
  private final GameStatusUpdaterService gameStatusUpdaterService;
  private final InitTestService initTestService;
  private final GameStatusRepository gameStatusRepository;
  private final DeckRetrieverService deckRetrieverService;
  private final DeckFactory deckFactory;
  private final ContinueTurnService continueTurnService;

  public InitController(SecurityHelper securityHelper, EventSender eventSender, GameStatusFactory gameStatusFactory, PlayerInfoRetriever playerInfoRetriever, PlayerFactory playerFactory, PlayerService playerService, GameStatusUpdaterService gameStatusUpdaterService, GameStatusRepository gameStatusRepository, DeckRetrieverService deckRetrieverService, @Autowired(required = false) InitTestService initTestService, DeckFactory deckFactory, ContinueTurnService continueTurnService) {
    this.securityHelper = securityHelper;
    this.eventSender = eventSender;
    this.gameStatusFactory = gameStatusFactory;
    this.playerInfoRetriever = playerInfoRetriever;
    this.playerFactory = playerFactory;
    this.playerService = playerService;
    this.gameStatusUpdaterService = gameStatusUpdaterService;
    this.gameStatusRepository = gameStatusRepository;
    this.deckRetrieverService = deckRetrieverService;
    this.initTestService = initTestService;
    this.deckFactory = deckFactory;
    this.continueTurnService = continueTurnService;
  }

  @MessageMapping("/game/init")
  void init(SimpMessageHeaderAccessor headerAccessor) {
    SecurityToken token = securityHelper.extractSecurityToken(headerAccessor);
    LOGGER.info("Init request received for sessionId '{}', gameId '{}'.", token.getSessionId(), token.getGameId());

    if (!gameStatusRepository.contains(token.getGameId())) {
      GameStatus gameStatus = gameStatusFactory.create(token.getGameId());
      gameStatus.setPlayer1(retrievePlayer(token));
      gameStatus.getPlayer1().getLibrary().addCards(retrieveDeck(token, gameStatus));
      gameStatus.getPlayer1().drawHand();
      gameStatusRepository.save(token.getGameId(), gameStatus);
      eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_WAITING_OPPONENT"));

    } else {
      GameStatus gameStatus = gameStatusRepository.getUnsecure(token.getGameId());
      if (gameStatus.getPlayer2() == null && !gameStatus.getPlayer1().getToken().getSessionId().equals(token.getSessionId())) {
        gameStatus.setPlayer2(retrievePlayer(token));
        gameStatus.getPlayer2().getLibrary().addCards(retrieveDeck(token, gameStatus));
        gameStatus.getPlayer2().drawHand();

        gameStatus.getTurn().init(gameStatus.getPlayer1().getName());

        eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("OPPONENT_JOINED"));

        if (initTestService != null && gameStatusRepository.getUnsecure(token.getGameId()).getPlayer2() != null) {
          initTestService.initGameStatusForTest(gameStatusRepository.getUnsecure(token.getGameId()));
        }

        eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(gameStatus.getPlayer1())));
        eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getPlayer2())));

        eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(gameStatus.getPlayer2())));
        eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getPlayer1())));

        // FIXME no implemented card yet can be played at the first game upkeep... so let's just continue.
        continueTurnService.continueTurn(gameStatus);

        gameStatusUpdaterService.sendUpdateTurn(gameStatus);

      } else {
        Player player = playerService.getPlayerByToken(gameStatus, token.getAdminToken());
        player.setToken(token);

        eventSender.sendToPlayer(player, new Event("INIT_PLAYER", InitPlayerEvent.createForPlayer(player)));
        eventSender.sendToPlayer(player, new Event("INIT_OPPONENT", InitPlayerEvent.createForOpponent(gameStatus.getOtherPlayer(player))));

        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
      }
    }
  }

  private Player retrievePlayer(SecurityToken token) {
    PlayerInfo playerInfo = playerInfoRetriever.retrieve(token);
    return playerFactory.create(token, playerInfo);
  }

  private List<CardInstance> retrieveDeck(SecurityToken token, GameStatus gameStatus) {
    DeckInfo deckInfo = deckRetrieverService.retrieveDeckForUser(token);
    return deckFactory.create(retrievePlayer(token).getName(), gameStatus, deckInfo);
  }
}
