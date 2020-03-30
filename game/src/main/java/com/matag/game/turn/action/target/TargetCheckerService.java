package com.matag.game.turn.action.target;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.CardInstanceSearch;
import com.matag.cardinstance.ability.AbilityAction;
import com.matag.cardinstance.ability.CardInstanceAbility;
import com.matag.cards.ability.selector.CardInstanceSelector;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.cards.ability.target.Target;
import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.AbilityActionFactory;
import com.matag.game.turn.action.selection.CardInstanceSelectorService;
import com.matag.player.PlayerType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.matag.cards.ability.type.AbilityType.abilityType;

@Component
@AllArgsConstructor
public class TargetCheckerService {
  private final AbilityActionFactory abilityActionFactory;
  private final CardInstanceSelectorService cardInstanceSelectorService;

  public void checkSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus, Map<Integer, List<Object>> targetsIdsForCardIds, String playedAbility) {
    for (CardInstanceAbility ability : cardToCast.getAbilities()) {
      AbilityAction abilityAction = abilityActionFactory.getAbilityAction(abilityType(playedAbility));
      if (abilityAction != null && ability.requiresTarget()) {
        if (targetsIdsForCardIds == null || !targetsIdsForCardIds.containsKey(cardToCast.getId())) {
          throw new MessageException(cardToCast.getIdAndName() + " requires a valid target.");
        }

        List<Object> targetsIds = targetsIdsForCardIds.get(cardToCast.getId());
        checkThatTargetsAreDifferent(ability.getTargets(), targetsIds);
        for (int i = 0; i < ability.getTargets().size(); i++) {
          Object targetId = getTargetIdAtIndex(targetsIds, i);
          check(gameStatus, cardToCast, ability.getTargets().get(i), targetId);
        }

        cardToCast.getModifiers().setTargets(targetsIds);
      }
    }
  }

  private Object getTargetIdAtIndex(List<Object> targetsIds, int i) {
    if (i < targetsIds.size()) {
      return targetsIds.get(i);
    }
    return null;
  }

  private void checkThatTargetsAreDifferent(List<Target> targets, List<Object> targetsIds) {
    for (int i = 0; i < Math.min(targets.size(), targetsIds.size()); i++) {
      Target target = targets.get(i);
      if (target.isOther()) {
        if (countTargetsWithId(targetsIds, targetsIds.get(i)) > 1) {
          throw new MessageException("Targets must be different.");
        }
      }
    }
  }

  private long countTargetsWithId(List<Object> targetsIds, Object targetId) {
    return targetsIds.stream().filter(t -> t.equals(targetId)).count();
  }

  public boolean checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus) {
    for (CardInstanceAbility ability : cardToCast.getAbilities()) {
      AbilityAction abilityAction = abilityActionFactory.getAbilityAction(abilityType("THAT_TARGETS_GET"));
      if (abilityAction == null) {
        return true;

      } else if (!ability.requiresTarget()) {
        return true;

      } else {
        for (Target target : ability.getTargets()) {
          CardInstanceSearch cards = cardInstanceSelectorService.select(gameStatus, cardToCast, target.getCardInstanceSelector());
          if (cards.isNotEmpty()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public void check(GameStatus gameStatus, CardInstance cardInstance, Target target, Object targetId) {
    CardInstanceSelector cardInstanceSelector = target.getCardInstanceSelector();
    if (targetId == null) {
      if (!target.isOptional()) {
        throw new MessageException("A target is required.");
      }

    } else if (targetId instanceof String) {
      if (!(cardInstanceSelector.getSelectorType().equals(SelectorType.PLAYER) || cardInstanceSelector.getSelectorType().equals(SelectorType.ANY))) {
        throw new MessageException(targetId + " is not valid for type " + cardInstanceSelector.getSelectorType());
      }

      if (PlayerType.OPPONENT.equals(cardInstanceSelector.getControllerType()) && cardInstance.getController().equals(targetId)) {
        throw new MessageException(targetId + " is not valid for type " + cardInstanceSelector.getSelectorType() + " (needs to be an opponent)");
      }

    } else {
      if (cardInstanceSelector.getSelectorType().equals(SelectorType.PLAYER)) {
        throw new MessageException(targetId + " is not valid for type " + cardInstanceSelector.getSelectorType());
      }

      int targetCardId = (int) targetId;
      CardInstanceSearch cards = cardInstanceSelectorService.select(gameStatus, cardInstance, cardInstanceSelector);
      if (!cards.withId(targetCardId).isPresent()) {
        throw new MessageException("Selected targets were not valid.");
      }
    }
  }
}
