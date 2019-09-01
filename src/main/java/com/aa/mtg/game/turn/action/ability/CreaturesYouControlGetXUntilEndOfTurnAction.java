package com.aa.mtg.game.turn.action.ability;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aa.mtg.cards.properties.Type.CREATURE;

@Component
public class CreaturesYouControlGetXUntilEndOfTurnAction implements AbilityAction {
    private final ThatTargetsGetAction thatTargetsGetAction;

    public CreaturesYouControlGetXUntilEndOfTurnAction(ThatTargetsGetAction thatTargetsGetAction) {
        this.thatTargetsGetAction = thatTargetsGetAction;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        Player controller = gameStatus.getPlayerByName(cardInstance.getController());

        List<CardInstance> cards = new CardInstanceSearch(controller.getBattlefield().getCards()).ofType(CREATURE).getCards();
        for (CardInstance card : cards) {
            thatTargetsGetAction.thatTargetPermanentGet(cardInstance, gameStatus, parameter, card);
        }
    }

}
