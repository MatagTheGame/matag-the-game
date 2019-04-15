package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;

import java.util.List;

public class DestroyTargetCreatureAction extends AbilityAction {
    public DestroyTargetCreatureAction(List<String> parameters) {
        super(parameters);
    }

    @Override
    public void check(CardInstance cardInstance, GameStatus gameStatus) {

    }
}
