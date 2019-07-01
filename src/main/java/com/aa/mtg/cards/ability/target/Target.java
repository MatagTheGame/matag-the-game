package com.aa.mtg.cards.ability.target;

import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.player.PlayerType;
import com.aa.mtg.game.status.GameStatus;
import lombok.Builder;

import java.util.List;

import static com.aa.mtg.cards.ability.target.TargetType.ANY;
import static com.aa.mtg.cards.ability.target.TargetType.PERMANENT;

@Builder
public class Target {
    private final TargetType targetType;
    private final List<Type> ofType;
    private final String ofSubtypeOf;
    private final AbilityType withAbilityType;
    private final Color ofColor;
    private final TargetPowerToughnessConstraint targetPowerToughnessConstraint;
    private final PlayerType targetControllerType;

    public Target(TargetType targetType, List<Type> ofType, String ofSubtypeOf, AbilityType withAbilityType, Color ofColor, TargetPowerToughnessConstraint targetPowerToughnessConstraint, PlayerType targetControllerType) {
        this.targetType = targetType;
        this.ofType = ofType;
        this.ofSubtypeOf = ofSubtypeOf;
        this.withAbilityType = withAbilityType;
        this.ofColor = ofColor;
        this.targetPowerToughnessConstraint = targetPowerToughnessConstraint;
        this.targetControllerType = targetControllerType;
    }

    public void check(GameStatus gameStatus, Object targetId) {
        if (targetId instanceof String) {
            if (!(targetType.equals(TargetType.PLAYER) || targetType.equals(ANY))) {
                throw new MessageException(targetId + " is not valid for targetType PERMANENT");
            }

        } else {
            CardInstanceSearch cards = getPossibleTargetCardInstances(gameStatus);
            int targetCardId = (int) targetId;
            if (!cards.withId(targetCardId).isPresent()) {
                throw new MessageException("Selected targets were not valid.");
            }
        }
    }

    private CardInstanceSearch getPossibleTargetCardInstances(GameStatus gameStatus) {
        CardInstanceSearch cards;
        if (targetType.equals(PERMANENT)) {
            cards = new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
                    .concat(gameStatus.getNonCurrentPlayer().getBattlefield().getCards());

            if (ofType != null) {
                cards = cards.ofAnyOfTheTypes(ofType);
            }

            if (targetPowerToughnessConstraint != null) {
                cards = cards.ofTargetPowerToughnessConstraint(targetPowerToughnessConstraint);
            }

        } else if (targetType.equals(ANY)) {
            cards = new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
                    .concat(gameStatus.getNonCurrentPlayer().getBattlefield().getCards());

        } else {
            throw new RuntimeException("Missing targetType.");
        }

        if (targetControllerType == PlayerType.PLAYER) {
            cards = cards.controlledBy(gameStatus.getCurrentPlayer().getName());
        } else if (targetControllerType == PlayerType.OPPONENT) {
            cards = cards.controlledBy(gameStatus.getNonCurrentPlayer().getName());
        }

        return cards;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public List<Type> getOfType() {
        return ofType;
    }

    public String getOfSubtypeOf() {
        return ofSubtypeOf;
    }

    public AbilityType getWithAbilityType() {
        return withAbilityType;
    }

    public Color getOfColor() {
        return ofColor;
    }

    public TargetPowerToughnessConstraint getTargetPowerToughnessConstraint() {
        return targetPowerToughnessConstraint;
    }
}
