package com.matag.game.init;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.deck.DeckFactory;
import com.matag.game.deck.DeckRetrieverService;
import com.matag.game.event.Event;
import com.matag.game.event.EventSender;
import com.matag.game.init.test.InitTestService;
import com.matag.game.player.Player;
import com.matag.game.player.PlayerFactory;
import com.matag.game.player.PlayerService;
import com.matag.game.player.playerInfo.PlayerInfoRetriever;
import com.matag.game.security.SecurityHelper;
import com.matag.game.security.SecurityToken;
import com.matag.game.status.GameStatus;
import com.matag.game.status.GameStatusFactory;
import com.matag.game.status.GameStatusRepository;
import com.matag.game.status.GameStatusUpdaterService;
import com.matag.game.turn.action._continue.ContinueService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

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
  private final ContinueService continueService;

  public InitController(SecurityHelper securityHelper, EventSender eventSender, GameStatusFactory gameStatusFactory, PlayerInfoRetriever playerInfoRetriever, PlayerFactory playerFactory, PlayerService playerService, GameStatusUpdaterService gameStatusUpdaterService, GameStatusRepository gameStatusRepository, DeckRetrieverService deckRetrieverService, @Autowired(required = false) InitTestService initTestService, DeckFactory deckFactory, ContinueService continueService) {
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
    this.continueService = continueService;
  }

  @MessageMapping("/game/init")
  void init(SimpMessageHeaderAccessor headerAccessor) {
    var token = securityHelper.extractSecurityToken(headerAccessor);
    LOGGER.info("Init request received for sessionId '{}', gameId '{}'.", token.getSessionId(), token.getGameId());

    if (!gameStatusRepository.contains(token.getGameId())) {
      var gameStatus = gameStatusFactory.create(token.getGameId());
      var player1 = retrievePlayer(token);
      gameStatus.setPlayer1(player1);
      gameStatus.getPlayer1().getLibrary().addCards(retrieveDeck(player1, gameStatus));
      gameStatus.getPlayer1().drawHand();
      gameStatusRepository.save(token.getGameId(), gameStatus);
      eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_WAITING_OPPONENT"));

    } else {
      var gameStatus = gameStatusRepository.getUnsecure(token.getGameId());
      if (gameStatus.getPlayer2() == null && !gameStatus.getPlayer1().getToken().getSessionId().equals(token.getSessionId())) {
        var player2 = retrievePlayer(token);
        gameStatus.setPlayer2(player2);
        gameStatus.getPlayer2().getLibrary().addCards(retrieveDeck(player2, gameStatus));
        gameStatus.getPlayer2().drawHand();

        gameStatus.getTurn().init(gameStatus.getPlayer1().getName());

        eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("OPPONENT_JOINED"));

        if (initTestService != null && gameStatusRepository.getUnsecure(token.getGameId()).getPlayer2() != null) {
          initTestService.initGameStatusForTest(gameStatusRepository.getUnsecure(token.getGameId()));
        }

        eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_PLAYER_AND_OPPONENT", initPlayerAndOpponentEvent(gameStatus.getPlayer1(), gameStatus.getPlayer2())));
        eventSender.sendToPlayer(gameStatus.getPlayer2(), new Event("INIT_PLAYER_AND_OPPONENT", initPlayerAndOpponentEvent(gameStatus.getPlayer2(), gameStatus.getPlayer1())));

        // FIXME no implemented card yet can be played at the first game upkeep... so let's just continue.
        continueService.next(gameStatus);

        addDelayBeforeSendingUpdateGameStatusEvent();
        gameStatusUpdaterService.sendUpdateGameStatus(gameStatus);

      } else {
        var player = playerService.getPlayerByToken(gameStatus, token.getAdminToken());
        player.setToken(token);

        eventSender.sendToPlayer(gameStatus.getPlayer1(), new Event("INIT_PLAYER_AND_OPPONENT", initPlayerAndOpponentEvent(player, gameStatus.getOtherPlayer(player))));

        gameStatusUpdaterService.sendUpdateGameStatus(gameStatus);
      }
    }
  }

  private Player retrievePlayer(SecurityToken token) {
    var playerInfo = playerInfoRetriever.retrieve(token);
    return playerFactory.create(token, playerInfo);
  }

  private List<CardInstance> retrieveDeck(Player player, GameStatus gameStatus) {
    var deckInfo = deckRetrieverService.retrieveDeckForUser(player.getToken());
    return deckFactory.create(player.getName(), gameStatus, deckInfo);
  }

  @SneakyThrows
  private void addDelayBeforeSendingUpdateGameStatusEvent() {
    Thread.sleep(100);
  }

  private static Map<String, InitPlayerEvent> initPlayerAndOpponentEvent(Player player, Player opponent) {
    return Map.of(
            "player", InitPlayerEvent.create(player),
            "opponent", InitPlayerEvent.create(opponent));
  }
}
