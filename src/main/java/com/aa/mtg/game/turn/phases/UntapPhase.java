package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.UpkeepPhase.UP;

@Component
public class UntapPhase implements Phase {
    public static final String UT = "UT";

    @Override
    public void apply(GameStatus gameStatus) {
        gameStatus.getCurrentPlayer().getBattlefield().untap();
        gameStatus.getCurrentPlayer().getBattlefield().removeSummoningSickness();
        gameStatus.getTurn().setCurrentPhase(UP);
    }
}
