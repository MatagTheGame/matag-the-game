package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.ability.type.AbilityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AbilityActionFactory {
    private final DestroyTargetAction destroyTargetAction;
    private final DealXDamageToTargetAction dealXDamageToTargetAction;

    @Autowired
    public AbilityActionFactory(DestroyTargetAction destroyTargetAction, DealXDamageToTargetAction dealXDamageToTargetAction) {
        this.destroyTargetAction = destroyTargetAction;
        this.dealXDamageToTargetAction = dealXDamageToTargetAction;
    }

    public AbilityAction getAbilityAction(AbilityType abilityType) {
        switch (abilityType) {
            case DESTROY_TARGET_CREATURE:
                return destroyTargetAction;
            case DEALS_X_DAMAGE_TO_TARGET:
                return dealXDamageToTargetAction;
            default:
                return null;
        }
    }

}
