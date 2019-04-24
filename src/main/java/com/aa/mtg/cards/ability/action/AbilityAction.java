package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.game.status.GameStatus;

public abstract class AbilityAction {

    private final Target target;

    public AbilityAction(Target target) {
        this.target = target;
    }

    public void check(CardInstance cardInstance, GameStatus gameStatus, Integer targetCardId) {

    }

    public abstract void perform();

    public Target getTarget() {
        return target;
    }
}
