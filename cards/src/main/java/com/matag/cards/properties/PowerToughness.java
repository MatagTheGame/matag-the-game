package com.matag.cards.properties;

import lombok.Data;

@Data
public class PowerToughness {
  private final int power;
  private final int toughness;

  public PowerToughness(int power, int toughness) {
    this.power = power;
    this.toughness = toughness;
  }

  public static PowerToughness powerToughness(String powerToughnessString) {
    String[] powerToughness = powerToughnessString.split("/");
    return new PowerToughness(Integer.parseInt(powerToughness[0]), Integer.parseInt(powerToughness[1]));
  }
}
