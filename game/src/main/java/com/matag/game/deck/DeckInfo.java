package com.matag.game.deck;

import com.matag.cards.properties.Color;
import lombok.Value;

import java.util.Set;

@Value
public class DeckInfo {
  private final Set<Color> randomColors;
}
