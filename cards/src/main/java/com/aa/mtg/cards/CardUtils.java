package com.aa.mtg.cards;

import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;

import java.util.List;

import static com.aa.mtg.cards.properties.Cost.COLORLESS;

public class CardUtils {
  public static boolean isColorless(Card card) {
    return card.getCost().stream().noneMatch(cost -> cost != COLORLESS);
  }

  public static boolean isOfType(Card card, Type type) {
    return card.getTypes().contains(type);
  }

  public static boolean isNotOfType(Card card, Type type) {
    return !isOfType(card, type);
  }

  public static boolean isOfColor(Card card, Color color) {
    return card.getColors().contains(color);
  }

  public static boolean ofAnyOfTheColors(Card card, List<Color> colors) {
    for (Color color : colors) {
      if (isOfColor(card, color)) {
        return true;
      }
    }
    return false;
  }
}
