package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.ability.type.AbilityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AbilityActionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbilityActionFactory.class);

    private final DestroyTargetAction destroyTargetAction;
    private final DealXDamageToTargetAction dealXDamageToTargetAction;
    private final DrawXCardsAction drawXCardsAction;
    private final ShuffleTargetGraveyardIntoLibraryAction shuffleTargetGraveyardIntoLibraryAction;
    private final ThatTargetsGetAction thatTargetsGetAction;

    @Autowired
    public AbilityActionFactory(DestroyTargetAction destroyTargetAction, DealXDamageToTargetAction dealXDamageToTargetAction, DrawXCardsAction drawXCardsAction,
                                ShuffleTargetGraveyardIntoLibraryAction shuffleTargetGraveyardIntoLibraryAction, ThatTargetsGetAction thatTargetsGetAction) {
        this.destroyTargetAction = destroyTargetAction;
        this.dealXDamageToTargetAction = dealXDamageToTargetAction;
        this.drawXCardsAction = drawXCardsAction;
        this.shuffleTargetGraveyardIntoLibraryAction = shuffleTargetGraveyardIntoLibraryAction;
        this.thatTargetsGetAction = thatTargetsGetAction;
    }

    public AbilityAction getAbilityAction(AbilityType abilityType) {
        if (abilityType.isStatic()) {
            return null;
        }

        switch (abilityType) {
            case DESTROY_TARGET:
                return destroyTargetAction;
            case DEALS_X_DAMAGE_TO_TARGET:
                return dealXDamageToTargetAction;
            case DRAW_X_CARDS:
                return drawXCardsAction;
            case SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER:
                return shuffleTargetGraveyardIntoLibraryAction;
            case THAT_TARGETS_GET_X:
                return thatTargetsGetAction;
            default:
                LOGGER.error("Ability action {} not found. Not performing anything.", abilityType);
                return null;
        }
    }

}
