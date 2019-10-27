package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.AfterDeclareBlockersPhase.AB;

@Component
public class DeclareBlockersPhase implements Phase {
    public static final String DB = "DB";

    @Override
    public void apply(GameStatus gameStatus) {
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        gameStatus.getTurn().setCurrentPhase(AB);
    }
}
