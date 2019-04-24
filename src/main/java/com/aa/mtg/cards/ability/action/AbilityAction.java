package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.game.status.GameStatus;

import java.util.List;

public abstract class AbilityAction {

    private final List<Target> targets;

    public AbilityAction(List<Target> targets) {
        this.targets = targets;
    }

    public void check(CardInstance cardInstance, GameStatus gameStatus, List<Integer> targetCardIds) {

    }

    public abstract void perform();

    public List<Target> getTargets() {
        return targets;
    }
}
