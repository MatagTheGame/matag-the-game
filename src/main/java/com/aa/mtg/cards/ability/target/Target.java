package com.aa.mtg.cards.ability.target;

import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.player.PlayerType;
import lombok.Builder;

import java.util.List;

@Builder
public class Target {
    private final TargetType targetType;
    private final List<Type> ofType;
    private final List<Type> notOfType;
    private final String ofSubtypeOf;
    private final AbilityType withAbilityType;
    private final Color ofColor;
    private final TargetPowerToughnessConstraint targetPowerToughnessConstraint;
    private final PlayerType targetControllerType;
    private final List<TargetStatusType> targetStatusTypes;
    private final boolean another;

    private Target(TargetType targetType, List<Type> ofType, List<Type> notOfType, String ofSubtypeOf, AbilityType withAbilityType, Color ofColor, TargetPowerToughnessConstraint targetPowerToughnessConstraint, PlayerType targetControllerType, List<TargetStatusType> targetStatusTypes, boolean another) {
        this.targetType = targetType;
        this.ofType = ofType;
        this.notOfType = notOfType;
        this.ofSubtypeOf = ofSubtypeOf;
        this.withAbilityType = withAbilityType;
        this.ofColor = ofColor;
        this.targetPowerToughnessConstraint = targetPowerToughnessConstraint;
        this.targetControllerType = targetControllerType;
        this.targetStatusTypes = targetStatusTypes;
        this.another = another;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public List<Type> getOfType() {
        return ofType;
    }

    public List<Type> getNotOfType() {
        return notOfType;
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

    public PlayerType getTargetControllerType() {
        return targetControllerType;
    }

    public List<TargetStatusType> getTargetStatusTypes() {
        return targetStatusTypes;
    }

    public boolean isAnother() {
        return another;
    }
}
