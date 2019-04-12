package com.aa.mtg.game.turn.phases;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.Phase.M1;

@Component
public class DrawPhase {

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public DrawPhase(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    public void apply(GameStatus gameStatus) {
        if (gameStatus.getTurn().getTurnNumber() > 1) {
            CardInstance cardInstance = gameStatus.getCurrentPlayer().getLibrary().draw();
            gameStatus.getCurrentPlayer().getHand().addCard(cardInstance);
            gameStatusUpdaterService.sendUpdateCurrentPlayerHand(gameStatus);
        }
        gameStatus.getTurn().setCurrentPhase(M1);
    }
}
