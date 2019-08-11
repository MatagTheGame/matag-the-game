package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtherCreaturesYouControlGetXUntilEndOfTurnAction implements AbilityAction {

    private CreaturesYouControlGetXUntilEndOfTurnAction creaturesYouControlGetXUntilEndOfTurnAction;

    @Autowired
    public OtherCreaturesYouControlGetXUntilEndOfTurnAction(CreaturesYouControlGetXUntilEndOfTurnAction creaturesYouControlGetXUntilEndOfTurnAction) {
        this.creaturesYouControlGetXUntilEndOfTurnAction = creaturesYouControlGetXUntilEndOfTurnAction;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        creaturesYouControlGetXUntilEndOfTurnAction.creaturesGet(cardInstance, gameStatus, parameter, false);
    }
}
