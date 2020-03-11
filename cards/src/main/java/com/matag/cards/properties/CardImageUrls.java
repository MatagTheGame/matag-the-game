package com.matag.cards.properties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CardImageUrls {
  private final String lowResolution;
  private final String highResolution;

  @JsonCreator
  public CardImageUrls(@JsonProperty("lowResolution") String lowResolution, @JsonProperty("highResolution") String highResolution) {
    this.lowResolution = lowResolution;
    this.highResolution = highResolution;
  }
}
