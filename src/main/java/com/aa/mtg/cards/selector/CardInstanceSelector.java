package com.aa.mtg.cards.selector;

import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Subtype;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.player.PlayerType;
import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
public class CardInstanceSelector {
    private final SelectorType selectorType;
    private final List<Type> ofType;
    private final List<Type> notOfType;
    private final List<Subtype> ofSubtypeOf;
    private final AbilityType withAbilityType;
    private final Color ofColor;
    private final PowerToughnessConstraint powerToughnessConstraint;
    private final PlayerType controllerType;
    private final List<StatusType> statusTypes;
    private final boolean others;

    private CardInstanceSelector(SelectorType selectorType, List<Type> ofType, List<Type> notOfType, List<Subtype> ofSubtypeOf, AbilityType withAbilityType, Color ofColor, PowerToughnessConstraint powerToughnessConstraint, PlayerType controllerType, List<StatusType> statusTypes, boolean others) {
        this.selectorType = selectorType;
        this.ofType = ofType;
        this.notOfType = notOfType;
        this.ofSubtypeOf = ofSubtypeOf;
        this.withAbilityType = withAbilityType;
        this.ofColor = ofColor;
        this.powerToughnessConstraint = powerToughnessConstraint;
        this.controllerType = controllerType;
        this.statusTypes = statusTypes;
        this.others = others;
    }

    public SelectorType getSelectorType() {
        return selectorType;
    }

    public List<Type> getOfType() {
        return ofType;
    }

    public List<Type> getNotOfType() {
        return notOfType;
    }

    public List<Subtype> getOfSubtypeOf() {
        return ofSubtypeOf;
    }

    public AbilityType getWithAbilityType() {
        return withAbilityType;
    }

    public Color getOfColor() {
        return ofColor;
    }

    public PowerToughnessConstraint getPowerToughnessConstraint() {
        return powerToughnessConstraint;
    }

    public PlayerType getControllerType() {
        return controllerType;
    }

    public List<StatusType> getStatusTypes() {
        return statusTypes;
    }

    public boolean isOthers() {
        return others;
    }

    public String getText() {
        StringBuilder stringBuilder = new StringBuilder();

        if (selectorType == SelectorType.PERMANENT) {

            if (others) {
                stringBuilder.append("other ");
            }

            if (ofType != null) {
                stringBuilder.append(ofType.stream().map(Objects::toString).collect(Collectors.joining(", "))).append("s ");
            }

            if (controllerType != null) {
                if (controllerType == PlayerType.PLAYER) {
                    stringBuilder.append("you control ");
                } else {
                    stringBuilder.append("opponent controls");
                }
            }

        }

        String str = stringBuilder.toString();
        str = str.toLowerCase();
        str = str.isEmpty() ? str : str.substring(0, 1).toUpperCase() + str.substring(1);

        return str.trim();
    }
}
