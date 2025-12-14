package com.matag.game.turn.action;

import org.springframework.stereotype.Component;

import com.matag.cards.ability.type.AbilityType;
import com.matag.cards.ability.AbilityAction;
import com.matag.game.turn.action.attach.AttachAction;
import com.matag.game.turn.action.selection.SelectedPermanentsGetAction;
import com.matag.game.turn.action.shuffle.ShuffleTargetGraveyardIntoLibraryAction;
import com.matag.game.turn.action.target.ThatTargetsGetAction;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AbilityActionFactory {
  private final ThatTargetsGetAction thatTargetsGetAction;
  private final ShuffleTargetGraveyardIntoLibraryAction shuffleTargetGraveyardIntoLibraryAction;
  private final SelectedPermanentsGetAction selectedPermanentsGetAction;
  private final AttachAction attachAction;

  public AbilityAction getAbilityAction(AbilityType abilityType) {
    return switch (abilityType) {
      case SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER -> shuffleTargetGraveyardIntoLibraryAction;
      case THAT_TARGETS_GET -> thatTargetsGetAction;
      case SELECTED_PERMANENTS_GET -> selectedPermanentsGetAction;
      case ENCHANT, EQUIP -> attachAction;
      default -> null;
    };
  }

}
