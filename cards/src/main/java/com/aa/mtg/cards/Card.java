package com.aa.mtg.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {
  private final String name;

  @JsonCreator
  public Card(@JsonProperty("name") String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
