package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.FirstStrikePhase.FS;

@Component
public class AfterDeclareBlockersPhase implements Phase {
    public static final String AB = "AB";

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final FirstStrikePhase firstStrikePhase;

    @Autowired
    public AfterDeclareBlockersPhase(GameStatusUpdaterService gameStatusUpdaterService, FirstStrikePhase firstStrikePhase) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.firstStrikePhase = firstStrikePhase;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());
            gameStatusUpdaterService.sendUpdateTurn(gameStatus);

        } else {
            gameStatus.getTurn().setCurrentPhase(FS);
            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
            firstStrikePhase.apply(gameStatus);
        }
    }
}
