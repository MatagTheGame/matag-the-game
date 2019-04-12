package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class CleanupPhase {
    public void apply(GameStatus gameStatus) {
        gameStatus.getTurn().increaseTurnNumber();
        gameStatus.getTurn().setCurrentPhase(Phase.UT);
        String nextCurrentPlayer = gameStatus.getNonCurrentPlayer().getName();
        gameStatus.getTurn().setCurrentTurnPlayer(nextCurrentPlayer);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(nextCurrentPlayer);
        gameStatus.getTurn().getCardsPlayedWithinTurn().clear();
    }
}
