package com.matag.cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.cards.ability.Ability;
import com.matag.cards.properties.*;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.TreeSet;

@Value
@JsonDeserialize(builder = Card.CardBuilder.class)
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
  private final String name;
  private final CardImageUrls imageUrls;
  private final TreeSet<Color> colors;
  private final List<Cost> cost;
  private final TreeSet<Type> types;
  private final TreeSet<Subtype> subtypes;
  private final Rarity rarity;
  private final String ruleText;
  private final int power;
  private final int toughness;
  private final List<Ability> abilities;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CardBuilder {

  }
}
