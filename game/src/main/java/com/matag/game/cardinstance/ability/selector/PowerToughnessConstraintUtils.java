package com.matag.game.cardinstance.ability.selector;

import com.matag.game.cardinstance.CardInstance;
import com.matag.cards.ability.selector.PowerToughnessConstraint;

public class PowerToughnessConstraintUtils {
  public static boolean check(PowerToughnessConstraint powerToughnessConstraint, CardInstance cardInstance) {
    int value = getValue(powerToughnessConstraint.getPowerOrToughness(), cardInstance);

    switch (powerToughnessConstraint.getPowerToughnessConstraintType()) {
      case EQUAL:
        return value == powerToughnessConstraint.getValue();

      case LESS:
        return value < powerToughnessConstraint.getValue();

      case LESS_OR_EQUAL:
        return value <= powerToughnessConstraint.getValue();

      case GREATER:
        return value > powerToughnessConstraint.getValue();

      case GREATER_OR_EQUAL:
        return value >= powerToughnessConstraint.getValue();

      default:
        throw new RuntimeException("powerToughnessConstraintType is null");
    }
  }

  private static int getValue(PowerToughnessConstraint.PowerOrToughness powerOrToughness, CardInstance cardInstance) {
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
