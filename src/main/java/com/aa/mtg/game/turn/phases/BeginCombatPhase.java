package com.aa.mtg.game.turn.phases;

import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;

@Component
public class BeginCombatPhase implements Phase {
    public static final String BC = "BC";

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public BeginCombatPhase(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
            if (new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards()).canAnyCreatureAttack()) {
                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());
                gameStatusUpdaterService.sendUpdateTurn(gameStatus);

            } else {
                gameStatus.getTurn().setCurrentPhase(M2);
                gameStatusUpdaterService.sendUpdateTurn(gameStatus);
            }

        } else {
            gameStatus.getTurn().setCurrentPhase(DA);
            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
            gameStatusUpdaterService.sendUpdateTurn(gameStatus);
        }
    }
}
