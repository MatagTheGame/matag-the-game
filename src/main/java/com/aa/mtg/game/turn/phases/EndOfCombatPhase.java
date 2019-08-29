package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.Main2Phase.M2;

@Component
public class EndOfCombatPhase implements Phase {
    public static final String EC = "EC";

    @Override
    public void apply(GameStatus gameStatus) {
        gameStatus.getTurn().setCurrentPhase(M2);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());

        gameStatus.getCurrentPlayer().getBattlefield().removeAttacking();
        gameStatus.getNonCurrentPlayer().getBattlefield().removeBlocking();
    }
}
