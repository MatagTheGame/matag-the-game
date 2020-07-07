package com.matag.game.cardinstance.ability.selector;

import com.matag.cards.ability.selector.PowerToughnessConstraint;
import com.matag.game.cardinstance.CardInstance;

public class PowerToughnessConstraintUtils {
  public static boolean check(PowerToughnessConstraint powerToughnessConstraint, CardInstance cardInstance) {
    var value = getValue(powerToughnessConstraint.getPowerOrToughness(), cardInstance);

    return switch (powerToughnessConstraint.getPowerToughnessConstraintType()) {
      case EQUAL -> value == powerToughnessConstraint.getValue();
      case LESS -> value < powerToughnessConstraint.getValue();
      case LESS_OR_EQUAL -> value <= powerToughnessConstraint.getValue();
      case GREATER -> value > powerToughnessConstraint.getValue();
      case GREATER_OR_EQUAL -> value >= powerToughnessConstraint.getValue();
    };
  }

  private static int getValue(PowerToughnessConstraint.PowerOrToughness powerOrToughness, CardInstance cardInstance) {
    return switch (powerOrToughness) {
      case POWER -> cardInstance.getPower();
      case TOUGHNESS -> cardInstance.getToughness();
    };
  }
}
