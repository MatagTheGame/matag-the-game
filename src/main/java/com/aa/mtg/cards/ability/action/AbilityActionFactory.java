package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.ability.type.AbilityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AbilityActionFactory {
    private final ThatTargetsGetAction thatTargetsGetAction;
    private final DrawXCardsAction drawXCardsAction;
    private final GainXLifeAction gainXLifeAction;
    private final EachPlayersGainXLifeAction eachPlayersGainXLifeAction;
    private final ShuffleTargetGraveyardIntoLibraryAction shuffleTargetGraveyardIntoLibraryAction;
    private final CreaturesYouControlGetXUntilEndOfTurn creaturesYouControlGetXUntilEndOfTurn;
    private final Attach attach;

    @Autowired
    public AbilityActionFactory(ThatTargetsGetAction thatTargetsGetAction, DrawXCardsAction drawXCardsAction,
                                GainXLifeAction gainXLifeAction, EachPlayersGainXLifeAction eachPlayersGainXLifeAction, ShuffleTargetGraveyardIntoLibraryAction shuffleTargetGraveyardIntoLibraryAction,
                                CreaturesYouControlGetXUntilEndOfTurn creaturesYouControlGetXUntilEndOfTurn, Attach attach) {
        this.thatTargetsGetAction= thatTargetsGetAction;
        this.drawXCardsAction = drawXCardsAction;
        this.gainXLifeAction = gainXLifeAction;
        this.eachPlayersGainXLifeAction = eachPlayersGainXLifeAction;
        this.shuffleTargetGraveyardIntoLibraryAction = shuffleTargetGraveyardIntoLibraryAction;
        this.creaturesYouControlGetXUntilEndOfTurn = creaturesYouControlGetXUntilEndOfTurn;
        this.attach = attach;
    }

    public AbilityAction getAbilityAction(AbilityType abilityType) {
        if (abilityType == null) {
            return null;
        }

        switch (abilityType) {
            case DRAW_X_CARDS:
                return drawXCardsAction;
            case GAIN_X_LIFE:
                return gainXLifeAction;
            case EACH_PLAYERS_GAIN_X_LIFE:
                return eachPlayersGainXLifeAction;
            case SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER:
                return shuffleTargetGraveyardIntoLibraryAction;
            case THAT_TARGETS_GET:
                return thatTargetsGetAction;
            case CREATURES_YOU_CONTROL_GET_X_UNTIL_END_OF_TURN:
                return creaturesYouControlGetXUntilEndOfTurn;
            case ENCHANTED_CREATURE_GETS:
            case EQUIPPED_CREATURE_GETS:
                return attach;
            default:
                return null;
        }
    }

}
