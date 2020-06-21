package com.matag.game.turn.action.target;

import com.matag.cards.ability.selector.MagicInstanceSelector;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.cards.ability.target.Target;
import com.matag.cards.ability.trigger.TriggerType;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.MagicInstancePermanentSelectorService;
import com.matag.player.PlayerType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.matag.cards.ability.type.AbilityType.THAT_TARGETS_GET;
import static com.matag.cards.ability.type.AbilityType.abilityType;

@Component
@AllArgsConstructor
public class TargetCheckerService {
  private final MagicInstancePermanentSelectorService magicInstancePermanentSelectorService;

  public void checkSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus, Map<Integer, List<Object>> targetsIdsForCardIds, String playedAbility) {
    List<CardInstanceAbility> playedAbilities = playedAbilities(cardToCast, playedAbility);
    List<Object> targetsIds = getTargetsIds(targetsIdsForCardIds, cardToCast.getId());

    int targetIndex = 0;
    for (CardInstanceAbility ability : playedAbilities) {
      if (ability.requiresTarget()) {
        checkThatTargetsAreDifferent(ability.getTargets(), targetsIds);
        for (int i = 0; i < ability.getTargets().size(); i++, targetIndex++) {
          Object targetId = targetIndex < targetsIds.size() ? targetsIds.get(targetIndex) : null;
          check(gameStatus, cardToCast, ability.getTargets().get(i), targetId);
        }

        cardToCast.getModifiers().addTargets(targetsIds);
      }
    }
  }

  public boolean checkIfRequiresTarget(CardInstance cardToCast) {
    for (CardInstanceAbility ability : cardToCast.getAbilitiesByType(THAT_TARGETS_GET)) {
      return ability.requiresTarget();
    }

    return false;
  }

  public boolean checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus) {
    for (CardInstanceAbility ability : cardToCast.getAbilitiesByType(THAT_TARGETS_GET)) {
      for (Target target : ability.getTargets()) {
        if (target.getMagicInstanceSelector().getSelectorType().equals(SelectorType.PLAYER)) {
          return true;

        } else {
          CardInstanceSearch cards = magicInstancePermanentSelectorService.select(gameStatus, cardToCast, target.getMagicInstanceSelector());
          if (cards.isNotEmpty()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public void check(GameStatus gameStatus, CardInstance cardInstance, Target target, Object targetId) {
    MagicInstanceSelector magicInstanceSelector = target.getMagicInstanceSelector();
    if (targetId == null) {
      if (!target.isOptional()) {
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

      int targetCardId = (int) targetId;
      CardInstanceSearch cards = magicInstancePermanentSelectorService.select(gameStatus, cardInstance, magicInstanceSelector);
      if (cards.withId(targetCardId).isEmpty()) {
        throw new MessageException("Selected targets were not valid.");
      }
    }
  }

  public Object getTargetIdAtIndex(CardInstance cardInstance, CardInstanceAbility ability, int index) {
    int abilityIndex = cardInstance.getAbilities().indexOf(ability);
    int firstTargetIndex = 0;
    for (int i = 0; i < abilityIndex; i++) {
      firstTargetIndex += cardInstance.getAbilities().get(i).getTargets().size();
    }

    if (cardInstance.getModifiers().getTargets().size() > firstTargetIndex + index) {
      return cardInstance.getModifiers().getTargets().get(firstTargetIndex + index);
    }

    return null;
  }

  private List<CardInstanceAbility> playedAbilities(CardInstance cardToCast, String playedAbility) {
    if (playedAbility != null) {
      return cardToCast.getAbilitiesByType(abilityType(playedAbility));
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
}
