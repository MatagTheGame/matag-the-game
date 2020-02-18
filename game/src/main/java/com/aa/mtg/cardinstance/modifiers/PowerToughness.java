package com.aa.mtg.cardinstance.modifiers;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class PowerToughness {
    private final int power;
    private final int toughness;

    public PowerToughness(int power, int toughness) {
        this.power = power;
        this.toughness = toughness;
    }

    public int getPower() {
        return power;
    }

    public int getToughness() {
        return toughness;
    }

    public static PowerToughness powerToughness(String powerToughnessString) {
        String[] powerToughness = powerToughnessString.split("/");
        return new PowerToughness(Integer.parseInt(powerToughness[0]), Integer.parseInt(powerToughness[1]));
    }
}
