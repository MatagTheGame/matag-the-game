package com.aa.mtg.game.turn;

import com.aa.mtg.game.message.MessageEvent;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.security.SecurityHelper;
import com.aa.mtg.game.security.SecurityToken;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusRepository;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static com.aa.mtg.utils.Utils.toMapListInteger;

@Controller
public class TurnController {
    private Logger LOGGER = LoggerFactory.getLogger(TurnController.class);

    private final SecurityHelper securityHelper;
    private final GameStatusRepository gameStatusRepository;
    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final TurnService turnService;

    public TurnController(SecurityHelper securityHelper, GameStatusRepository gameStatusRepository, GameStatusUpdaterService gameStatusUpdaterService, TurnService turnService) {
        this.securityHelper = securityHelper;
        this.gameStatusRepository = gameStatusRepository;
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.turnService = turnService;
    }

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

        gameStatusUpdaterService.sendUpdateBattlefields(gameStatus);
        gameStatusUpdaterService.sendUpdateGraveyards(gameStatus);
        gameStatusUpdaterService.sendUpdateStack(gameStatus);
        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }

    @MessageExceptionHandler
    public void handleException(SimpMessageHeaderAccessor headerAccessor, MessageException e) {
        gameStatusUpdaterService.sendMessage(headerAccessor.getSessionId(), new MessageEvent(e.getMessage(), true));
    }
}
