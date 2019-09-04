package com.aa.mtg.game.turn.action.selection;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.permanent.PermanentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aa.mtg.cards.properties.Type.CREATURE;

@Component
public class OtherCreaturesYouControlGetXUntilEndOfTurnAction implements AbilityAction {

    private final PermanentService permanentService;

    @Autowired
    public OtherCreaturesYouControlGetXUntilEndOfTurnAction(PermanentService permanentService) {
        this.permanentService = permanentService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        Player controller = gameStatus.getPlayerByName(cardInstance.getController());

        List<CardInstance> cards = new CardInstanceSearch(controller.getBattlefield().getCards()).ofType(CREATURE).getCards();
        for (CardInstance card : cards) {
            if (card.getId() != cardInstance.getId()) {
                permanentService.thatPermanentGets(cardInstance, gameStatus, parameter, card);
            }
        }
    }
}
