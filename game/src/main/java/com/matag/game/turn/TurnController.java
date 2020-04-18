package com.matag.game.turn;

import com.matag.game.message.MessageEvent;
import com.matag.game.message.MessageException;
import com.matag.game.security.SecurityHelper;
import com.matag.game.security.SecurityToken;
import com.matag.game.status.GameStatus;
import com.matag.game.status.GameStatusRepository;
import com.matag.game.status.GameStatusUpdaterService;
import com.matag.game.turn.action._continue.ConsolidateStatusService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class TurnController {
  private static final Logger LOGGER = LoggerFactory.getLogger(TurnController.class);

  private final SecurityHelper securityHelper;
  private final GameStatusRepository gameStatusRepository;
  private final GameStatusUpdaterService gameStatusUpdaterService;
  private final TurnService turnService;
  private final ConsolidateStatusService consolidateStatusService;

  @MessageMapping("/game/turn")
  public void turn(SimpMessageHeaderAccessor headerAccessor, TurnRequest request) {
    SecurityToken token = securityHelper.extractSecurityToken(headerAccessor);
    LOGGER.info("Turn request received for sessionId '{}', gameId '{}': {}", token.getSessionId(), token.getGameId(), request);
    GameStatus gameStatus = gameStatusRepository.get(token.getGameId(), token.getSessionId());
    if (gameStatus.getTurn().isEnded()) {
      throw new RuntimeException("Game is ended, no more actions are permitted.");
    }

    if ("CONTINUE_TURN".equals(request.getAction())) {
      turnService.continueTurn(gameStatus);
    } else if ("PLAY_LAND".equals(request.getAction())) {
      turnService.playLand(gameStatus, request.getCardIds().get(0));
    } else if ("CAST".equals(request.getAction())) {
      turnService.cast(gameStatus, request.getCardIds().get(0), request.getMana(), request.getTargetsIdsForCardIds(), request.getPlayedAbility());
    } else if ("RESOLVE".equals(request.getAction())) {
      turnService.resolve(gameStatus, request.getTriggeredNonStackAction(), request.getCardIds(), request.getTargetsIdsForCardIds());
    } else if ("DECLARE_ATTACKERS".equals(request.getAction())) {
      turnService.declareAttackers(gameStatus, request.getCardIds());
    } else if ("DECLARE_BLOCKERS".equals(request.getAction())) {
      turnService.declareBlockers(gameStatus, toMapListInteger(request.getTargetsIdsForCardIds()));
    }

    consolidateStatusService.consolidate(gameStatus);
    gameStatusUpdaterService.sendUpdateGameStatus(gameStatus);
  }

  @MessageExceptionHandler
  public void handleException(SimpMessageHeaderAccessor headerAccessor, MessageException e) {
    gameStatusUpdaterService.sendMessage(headerAccessor.getSessionId(), new MessageEvent(e.getMessage(), true));
  }

  public static Map<Integer, List<Integer>> toMapListInteger(Map<Integer, List<Object>> mapListObject) {
    return mapListObject.entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, entry -> toListInteger(entry.getValue())));
  }

  private static List<Integer> toListInteger(List<Object> listObject) {
    return listObject.stream().map((item) -> (int) item).collect(Collectors.toList());
  }
}
