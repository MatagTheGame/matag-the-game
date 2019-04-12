package com.aa.mtg.game.turn.action;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.phases.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContinueTurnService {

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final PhaseFactory phaseFactory;

    @Autowired
    public ContinueTurnService(GameStatusUpdaterService gameStatusUpdaterService, PhaseFactory phaseFactory) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.phaseFactory = phaseFactory;
    }

    public void continueTurn(GameStatus gameStatus) {
        Phase phase = phaseFactory.get(gameStatus.getTurn().getCurrentPhase());
        phase.apply(gameStatus);
        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }
}
