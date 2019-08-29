package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.turn.action.ability.DrawXCardsAction;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@Component
public class DrawPhase implements Phase {
    public static final String DR = "DR";

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final DrawXCardsAction drawXCardsAction;

    @Autowired
    public DrawPhase(GameStatusUpdaterService gameStatusUpdaterService, DrawXCardsAction drawXCardsAction) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.drawXCardsAction = drawXCardsAction;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        if (gameStatus.getTurn().getTurnNumber() > 1) {
            drawXCardsAction.perform(null, gameStatus, "1");
        }
        gameStatus.getTurn().setCurrentPhase(M1);
        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }
}
