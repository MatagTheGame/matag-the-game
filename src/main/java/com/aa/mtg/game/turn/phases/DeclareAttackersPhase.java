package com.aa.mtg.game.turn.phases;

import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.DeclareBlockersPhase.DB;
import static com.aa.mtg.game.turn.phases.FirstStrikePhase.FS;

@Component
public class DeclareAttackersPhase implements Phase {
    public static final String DA = "DA";

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final FirstStrikePhase firstStrikePhase;

    @Autowired
    public DeclareAttackersPhase(GameStatusUpdaterService gameStatusUpdaterService, FirstStrikePhase firstStrikePhase) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.firstStrikePhase = firstStrikePhase;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());
            gameStatusUpdaterService.sendUpdateTurn(gameStatus);

        } else {
            if (!gameStatus.getCurrentPlayer().getBattlefield().getAttackingCreatures().isEmpty() && new CardInstanceSearch(gameStatus.getNonCurrentPlayer().getBattlefield().getCards()).canAnyCreatureBlock()) {
                gameStatus.getTurn().setCurrentPhase(DB);
                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());
                gameStatusUpdaterService.sendUpdateTurn(gameStatus);

            } else {
                gameStatus.getTurn().setCurrentPhase(FS);
                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
                gameStatusUpdaterService.sendUpdateTurn(gameStatus);
                firstStrikePhase.apply(gameStatus);
            }
        }
    }
}
