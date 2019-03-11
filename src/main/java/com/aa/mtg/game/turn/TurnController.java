package com.aa.mtg.game.turn;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusRepository;
import com.aa.mtg.game.security.SecurityToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static com.aa.mtg.game.security.SecurityHelper.extractSecurityToken;

@Controller
public class TurnController {
    private Logger LOGGER = LoggerFactory.getLogger(TurnController.class);

    private GameStatusRepository gameStatusRepository;
    private TurnService turnService;

    public TurnController(GameStatusRepository gameStatusRepository, TurnService turnService) {
        this.gameStatusRepository = gameStatusRepository;
        this.turnService = turnService;
    }

    @MessageMapping("/game/turn")
    public void turn(SimpMessageHeaderAccessor headerAccessor, TurnRequest request) {
        SecurityToken token = extractSecurityToken(headerAccessor);
        LOGGER.info("Turn request received for sessionId '{}', gameId '{}': {}", token.getSessionId(), token.getGameId(), request);
        GameStatus gameStatus = gameStatusRepository.get(token.getGameId(), token.getSessionId());

        if ("CONTINUE_TURN".equals(request.getAction())) {
            turnService.continueTurn(gameStatus);
        } else if ("PLAY_LAND".equals(request.getAction())) {
            turnService.playLand(gameStatus, request.getCardIds().get(0));
        } else if ("CAST".equals(request.getAction())) {
            turnService.cast(gameStatus, request.getCardIds().get(0), request.getTappingLandIds());
        } else if ("RESOLVE".equals(request.getAction())) {
            turnService.resolve(gameStatus, request.getTriggeredAction(), request.getCardIds().get(0));
        } else if ("DECLARE_ATTACKERS".equals(request.getAction())) {
            turnService.declareAttackers(gameStatus, request.getCardIds());
        }
    }

}
