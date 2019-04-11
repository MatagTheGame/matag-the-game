package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResolveService {

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private ContinueTurnService continueTurnService;

    @Autowired
    public ResolveService(GameStatusUpdaterService gameStatusUpdaterService, ContinueTurnService continueTurnService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.continueTurnService = continueTurnService;
    }

    public void resolve(GameStatus gameStatus, String triggeredAction, List<Integer> cardIds) {
        if (!gameStatus.getStack().isEmpty()) {
            CardInstance cardInstance = gameStatus.getStack().removeLast();
            gameStatusUpdaterService.sendUpdateStack(gameStatus);

            cardInstance.getModifiers().setSummoningSickness(true);
            gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);
            gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);

            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
            gameStatusUpdaterService.sendUpdateTurn(gameStatus);

        } else if (gameStatus.getTurn().getTriggeredAction().equals(triggeredAction)) {
            switch (triggeredAction) {
                case "DISCARD_A_CARD": {
                    CardInstance cardInstance = gameStatus.getCurrentPlayer().getHand().extractCardById(cardIds.get(0));
                    gameStatus.getCurrentPlayer().getGraveyard().addCard(cardInstance);
                    gameStatusUpdaterService.sendUpdateCurrentPlayerHand(gameStatus);
                    gameStatusUpdaterService.sendUpdateCurrentPlayerGraveyard(gameStatus);
                    gameStatus.getTurn().setTriggeredAction(null);
                    break;
                }
            }
            continueTurnService.continueTurn(gameStatus);

        } else {
            String message = "Cannot resolve triggeredAction " + triggeredAction + " as current triggeredAction is " + gameStatus.getTurn().getTriggeredAction();
            throw new MessageException(message);
        }
    }
}
