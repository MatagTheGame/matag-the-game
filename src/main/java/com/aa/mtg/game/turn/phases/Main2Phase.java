package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.EndTurnPhase.ET;

@Component
public class Main2Phase implements Phase {
    public static final String M2 = "M2";

    private final EndTurnPhase endTurnPhase;

    @Autowired
    public Main2Phase(EndTurnPhase endTurnPhase) {
        this.endTurnPhase = endTurnPhase;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        gameStatus.getTurn().setCurrentPhase(ET);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        endTurnPhase.apply(gameStatus);
    }
}
