package com.matag.cards.properties;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = CardImageUrls.CardImageUrlsBuilder.class)
@Builder(toBuilder = true)
public class CardImageUrls {
  private final String lowResolution;
  private final String highResolution;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CardImageUrlsBuilder {

  }
}
