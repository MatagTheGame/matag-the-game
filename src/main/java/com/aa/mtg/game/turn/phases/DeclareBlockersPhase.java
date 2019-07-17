package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.FirstStrikePhase.FS;

@Component
public class DeclareBlockersPhase implements Phase {
    public static final String DB = "DB";

    private final FirstStrikePhase firstStrikePhase;

    @Autowired
    public DeclareBlockersPhase(FirstStrikePhase firstStrikePhase) {
        this.firstStrikePhase = firstStrikePhase;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        gameStatus.getTurn().setCurrentPhase(FS);
        firstStrikePhase.apply(gameStatus);
    }
}
