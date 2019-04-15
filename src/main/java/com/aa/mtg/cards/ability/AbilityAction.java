package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;

import java.util.List;

public abstract class AbilityAction {

    private final List<String> parameters;

    public AbilityAction(List<String> parameters) {
        this.parameters = parameters;
    }

    public abstract void check(CardInstance cardInstance, GameStatus gameStatus);

    public List<String> getParameters() {
        return parameters;
    }
}
