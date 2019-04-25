package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.ability.type.AbilityType;
import org.springframework.stereotype.Component;

@Component
public class AbilityActionFactory {
    public AbilityAction getAbilityAction(AbilityType abilityType) {
        switch (abilityType) {
            case DESTROY_TARGET_CREATURE:
                return new DestroyTargetAction();
            default:
                return null;
        }
    }

}
