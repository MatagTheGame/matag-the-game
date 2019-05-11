package com.aa.mtg.game.turn.phases;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.action.DrawXCardsAction;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.cards.ability.type.AbilityType.DRAW_X_CARDS;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Component
public class DrawPhase implements Phase {
    public static final String DR = "DR";

    private final DrawXCardsAction drawXCardsAction;

    @Autowired
    public DrawPhase(DrawXCardsAction drawXCardsAction) {
        this.drawXCardsAction = drawXCardsAction;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        if (gameStatus.getTurn().getTurnNumber() > 1) {
            drawXCardsAction.perform(new Ability(DRAW_X_CARDS, emptyList(), singletonList("1")), null, gameStatus);
        }
        gameStatus.getTurn().setCurrentPhase(M1);
    }
}
