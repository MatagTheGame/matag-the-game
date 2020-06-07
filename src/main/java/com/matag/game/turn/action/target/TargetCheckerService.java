package com.matag.game.turn.action.target;

import com.matag.cards.ability.selector.CardInstanceSelector;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.cards.ability.target.Target;
import com.matag.cards.ability.trigger.TriggerType;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.CardInstanceSelectorService;
import com.matag.player.PlayerType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.matag.cards.ability.type.AbilityType.THAT_TARGETS_GET;
import static com.matag.cards.ability.type.AbilityType.abilityType;

@Component
@AllArgsConstructor
public class TargetCheckerService {
  private final CardInstanceSelectorService cardInstanceSelectorService;

  public void checkSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus, Map<Integer, List<Object>> targetsIdsForCardIds, String playedAbility) {
    List<CardInstanceAbility> playedAbilities = playedAbilities(cardToCast, playedAbility);

    for (CardInstanceAbility ability : playedAbilities) {
      if (ability.requiresTarget()) {
        if (targetsIdsForCardIds == null || !targetsIdsForCardIds.containsKey(cardToCast.getId())) {
          throw new MessageException(cardToCast.getIdAndName() + " requires a valid target.");
        }

        List<Object> targetsIds = targetsIdsForCardIds.get(cardToCast.getId());
        checkThatTargetsAreDifferent(ability.getTargets(), targetsIds);
        for (int i = 0; i < ability.getTargets().size(); i++) {
          Object targetId = getTargetIdAtIndex(targetsIds, i);
          check(gameStatus, cardToCast, ability.getTargets().get(i), targetId);
        }

        cardToCast.getModifiers().addTargets(targetsIds);
      }
    }
  }

  private List<CardInstanceAbility> playedAbilities(CardInstance cardToCast, String playedAbility) {
    if (playedAbility != null) {
      return cardToCast.getAbilitiesByType(abilityType(playedAbility));
    } else {
      return cardToCast.getAbilitiesByTriggerType(TriggerType.CAST);
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

  public boolean checkIfRequiresTarget(CardInstance cardToCast, GameStatus gameStatus) {
    for (CardInstanceAbility ability : cardToCast.getAbilitiesByType(THAT_TARGETS_GET)) {
      return ability.requiresTarget();
    }

    return false;
  }

  public boolean checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus) {
    for (CardInstanceAbility ability : cardToCast.getAbilitiesByType(THAT_TARGETS_GET)) {
      for (Target target : ability.getTargets()) {
        CardInstanceSearch cards = cardInstanceSelectorService.select(gameStatus, cardToCast, target.getCardInstanceSelector());
        if (cards.isNotEmpty()) {
          return true;
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
      if (cards.withId(targetCardId).isEmpty()) {
        throw new MessageException("Selected targets were not valid.");
      }
    }
  }
}
