package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.Phase.FS;

@Component
public class DeclareBlockersPhase {
    public void apply(GameStatus gameStatus) {
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        gameStatus.getTurn().setCurrentPhase(FS);
    }
}
