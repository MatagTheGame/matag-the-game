package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.ability.type.AbilityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AbilityActionFactory {
    private final DestroyTargetAction destroyTargetAction;
    private final DealXDamageToTargetAction dealXDamageToTargetAction;
    private final DrawXCardsAction drawXCardsAction;
    private final ShuffleTargetGraveyardIntoLibraryAction shuffleTargetGraveyardIntoLibraryAction;
    private final ThatTargetsGetAction thatTargetsGetAction;
    private final CreaturesYouControlGetPlusXXUntilEndOfTurn creaturesYouControlGetPlusXXUntilEndOfTurn;
    private final Attach attach;

    @Autowired
    public AbilityActionFactory(DestroyTargetAction destroyTargetAction, DealXDamageToTargetAction dealXDamageToTargetAction, DrawXCardsAction drawXCardsAction,
                                ShuffleTargetGraveyardIntoLibraryAction shuffleTargetGraveyardIntoLibraryAction, ThatTargetsGetAction thatTargetsGetAction,
                                CreaturesYouControlGetPlusXXUntilEndOfTurn creaturesYouControlGetPlusXXUntilEndOfTurn, Attach attach) {
        this.destroyTargetAction = destroyTargetAction;
        this.dealXDamageToTargetAction = dealXDamageToTargetAction;
        this.drawXCardsAction = drawXCardsAction;
        this.shuffleTargetGraveyardIntoLibraryAction = shuffleTargetGraveyardIntoLibraryAction;
        this.thatTargetsGetAction = thatTargetsGetAction;
        this.creaturesYouControlGetPlusXXUntilEndOfTurn = creaturesYouControlGetPlusXXUntilEndOfTurn;
        this.attach = attach;
    }

    public AbilityAction getAbilityAction(AbilityType abilityType) {
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
            case CREATURES_YOU_CONTROL_GET_PLUS_X_UNTIL_END_OF_TURN:
                return creaturesYouControlGetPlusXXUntilEndOfTurn;
            case ENCHANTED_CREATURE_GETS:
            case ENCHANTED_CREATURE_GETS_AND:
                return attach;
            default:
                return null;
        }
    }

}
