package com.aa.mtg.game.turn.action.selection;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.CardInstanceSearch;
import com.aa.mtg.cards.ability.selector.CardInstanceSelector;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.aa.mtg.cards.ability.selector.SelectorType.ANY;
import static com.aa.mtg.cards.ability.selector.SelectorType.PERMANENT;
import static com.aa.mtg.cards.ability.selector.StatusType.ATTACKING;
import static com.aa.mtg.cards.ability.selector.StatusType.BLOCKING;
import static com.aa.mtg.player.PlayerType.OPPONENT;
import static com.aa.mtg.player.PlayerType.PLAYER;
import static java.util.Collections.emptyList;

@Component
public class CardInstanceSelectorService {
    public CardInstanceSearch select(GameStatus gameStatus, CardInstance cardInstance, CardInstanceSelector cardInstanceSelector) {
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

        if (cardInstanceSelector.getOfSubtypeOf() != null) {
            cards = cards.ofAnyOfTheSubtypes(cardInstanceSelector.getOfSubtypeOf());
        }

        if (cardInstanceSelector.getControllerType() == PLAYER) {
            cards = cards.controlledBy(gameStatus.getCurrentPlayer().getName());
        } else if (cardInstanceSelector.getControllerType() == OPPONENT) {
            cards = cards.controlledBy(gameStatus.getNonCurrentPlayer().getName());
        }

        if (cardInstanceSelector.getWithAbilityType() != null) {
            cards = cards.withFixedAbility(cardInstanceSelector.getWithAbilityType());
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
