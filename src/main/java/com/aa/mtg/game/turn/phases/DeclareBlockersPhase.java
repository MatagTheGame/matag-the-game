package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.AfterDeclareBlockersPhase.AB;

@Component
public class DeclareBlockersPhase implements Phase {
    public static final String DB = "DB";

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public DeclareBlockersPhase(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        gameStatus.getTurn().setCurrentPhase(AB);
        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }
}
