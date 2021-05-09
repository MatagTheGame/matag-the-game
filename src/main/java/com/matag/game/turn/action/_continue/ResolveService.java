package com.matag.game.turn.action._continue;

import static com.matag.game.turn.action._continue.InputRequiredActions.DISCARD_A_CARD;
import static com.matag.game.turn.action._continue.InputRequiredActions.SCRY;
import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.matag.cards.ability.trigger.TriggerType;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.AbilityActionFactory;
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService;
import com.matag.game.turn.action.leave.PutIntoGraveyardService;
import com.matag.game.turn.action.player.DiscardXCardsService;
import com.matag.game.turn.action.player.ScryXCardsService;
import com.matag.game.turn.action.target.TargetCheckerService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ResolveService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResolveService.class);

  private final ContinueService continueService;
  private final AbilityActionFactory abilityActionFactory;
  private final EnterCardIntoBattlefieldService enterCardIntoBattlefieldService;
  private final PutIntoGraveyardService putIntoGraveyardService;
  private final TargetCheckerService targetCheckerService;
  private final DiscardXCardsService discardXCardsService;
  private final ScryXCardsService scryXCardsService;

  public void resolve(GameStatus gameStatus, String inputRequiredAction, String inputRequiredChoices, List<Integer> targetCardIds, Map<Integer, List<Object>> targetsIdsForCardIds) {
    if (!gameStatus.getStack().isEmpty()) {
      var stackItemToResolve = gameStatus.getStack().peek();

      if (stackItemToResolve.getTriggeredAbilities().isEmpty()) {
        gameStatus.getStack().pop();
        resolveCardInstanceFromStack(gameStatus, stackItemToResolve);

      } else {
        var controllerName = stackItemToResolve.getController();
        var otherPlayerName = gameStatus.getOtherPlayer(controllerName).getName();

        stackItemToResolve.getTriggeredAbilities().stream()
          .filter(triggeredAbility -> !triggeredAbility.getTrigger().getType().equals(TriggerType.TRIGGERED_ABILITY))
          .forEach(t -> stackItemToResolve.acknowledgedBy(controllerName));

        if (gameStatus.getActivePlayer().getName().equals(controllerName) && !stackItemToResolve.getAcknowledgedBy().contains(controllerName)) {
          if (targetCheckerService.checkIfRequiresTarget(stackItemToResolve)) {
            if (targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(stackItemToResolve, gameStatus)) {
              targetCheckerService.checkSpellOrAbilityTargetRequisites(stackItemToResolve, gameStatus, targetsIdsForCardIds, "THAT_TARGETS_GET");
            }
          }
          stackItemToResolve.acknowledgedBy(controllerName);

        } else if (!stackItemToResolve.getAcknowledgedBy().contains(otherPlayerName)) {
          var needsTargets = stackItemToResolve.getTriggeredAbilities().stream()
            .map(CardInstanceAbility::getTargets)
            .flatMap(List::stream)
            .anyMatch(target -> !target.isOptional());
          var hasSelectedTargets = !stackItemToResolve.getModifiers().getTargets().isEmpty();
          if (!needsTargets || hasSelectedTargets) {
            stackItemToResolve.acknowledgedBy(otherPlayerName);
          }
        }

        if (stackItemToResolve.acknowledged()) {
          gameStatus.getStack().pop();
          resolveTriggeredAbility(gameStatus, stackItemToResolve);
        }
      }

      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonActivePlayer().getName());

    } else if (Objects.equals(gameStatus.getTurn().getInputRequiredAction(), inputRequiredAction)) {
      resolveInputRequiredAction(gameStatus, inputRequiredAction, inputRequiredChoices, targetCardIds);

    } else {
      String message = "Cannot resolve inputRequiredAction " + inputRequiredAction + " as current inputRequiredAction is " + gameStatus.getTurn().getInputRequiredAction();
      throw new MessageException(message);
    }
  }

  private void resolveCardInstanceFromStack(GameStatus gameStatus, CardInstance cardToResolve) {
    performAbilitiesActions(gameStatus, cardToResolve, cardToResolve.getAbilitiesByTriggerType(TriggerType.CAST));

    if (cardToResolve.isPermanent()) {
      enterCardIntoBattlefieldService.enter(gameStatus, cardToResolve);

    } else {
      cardToResolve.resetAllModifiers();
      putIntoGraveyardService.putIntoGraveyard(gameStatus, cardToResolve);
    }
  }

  private void resolveTriggeredAbility(GameStatus gameStatus, CardInstance stackItemToResolve) {
    performAbilitiesActions(gameStatus, stackItemToResolve, stackItemToResolve.getTriggeredAbilities());
    stackItemToResolve.getTriggeredAbilities().clear();
  }

  private void resolveInputRequiredAction(GameStatus gameStatus, String inputRequiredAction, String inputRequiredChoices, List<Integer> cardIds) {
    if (DISCARD_A_CARD.equals(inputRequiredAction)) {
      discardXCardsService.discardXCards(cardIds, gameStatus);

    } else if (SCRY.equals(inputRequiredAction)) {
      var positions = extractIntegerChoices(inputRequiredChoices);
      if (positions.size() == parseInt(gameStatus.getTurn().getInputRequiredActionParameter())) {
        scryXCardsService.scryXCards(gameStatus, positions);
      } else {
        throw new MessageException("Please click on the visible cards to scry them.");
      }
    }

    gameStatus.getTurn().setInputRequiredAction(null);
    gameStatus.getTurn().setInputRequiredActionParameter(null);
    continueService.next(gameStatus);
  }

  private List<Integer> extractIntegerChoices(String inputRequiredChoices) {
    if (inputRequiredChoices == null) {
      return new ArrayList<>();
    }
    return Arrays.stream(inputRequiredChoices.split(",")).map(Integer::parseInt).collect(Collectors.toList());
  }

  private void performAbilitiesActions(GameStatus gameStatus, CardInstance cardToResolve, List<CardInstanceAbility> abilities) {
    try {
      abilities.forEach(ability -> performAbilityAction(gameStatus, cardToResolve, ability));
    } catch (MessageException e) {
      LOGGER.info("{}: Target is now invalid during resolution, Cancelling the entire spell. [{}] ", cardToResolve.getIdAndName(), e.getMessage());
    }

    cardToResolve.getModifiers().resetTargets();
  }

  private void performAbilityAction(GameStatus gameStatus, CardInstance cardToResolve, CardInstanceAbility ability) {
    var abilityAction = abilityActionFactory.getAbilityAction(ability.getAbilityType());
    if (abilityAction != null) {
      checkTargets(gameStatus, cardToResolve, ability);
      abilityAction.perform(cardToResolve, gameStatus, ability);
    }
  }

  private void checkTargets(GameStatus gameStatus, CardInstance cardToResolve, CardInstanceAbility ability) {
    for (var i = 0; i < ability.getTargets().size(); i++) {
      var target = ability.getTargets().get(i);
      var targetId = targetCheckerService.getTargetIdAtIndex(cardToResolve, ability, i);
      targetCheckerService.check(gameStatus, cardToResolve, target, targetId);
    }
  }
}
