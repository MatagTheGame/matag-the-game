package com.matag.game.turn.action.selection;

import com.matag.cards.ability.selector.CardInstanceSelector;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.cards.ability.selector.StatusType;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.player.PlayerType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CardInstanceSelectorService {
  public CardInstanceSearch select(GameStatus gameStatus, CardInstance cardInstance, CardInstanceSelector cardInstanceSelector) {
    CardInstanceSearch cards;
    if (cardInstanceSelector.getSelectorType().equals(SelectorType.PERMANENT)) {
      cards = gameStatus.getAllBattlefieldCards();

    } else if (cardInstanceSelector.getSelectorType().equals(SelectorType.SPELL)) {
      cards = new CardInstanceSearch(gameStatus.getStack().getItems());

    } else if (cardInstanceSelector.getSelectorType().equals(SelectorType.ANY)) {
      cards = gameStatus.getAllBattlefieldCards();

    } else {
      throw new RuntimeException("Missing selectorType.");
    }

    if (cardInstanceSelector.getOfType() != null) {
      cards = cards.ofAnyOfTheTypes(cardInstanceSelector.getOfType());

    } else if (cardInstanceSelector.getNotOfType() != null) {
      cards = cards.notOfTypes(cardInstanceSelector.getNotOfType());
    }

    if (cardInstanceSelector.getOfSubtype() != null) {
      cards = cards.ofAnyOfTheSubtypes(cardInstanceSelector.getOfSubtype());

    } else if (cardInstanceSelector.getNotOfSubtype() != null) {
      cards = cards.notOfSubtypes(cardInstanceSelector.getNotOfSubtype());
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
      } else if (cardInstanceSelector.getStatusTypes().contains(StatusType.ATTACKED) && cardInstanceSelector.getStatusTypes().contains(StatusType.BLOCKED)) {
        cards = cards.attackedOrBlocked();
      } else if (cardInstanceSelector.getStatusTypes().contains(StatusType.ATTACKED)) {
        cards = cards.attacked();
      } else if (cardInstanceSelector.getStatusTypes().contains(StatusType.BLOCKED)) {
        cards = cards.blocked();
      }
    }

    if (cardInstanceSelector.getOfColors() != null) {
      cards = cards.ofAnyOfTheColors(cardInstanceSelector.getOfColors());
    }

    if (cardInstanceSelector.isColorless()) {
      cards = cards.colorless();
    }

    if (cardInstanceSelector.isMulticolor()) {
      cards = cards.multicolor();
    }

    if (cardInstanceSelector.isOthers()) {
      cards = cards.notWithId(cardInstance.getId());
    }

    if (cardInstanceSelector.isItself()) {
      cards = cards.withIdAsList(cardInstance.getId());
    }

    if (cardInstanceSelector.getTurnStatusType() != null) {
      cards = cards.onTurnStatusType(cardInstanceSelector.getTurnStatusType(), gameStatus);
    }

    if (cardInstanceSelector.isCurrentEnchanted()) {
      cards = cards.withIdAsList((cardInstance.getModifiers().getAttachedToId()));
    }

    if (cardInstanceSelector.isHistoric()) {
      cards = cards.historic();
    }

    return cards;
  }

  public List<Player> selectPlayers(GameStatus gameStatus, CardInstance cardInstance, CardInstanceSelector cardInstanceSelector) {
    List<Player> players = new ArrayList<>();

    if (cardInstanceSelector.getSelectorType().equals(SelectorType.PLAYER)) {
      if (cardInstanceSelector.isItself()) {
        players.add(gameStatus.getPlayerByName(cardInstance.getController()));

      } else {
        Player player = gameStatus.getPlayerByName(cardInstance.getController());
        Player opponent = gameStatus.getOtherPlayer(player);
        players.add(opponent);
        if (cardInstanceSelector.getControllerType() != PlayerType.OPPONENT) {
          players.add(player);
        }
      }
    }

    return players;
  }
}
