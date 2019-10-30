package com.aa.mtg.cards;

import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Rarity;
import com.aa.mtg.cards.properties.Subtype;
import com.aa.mtg.cards.properties.Type;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Set;

public class Card {
  private final String name;
  private final Set<Color> colors;
  private final List<Cost> cost;
  private final Set<Type> types;
  private final Set<Subtype> subtypes;
  private final Rarity rarity;
  private final String ruleText;
  private final int power;
  private final int toughness;

  @JsonCreator
  public Card(@JsonProperty("name") String name,
      @JsonProperty("colors") Set<Color> colors, @JsonProperty("cost") List<Cost> cost,
      @JsonProperty("types") Set<Type> types, @JsonProperty("subtypes") Set<Subtype> subtypes,
      @JsonProperty("rarity") Rarity rarity, @JsonProperty("ruleText") String ruleText,
      @JsonProperty("power") int power, @JsonProperty("toughness") int toughness) {
    this.name = name;
    this.colors = colors;
    this.cost = cost;
    this.types = types;
    this.subtypes = subtypes;
    this.rarity = rarity;
    this.ruleText = ruleText;
    this.power = power;
    this.toughness = toughness;
  }

  public String getName() {
    return name;
  }

  public Set<Color> getColors() {
    return colors;
  }

  public List<Cost> getCost() {
    return cost;
  }

  public Set<Type> getTypes() {
    return types;
  }

  public Set<Subtype> getSubtypes() {
    return subtypes;
  }

  public Rarity getRarity() {
    return rarity;
  }

  public String getRuleText() {
    return ruleText;
  }

  public int getPower() {
    return power;
  }

  public int getToughness() {
    return toughness;
  }
}
