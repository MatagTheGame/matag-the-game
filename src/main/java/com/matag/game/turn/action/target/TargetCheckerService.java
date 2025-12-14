package com.matag.game.turn.action.target;

import static com.matag.cards.ability.type.AbilityType.THAT_TARGETS_GET;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.matag.cards.ability.type.AbilityType;
import org.springframework.stereotype.Component;

import com.matag.cards.ability.selector.SelectorType;
import com.matag.cards.ability.target.Target;
import com.matag.cards.ability.trigger.TriggerType;
import com.matag.game.cardinstance.CardInstance;
import com.matag.cards.ability.Ability;
import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.MagicInstancePermanentSelectorService;
import com.matag.player.PlayerType;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TargetCheckerService {
  private final MagicInstancePermanentSelectorService magicInstancePermanentSelectorService;

  public void checkSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus, Map<Integer, List<Object>> targetsIdsForCardIds, String playedAbility) {
    var playedAbilities = playedAbilities(cardToCast, playedAbility);
    var targetsIds = getTargetsIds(targetsIdsForCardIds, cardToCast.getId());

    var targetIndex = 0;
    for (var ability : playedAbilities) {
      if (!ability.getAbility().getTargets().isEmpty()) {
        checkThatTargetsAreDifferent(ability.getAbility().getTargets(), targetsIds);
        for (var i = 0; i < ability.getAbility().getTargets().size(); i++, targetIndex++) {
          var targetId = targetIndex < targetsIds.size() ? targetsIds.get(targetIndex) : null;
          check(gameStatus, cardToCast, ability.getAbility().getTargets().get(i), targetId);
        }

        cardToCast.getModifiers().addTargets(targetsIds);
      }
    }
  }

  public boolean checkIfRequiresTarget(CardInstance cardToCast) {
    for (var ability : cardToCast.getAbilitiesByType(THAT_TARGETS_GET)) {
      return !ability.getAbility().getTargets().isEmpty();
    }

    return false;
  }

  public boolean checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus) {
    for (var ability : cardToCast.getAbilitiesByType(THAT_TARGETS_GET)) {
      for (var target : ability.getTargets()) {
        if (target.getMagicInstanceSelector().getSelectorType().equals(SelectorType.PLAYER)) {
          return true;

        } else {
          var possibleTargets = magicInstancePermanentSelectorService.selectAsTarget(gameStatus, cardToCast, target.getMagicInstanceSelector());
          if (possibleTargets.isNotEmpty()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public void check(GameStatus gameStatus, CardInstance cardInstance, Target target, Object targetId) {
    var magicInstanceSelector = target.getMagicInstanceSelector();
    if (targetId == null) {
      if (!target.getOptional()) {
        throw new MessageException(cardInstance.getIdAndName() + " requires a valid target.");
      }

    } else if (targetId instanceof String) {
      if (!(magicInstanceSelector.getSelectorType().equals(SelectorType.PLAYER) || magicInstanceSelector.getSelectorType().equals(SelectorType.ANY))) {
        throw new MessageException(targetId + " is not valid for type " + magicInstanceSelector.getSelectorType());
      }

      if (PlayerType.OPPONENT.equals(magicInstanceSelector.getControllerType()) && cardInstance.getController().equals(targetId)) {
        throw new MessageException(targetId + " is not valid for type " + magicInstanceSelector.getSelectorType() + " (needs to be an opponent)");
      }

    } else {
      if (magicInstanceSelector.getSelectorType().equals(SelectorType.PLAYER)) {
        throw new MessageException(targetId + " is not valid for type " + magicInstanceSelector.getSelectorType());
      }

      var targetCardId = (int) targetId;
      var possibleTargets = magicInstancePermanentSelectorService.selectAsTarget(gameStatus, cardInstance, magicInstanceSelector);
      if (possibleTargets.withId(targetCardId).isEmpty()) {
        throw new MessageException("Selected targets were not valid.");
      }
    }
  }

  public Object getTargetIdAtIndex(CardInstance cardInstance, Ability ability, int index) {
    var abilityIndex = cardInstance.getAbilities().indexOf(ability);
    var firstTargetIndex = 0;
    for (var i = 0; i < abilityIndex; i++) {
      firstTargetIndex += cardInstance.getAbilities().get(i).getAbility().getTargets().size();
    }

    if (cardInstance.getModifiers().getTargets().size() > firstTargetIndex + index) {
      return cardInstance.getModifiers().getTargets().get(firstTargetIndex + index);
    }

    return null;
  }

  private List<Ability> playedAbilities(CardInstance cardToCast, String playedAbility) {
    if (playedAbility != null) {
      return cardToCast.getAbilitiesByType(AbilityType.valueOf(playedAbility));
    } else {
      return cardToCast.getAbilitiesByTriggerType(TriggerType.CAST);
    }
  }

  private List<Object> getTargetsIds(Map<Integer, List<Object>> targetsIdsForCardIds, int cardId) {
    if (targetsIdsForCardIds == null) {
      return Collections.emptyList();
    }

    return targetsIdsForCardIds.getOrDefault(cardId, Collections.emptyList());
  }

  private void checkThatTargetsAreDifferent(List<Target> targets, List<Object> targetsIds) {
    for (var i = 0; i < Math.min(targets.size(), targetsIds.size()); i++) {
      var target = targets.get(i);
      if (target.getOther()) {
        if (countTargetsWithId(targetsIds, targetsIds.get(i)) > 1) {
          throw new MessageException("Targets must be different.");
        }
      }
    }
  }

  private long countTargetsWithId(List<Object> targetsIds, Object targetId) {
    return targetsIds.stream().filter(t -> t.equals(targetId)).count();
  }
}
