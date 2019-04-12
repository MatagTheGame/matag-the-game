package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.Phase.ET;

@Component
public class Main2Phase {
    public void apply(GameStatus gameStatus) {
        if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

        } else {
            gameStatus.getTurn().setCurrentPhase(ET);
            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        }
    }
}
