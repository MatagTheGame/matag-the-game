package com.aa.mtg.game.turn.action.selection;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.target.ThatTargetsGetAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aa.mtg.cards.properties.Type.CREATURE;

@Component
public class OtherCreaturesGetXUntilEndOfTurnAction implements AbilityAction {

    private final ThatTargetsGetAction thatTargetsGetAction;

    @Autowired
    public OtherCreaturesGetXUntilEndOfTurnAction(ThatTargetsGetAction thatTargetsGetAction) {
        this.thatTargetsGetAction = thatTargetsGetAction;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        List<CardInstance> cards = gameStatus.getAllBattlefieldCards().ofType(CREATURE).getCards();
        for (CardInstance card : cards) {
            if (card.getId() != cardInstance.getId()) {
                thatTargetsGetAction.thatTargetPermanentGet(cardInstance, gameStatus, parameter, card);
            }
        }
    }
}
