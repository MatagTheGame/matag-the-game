package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.DeclareBlockersPhase.DB;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;

@Component
public class DeclareAttackersPhase implements Phase {
    public static final String DA = "DA";

    @Override
    public void apply(GameStatus gameStatus) {
        if (!gameStatus.getCurrentPlayer().getBattlefield().getAttackingCreatures().isEmpty()) {
            gameStatus.getTurn().setCurrentPhase(DB);
            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());
        } else {
            gameStatus.getTurn().setCurrentPhase(M2);
        }
    }
}
