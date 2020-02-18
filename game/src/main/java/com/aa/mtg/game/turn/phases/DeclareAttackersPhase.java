package com.aa.mtg.game.turn.phases;

import com.aa.mtg.cardinstance.CardInstanceSearch;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.DeclareBlockersPhase.DB;
import static com.aa.mtg.game.turn.phases.FirstStrikePhase.FS;

@Component
public class DeclareAttackersPhase implements Phase {
    public static final String DA = "DA";

    private final FirstStrikePhase firstStrikePhase;

    @Autowired
    public DeclareAttackersPhase(FirstStrikePhase firstStrikePhase) {
        this.firstStrikePhase = firstStrikePhase;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

        } else {
            if (!gameStatus.getCurrentPlayer().getBattlefield().getAttackingCreatures().isEmpty() && new CardInstanceSearch(gameStatus.getNonCurrentPlayer().getBattlefield().getCards()).canAnyCreatureBlock()) {
                gameStatus.getTurn().setCurrentPhase(DB);
                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

            } else {
                gameStatus.getTurn().setCurrentPhase(FS);
                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
                firstStrikePhase.apply(gameStatus);
            }
        }
    }
}
