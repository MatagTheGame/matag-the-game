package com.aa.mtg.game.turn.phases;

import com.aa.mtg.cardinstance.CardInstanceSearch;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;

@Component
public class BeginCombatPhase implements Phase {
    public static final String BC = "BC";

    @Override
    public void apply(GameStatus gameStatus) {
        if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
            if (new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards()).canAnyCreatureAttack()) {
                gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

            } else {
                gameStatus.getTurn().setCurrentPhase(M2);
            }

        } else {
            gameStatus.getTurn().setCurrentPhase(DA);
            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        }
    }
}
