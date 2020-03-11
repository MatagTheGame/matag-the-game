package com.matag.game.turn.action.selection;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.CardInstanceSearch;
import com.matag.cards.ability.selector.CardInstanceSelector;
import com.matag.game.status.GameStatus;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.cards.ability.selector.StatusType;
import com.matag.player.PlayerType;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static java.util.Collections.emptyList;

@Component
public class CardInstanceSelectorService {
  public CardInstanceSearch select(GameStatus gameStatus, CardInstance cardInstance, CardInstanceSelector cardInstanceSelector) {
    CardInstanceSearch cards;
    if (cardInstanceSelector.getSelectorType().equals(SelectorType.PERMANENT)) {
      cards = gameStatus.getAllBattlefieldCards();

      if (cardInstanceSelector.getOfType() != null) {
        cards = cards.ofAnyOfTheTypes(cardInstanceSelector.getOfType());

      } else if (cardInstanceSelector.getNotOfType() != null) {
        cards = cards.notOfTypes(cardInstanceSelector.getNotOfType());
      }

    } else if (cardInstanceSelector.getSelectorType().equals(SelectorType.ANY)) {
      cards = gameStatus.getAllBattlefieldCards();

    } else {
      throw new RuntimeException("Missing selectorType.");
    }

    if (cardInstanceSelector.getOfSubtypeOf() != null) {
      cards = cards.ofAnyOfTheSubtypes(cardInstanceSelector.getOfSubtypeOf());
    }

    if (cardInstanceSelector.getControllerType() == PlayerType.PLAYER) {
      cards = cards.controlledBy(gameStatus.getCurrentPlayer().getName());
    } else if (cardInstanceSelector.getControllerType() == PlayerType.OPPONENT) {
      cards = cards.controlledBy(gameStatus.getNonCurrentPlayer().getName());
    }

    if (cardInstanceSelector.getWithAbilityType() != null) {
      cards = cards.withFixedAbility(cardInstanceSelector.getWithAbilityType());
    }

    if (cardInstanceSelector.getPowerToughnessConstraint() != null) {
      cards = cards.ofPowerToughnessConstraint(cardInstanceSelector.getPowerToughnessConstraint());
    }

    if (cardInstanceSelector.getStatusTypes() != null) {
      if (cardInstanceSelector.getStatusTypes().contains(StatusType.ATTACKING) && cardInstanceSelector.getStatusTypes().contains(StatusType.BLOCKING)) {
        cards = cards.attackingOrBlocking();
      } else if (cardInstanceSelector.getStatusTypes().contains(StatusType.ATTACKING)) {
        cards = cards.attacking();
      } else if (cardInstanceSelector.getStatusTypes().contains(StatusType.BLOCKING)) {
        cards = cards.blocking();
      }
    }

    if (cardInstanceSelector.getOfColors() != null) {
      cards = cards.ofAnyOfTheColors(cardInstanceSelector.getOfColors());
    }

    if (cardInstanceSelector.isOthers()) {
      cards = cards.notWithId(cardInstance.getId());
    }

    if (cardInstanceSelector.isItself()) {
      cards = cards.withId(cardInstance.getId()).map(Collections::singletonList).map(CardInstanceSearch::new).orElse(new CardInstanceSearch(emptyList()));
    }

    if (cardInstanceSelector.getTurnStatusType() != null) {
      cards = cards.onTurnStatusType(cardInstanceSelector.getTurnStatusType(), gameStatus);
    }

    return cards;
  }
}
