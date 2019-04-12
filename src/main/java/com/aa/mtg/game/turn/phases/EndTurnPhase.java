package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.CleanupPhase.CL;

@Component
public class EndTurnPhase implements Phase {
    public static final String ET = "ET";

    @Override
    public void apply(GameStatus gameStatus) {
        if (gameStatus.getCurrentPlayer().getHand().size() > 7) {
            gameStatus.getTurn().setTriggeredAction("DISCARD_A_CARD");

        } else {
            if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

            } else {
                gameStatus.getTurn().setCurrentPhase(CL);
                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
            }
        }
    }
}
