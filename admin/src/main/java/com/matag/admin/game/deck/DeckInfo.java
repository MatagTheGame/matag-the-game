package com.matag.admin.game.deck;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.cards.properties.CardImageUrls;
import com.matag.cards.properties.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = DeckInfo.DeckInfoBuilder.class)
@Builder(toBuilder = true)
public class DeckInfo {
  private final Set<Color> randomColors;

  @JsonPOJOBuilder(withPrefix = "")
  public static class DeckInfoBuilder {

  }
}
