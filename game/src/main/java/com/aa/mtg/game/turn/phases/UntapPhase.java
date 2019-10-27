package com.aa.mtg.game.turn.phases;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.tap.TapPermanentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aa.mtg.game.turn.phases.UpkeepPhase.UP;

@Component
public class UntapPhase implements Phase {
    public static final String UT = "UT";

    private final TapPermanentService tapPermanentService;

    @Autowired
    public UntapPhase(TapPermanentService tapPermanentService) {
        this.tapPermanentService = tapPermanentService;
    }

    @Override
    public void apply(GameStatus gameStatus) {
        List<CardInstance> cards = gameStatus.getCurrentPlayer().getBattlefield().getCards();

        for (CardInstance cardInstance : new CardInstanceSearch(cards).tapped().getCards()) {
            if (cardInstance.getModifiers().isDoesNotUntapNextTurn()) {
                cardInstance.getModifiers().doesNotUntapNextTurn(false);
            } else {
                tapPermanentService.untap(gameStatus, cardInstance.getId());
                cardInstance.getModifiers().untap();
            }
        }

        new CardInstanceSearch(cards).withSummoningSickness().getCards()
                .forEach(cardInstance -> cardInstance.getModifiers().setSummoningSickness(false));

        gameStatus.getTurn().setCurrentPhase(UP);
    }
}
