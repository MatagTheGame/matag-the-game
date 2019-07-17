package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.Main2Phase.M2;

@Component
public class EndOfCombatPhase implements Phase {
    public static final String EC = "EC";

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public EndOfCombatPhase(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        gameStatus.getTurn().setCurrentPhase(M2);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        gameStatusUpdaterService.sendUpdateTurn(gameStatus);

        gameStatus.getCurrentPlayer().getBattlefield().removeAttacking();
        gameStatus.getNonCurrentPlayer().getBattlefield().removeBlocking();
        gameStatusUpdaterService.sendUpdateBattlefields(gameStatus);
    }
}
