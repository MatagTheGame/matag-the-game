package com.aa.mtg.cards.selector;

import com.aa.mtg.cards.CardInstance;

public class PowerToughnessConstraint {

    public enum PowerOrToughness {
        POWER, TOUGHNESS
    }

    private final PowerOrToughness powerOrToughness;
    private final PowerToughnessConstraintType powerToughnessConstraintType;
    private final int value;

    public PowerToughnessConstraint(PowerOrToughness powerOrToughness, PowerToughnessConstraintType powerToughnessConstraintType, int value) {
        this.powerOrToughness = powerOrToughness;
        this.powerToughnessConstraintType = powerToughnessConstraintType;
        this.value = value;
    }

    public boolean check(CardInstance cardInstance) {
        int value = getValue(cardInstance);

        switch (powerToughnessConstraintType) {
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
                throw new RuntimeException("powerToughnessConstraintType is null");
        }
    }

    public PowerOrToughness getPowerOrToughness() {
        return powerOrToughness;
    }

    public PowerToughnessConstraintType getPowerToughnessConstraintType() {
        return powerToughnessConstraintType;
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
