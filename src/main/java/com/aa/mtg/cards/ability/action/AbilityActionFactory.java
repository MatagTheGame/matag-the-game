package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.ability.type.AbilityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AbilityActionFactory {
    private final DestroyTargetAction destroyTargetAction;

    @Autowired
    public AbilityActionFactory(DestroyTargetAction destroyTargetAction) {
        this.destroyTargetAction = destroyTargetAction;
    }

    public AbilityAction getAbilityAction(AbilityType abilityType) {
        switch (abilityType) {
            case DESTROY_TARGET_CREATURE:
                return destroyTargetAction;
            default:
                return null;
        }
    }

}
