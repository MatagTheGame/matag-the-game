package com.matag.game.turn.action;

import com.matag.cards.ability.type.AbilityType;
import com.matag.game.cardinstance.ability.AbilityAction;
import com.matag.game.turn.action.attach.AttachAction;
import com.matag.game.turn.action.draw.DrawXCardsAction;
import com.matag.game.turn.action.life.AddXLifeAction;
import com.matag.game.turn.action.selection.SelectedPermanentsGetXUntilEndOfTurnAction;
import com.matag.game.turn.action.shuffle.ShuffleTargetGraveyardIntoLibraryAction;
import com.matag.game.turn.action.target.ThatTargetsGetAction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AbilityActionFactory {
  private final ThatTargetsGetAction thatTargetsGetAction;
  private final DrawXCardsAction drawXCardsAction;
  private final AddXLifeAction addXLifeAction;
  private final ShuffleTargetGraveyardIntoLibraryAction shuffleTargetGraveyardIntoLibraryAction;
  private final SelectedPermanentsGetXUntilEndOfTurnAction selectedPermanentsGetXUntilEndOfTurnAction;
  private final AttachAction attachAction;

  public AbilityAction getAbilityAction(AbilityType abilityType) {
    if (abilityType == null) {
      return null;
    }

    switch (abilityType) {
      case DRAW_X_CARDS:
        return drawXCardsAction;
      case ADD_X_LIFE:
        return addXLifeAction;
      case SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER:
        return shuffleTargetGraveyardIntoLibraryAction;
      case THAT_TARGETS_GET:
        return thatTargetsGetAction;
      case SELECTED_PERMANENTS_GET:
        return selectedPermanentsGetXUntilEndOfTurnAction;
      case ENCHANTED_CREATURE_GETS:
      case EQUIPPED_CREATURE_GETS:
        return attachAction;
      default:
        return null;
    }
  }

}
