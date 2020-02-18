package com.aa.mtg.cards;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.properties.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@EqualsAndHashCode
@ToString
@Builder
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
  private final List<Ability> abilities;

  @JsonCreator
  public Card(@JsonProperty("name") String name,
      @JsonProperty("colors") Set<Color> colors, @JsonProperty("cost") List<Cost> cost,
      @JsonProperty("types") Set<Type> types, @JsonProperty("subtypes") Set<Subtype> subtypes,
      @JsonProperty("rarity") Rarity rarity, @JsonProperty("ruleText") String ruleText,
      @JsonProperty("power") int power, @JsonProperty("toughness") int toughness,
      @JsonProperty("abilities") List<Ability> abilities) {
    this.name = name;
    this.colors = colors;
    this.cost = cost;
    this.types = types;
    this.subtypes = subtypes;
    this.rarity = rarity;
    this.ruleText = ruleText;
    this.power = power;
    this.toughness = toughness;
    this.abilities = abilities;
  }
}
