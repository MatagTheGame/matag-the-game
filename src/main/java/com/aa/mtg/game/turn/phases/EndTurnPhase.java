package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.Phase.CL;

@Component
public class EndTurnPhase {
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
