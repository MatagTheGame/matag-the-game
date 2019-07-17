package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.UpkeepPhase.UP;

@Component
public class UntapPhase implements Phase {
    public static final String UT = "UT";

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public UntapPhase(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        gameStatus.getCurrentPlayer().getBattlefield().untap();
        gameStatus.getCurrentPlayer().getBattlefield().removeSummoningSickness();

        gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
        gameStatus.getTurn().setCurrentPhase(UP);
        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }
}
