package com.aa.mtg.cards.ability.target;

import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.cards.search.CardSearch;
import com.aa.mtg.game.message.MessageException;
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

    public Target(TargetType targetType, List<Type> ofType, String ofSubtypeOf, AbilityType withAbilityType, Color ofColor, TargetPowerToughnessConstraint targetPowerToughnessConstraint) {
        this.targetType = targetType;
        this.ofType = ofType;
        this.ofSubtypeOf = ofSubtypeOf;
        this.withAbilityType = withAbilityType;
        this.ofColor = ofColor;
        this.targetPowerToughnessConstraint = targetPowerToughnessConstraint;
    }

    public void check(GameStatus gameStatus, int targetCardId) {
        CardSearch cards;

        if (targetType.equals(PERMANENT)) {
            cards = new CardSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
                    .concat(gameStatus.getNonCurrentPlayer().getBattlefield().getCards());

            if (ofType != null) {
                cards = cards.ofAnyOfTheTypes(ofType);
            }

            if (targetPowerToughnessConstraint != null) {
                cards = cards.ofTargetPowerToughnessConstraint(targetPowerToughnessConstraint);
            }

        } else if (targetType.equals(ANY)) {
            cards = new CardSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
                    .concat(gameStatus.getNonCurrentPlayer().getBattlefield().getCards());

        } else {
            throw new RuntimeException("Missing targetType.");
        }

        if (!cards.withId(targetCardId).isPresent()) {
            throw new MessageException("Selected targets were not valid.");
        }
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
