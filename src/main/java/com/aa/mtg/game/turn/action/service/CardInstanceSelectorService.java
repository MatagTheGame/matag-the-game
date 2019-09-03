package com.aa.mtg.game.turn.action.service;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.cards.selector.CardInstanceSelector;
import com.aa.mtg.game.player.PlayerType;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.aa.mtg.cards.selector.SelectorType.ANY;
import static com.aa.mtg.cards.selector.SelectorType.PERMANENT;
import static com.aa.mtg.cards.selector.StatusType.ATTACKING;
import static com.aa.mtg.cards.selector.StatusType.BLOCKING;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;

@Component
public class CardInstanceSelectorService {
    CardInstanceSearch select(GameStatus gameStatus, CardInstance cardInstance, CardInstanceSelector cardInstanceSelector) {
        CardInstanceSearch cards;
        if (cardInstanceSelector.getSelectorType().equals(PERMANENT)) {
            cards = gameStatus.getAllBattlefieldCards();

            if (cardInstanceSelector.getOfType() != null) {
                cards = cards.ofAnyOfTheTypes(cardInstanceSelector.getOfType());

            } else if(cardInstanceSelector.getNotOfType() != null) {
                cards = cards.notOfTypes(cardInstanceSelector.getNotOfType());
            }

        } else if (cardInstanceSelector.getSelectorType().equals(ANY)) {
            cards = gameStatus.getAllBattlefieldCards();

        } else {
            throw new RuntimeException("Missing selectorType.");
        }

        if (cardInstanceSelector.getControllerType() == PlayerType.PLAYER) {
            cards = cards.controlledBy(gameStatus.getCurrentPlayer().getName());
        } else if (cardInstanceSelector.getControllerType() == OPPONENT) {
            cards = cards.controlledBy(gameStatus.getNonCurrentPlayer().getName());
        }

        if (cardInstanceSelector.getPowerToughnessConstraint() != null) {
            cards = cards.ofPowerToughnessConstraint(cardInstanceSelector.getPowerToughnessConstraint());
        }

        if (cardInstanceSelector.getStatusTypes() != null) {
            if (cardInstanceSelector.getStatusTypes().contains(ATTACKING) && cardInstanceSelector.getStatusTypes().contains(BLOCKING)) {
                cards = cards.attackingOrBlocking();
            } else if (cardInstanceSelector.getStatusTypes().contains(ATTACKING)) {
                cards = cards.attacking();
            } else if (cardInstanceSelector.getStatusTypes().contains(BLOCKING)) {
                cards = cards.blocking();
            }
        }

        if (cardInstanceSelector.isOthers()) {
            cards = cards.notWithId(cardInstance.getId());
        }
        return cards;
    }
}
