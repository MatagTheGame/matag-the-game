package com.matag.game.turn.action.selection;

import com.matag.cards.ability.selector.MagicInstanceSelector;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.cards.ability.selector.StatusType;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import com.matag.player.PlayerType;
import org.springframework.stereotype.Component;

@Component
public class MagicInstancePermanentSelectorService {
  public CardInstanceSearch select(GameStatus gameStatus, CardInstance cardInstance, MagicInstanceSelector magicInstanceSelector) {
    CardInstanceSearch cards;
    if (magicInstanceSelector.getSelectorType().equals(SelectorType.PERMANENT)) {
      cards = gameStatus.getAllBattlefieldCardsSearch();

    } else if (magicInstanceSelector.getSelectorType().equals(SelectorType.SPELL)) {
      cards = gameStatus.getStack().search();

    } else if (magicInstanceSelector.getSelectorType().equals(SelectorType.ANY)) {
      cards = gameStatus.getAllBattlefieldCardsSearch();

    } else {
      throw new RuntimeException("Missing selectorType.");
    }

    if (magicInstanceSelector.getOfType() != null) {
      cards = cards.ofAnyOfTheTypes(magicInstanceSelector.getOfType());

    } else if (magicInstanceSelector.getNotOfType() != null) {
      cards = cards.notOfTypes(magicInstanceSelector.getNotOfType());
    }

    if (magicInstanceSelector.getOfSubtype() != null) {
      cards = cards.ofAnyOfTheSubtypes(magicInstanceSelector.getOfSubtype());

    } else if (magicInstanceSelector.getNotOfSubtype() != null) {
      cards = cards.notOfSubtypes(magicInstanceSelector.getNotOfSubtype());
    }

    if (magicInstanceSelector.getControllerType() == PlayerType.PLAYER) {
      cards = cards.controlledBy(getControllerOrOwner(cardInstance));
    } else if (magicInstanceSelector.getControllerType() == PlayerType.OPPONENT) {
      cards = cards.controlledBy(gameStatus.getOtherPlayer(getControllerOrOwner(cardInstance)).getName());
    }

    if (magicInstanceSelector.getWithAbilityType() != null) {
      cards = cards.withFixedAbility(magicInstanceSelector.getWithAbilityType());
    }

    if (magicInstanceSelector.getWithoutAbilityType() != null) {
      cards = cards.withoutFixedAbility(magicInstanceSelector.getWithoutAbilityType());
    }

    if (magicInstanceSelector.getPowerToughnessConstraint() != null) {
      cards = cards.ofPowerToughnessConstraint(magicInstanceSelector.getPowerToughnessConstraint());
    }

    if (magicInstanceSelector.getStatusTypes() != null) {
      if (magicInstanceSelector.getStatusTypes().contains(StatusType.ATTACKING) && magicInstanceSelector.getStatusTypes().contains(StatusType.BLOCKING)) {
        cards = cards.attackingOrBlocking();
      } else if (magicInstanceSelector.getStatusTypes().contains(StatusType.ATTACKING)) {
        cards = cards.attacking();
      } else if (magicInstanceSelector.getStatusTypes().contains(StatusType.BLOCKING)) {
        cards = cards.blocking();
      }

      if (magicInstanceSelector.getStatusTypes().contains(StatusType.ATTACKED) && magicInstanceSelector.getStatusTypes().contains(StatusType.BLOCKED)) {
        cards = cards.attackedOrBlocked();
      } else if (magicInstanceSelector.getStatusTypes().contains(StatusType.ATTACKED)) {
        cards = cards.attacked();
      } else if (magicInstanceSelector.getStatusTypes().contains(StatusType.BLOCKED)) {
        cards = cards.blocked();
      }

      if (magicInstanceSelector.getStatusTypes().contains(StatusType.TAPPED)) {
        cards = cards.tapped();
      }
    }

    if (magicInstanceSelector.getOfColors() != null) {
      cards = cards.ofAnyOfTheColors(magicInstanceSelector.getOfColors());
    }

    if (magicInstanceSelector.isColorless()) {
      cards = cards.colorless();
    }

    if (magicInstanceSelector.isMulticolor()) {
      cards = cards.multicolor();
    }

    if (magicInstanceSelector.isOthers()) {
      cards = cards.notWithId(cardInstance.getId());
    }

    if (magicInstanceSelector.isItself()) {
      cards = cards.withIdAsList(cardInstance.getId());
    }

    if (magicInstanceSelector.getTurnStatusType() != null) {
      cards = cards.onTurnStatusType(magicInstanceSelector.getTurnStatusType(), gameStatus);
    }

    if (magicInstanceSelector.isCurrentEnchanted()) {
      cards = cards.withIdAsList((cardInstance.getModifiers().getAttachedToId()));
    }

    if (magicInstanceSelector.isHistoric()) {
      cards = cards.historic();
    }

    return cards;
  }

  private String getControllerOrOwner(CardInstance cardInstance) {
    return cardInstance.getController() != null ? cardInstance.getController() : cardInstance.getOwner();
  }
}
