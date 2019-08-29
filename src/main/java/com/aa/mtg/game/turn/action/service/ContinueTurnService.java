package com.aa.mtg.game.turn.action.service;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.phases.Phase;
import com.aa.mtg.game.turn.phases.PhaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContinueTurnService {

    private final PhaseFactory phaseFactory;

    @Autowired
    public ContinueTurnService(PhaseFactory phaseFactory) {
        this.phaseFactory = phaseFactory;
    }

    public void continueTurn(GameStatus gameStatus) {
        Phase phase = phaseFactory.get(gameStatus.getTurn().getCurrentPhase());
        phase.apply(gameStatus);
    }
}
