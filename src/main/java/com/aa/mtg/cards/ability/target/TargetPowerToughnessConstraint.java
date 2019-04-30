package com.aa.mtg.cards.ability.target;

import com.aa.mtg.cards.CardInstance;

public class TargetPowerToughnessConstraint {

    public enum PowerOrToughness {
        POWER, TOUGHNESS
    }

    private final PowerOrToughness powerOrToughness;
    private final TargetSelectionConstraint targetSelectionConstraint;
    private final int value;

    public TargetPowerToughnessConstraint(PowerOrToughness powerOrToughness, TargetSelectionConstraint targetSelectionConstraint, int value) {
        this.powerOrToughness = powerOrToughness;
        this.targetSelectionConstraint = targetSelectionConstraint;
        this.value = value;
    }

    public boolean check(CardInstance cardInstance) {
        int value = getValue(cardInstance);

        switch (targetSelectionConstraint) {
            case EQUAL:
                return value == this.value;

            case LESS:
                return value < this.value;

            case LESS_OR_EQUAL:
                return value <= this.value;

            case GREATER:
                return value > this.value;

            case GREATER_OR_EQUAL:
                return value >= this.value;

            default:
                throw new RuntimeException("targetSelectionConstraint is null");
        }
    }

    public PowerOrToughness getPowerOrToughness() {
        return powerOrToughness;
    }

    public TargetSelectionConstraint getTargetSelectionConstraint() {
        return targetSelectionConstraint;
    }

    public int getValue() {
        return value;
    }

    private int getValue(CardInstance cardInstance) {
        switch (powerOrToughness) {
            case POWER:
                return cardInstance.getPower();
            case TOUGHNESS:
                return cardInstance.getToughness();
            default:
                throw new RuntimeException("powerOrToughness is null");
        }
    }
}
