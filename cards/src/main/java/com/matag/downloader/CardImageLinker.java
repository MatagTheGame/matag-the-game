package com.matag.downloader;

import com.matag.cards.Card;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

public class CardImageLinker {
  private final String lowResolution;
  private final String highResolution;

  @SneakyThrows
  public CardImageLinker(Card card) {
    String file = readHttpResource("https://api.scryfall.com/cards/named?fuzzy=" + URLEncoder.encode(card.getName(), "UTF-8"));
    JsonNode jsonNode = new ObjectMapper().readTree(file);
    lowResolution = jsonNode.path("image_uris").path("normal").asText();
    highResolution = jsonNode.path("image_uris").path("png").asText();
  }

  public String getLowResolution() {
    return lowResolution;
  }

  public String getHighResolution() {
    return highResolution;
  }

  @SneakyThrows
  private String readHttpResource(String url) {
    return new BufferedReader(new InputStreamReader(new URL(url).openStream()))
      .lines().collect(Collectors.joining("\n"));
  }
}
