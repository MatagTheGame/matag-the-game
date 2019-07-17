package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.CleanupPhase.CL;

@Component
public class EndTurnPhase implements Phase {
    public static final String ET = "ET";

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final CleanupPhase cleanupPhase;

    @Autowired
    public EndTurnPhase(GameStatusUpdaterService gameStatusUpdaterService, CleanupPhase cleanupPhase) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.cleanupPhase = cleanupPhase;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        if (gameStatus.getCurrentPlayer().getHand().size() > 7) {
            gameStatus.getTurn().setTriggeredNonStackAction("DISCARD_A_CARD");
            gameStatusUpdaterService.sendUpdateTurn(gameStatus);

        } else {
            if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());
                gameStatusUpdaterService.sendUpdateTurn(gameStatus);

            } else {
                gameStatus.getTurn().setCurrentPhase(CL);
                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
                cleanupPhase.apply(gameStatus);
            }
        }
    }
}
