package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.ability.target.Target;

import java.util.List;

public class DestroyTargetAction extends AbilityAction {
    public DestroyTargetAction(List<Target> targets) {
        super(targets);
    }

    @Override
    public void perform() {

    }
}
