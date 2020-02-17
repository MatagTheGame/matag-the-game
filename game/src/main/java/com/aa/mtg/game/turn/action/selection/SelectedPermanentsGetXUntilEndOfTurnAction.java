package com.aa.mtg.game.turn.action.selection;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.permanent.PermanentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SelectedPermanentsGetXUntilEndOfTurnAction implements AbilityAction {

    private final CardInstanceSelectorService cardInstanceSelectorService;
    private final PermanentService permanentService;

    public SelectedPermanentsGetXUntilEndOfTurnAction(CardInstanceSelectorService cardInstanceSelectorService, PermanentService permanentService) {
        this.cardInstanceSelectorService = cardInstanceSelectorService;
        this.permanentService = permanentService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, Ability ability) {
        List<CardInstance> cards = cardInstanceSelectorService.select(gameStatus, cardInstance, ability.getCardInstanceSelector()).getCards();
        for (CardInstance card : cards) {
            permanentService.thatPermanentGets(cardInstance, gameStatus, ability.getParameters(), card);
        }
    }

}
