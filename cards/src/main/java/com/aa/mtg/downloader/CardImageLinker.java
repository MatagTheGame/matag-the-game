package com.aa.mtg.downloader;

import com.aa.mtg.cards.Card;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

public class CardImageLinker {
  private final String name;

  public CardImageLinker(Card card) {
    this.name = card.getName();
  }

  @SneakyThrows
  public String getImageUrl() {
    String file = readHttpResource("https://api.scryfall.com/cards/named?fuzzy=" + URLEncoder.encode(name, "UTF-8"));
    JsonNode jsonNode = new ObjectMapper().readTree(file);
    return jsonNode.path("image_uris").path("png").asText();
  }

  @SneakyThrows
  private String readHttpResource(String url) {
    return new BufferedReader(new InputStreamReader(new URL(url).openStream()))
        .lines().collect(Collectors.joining("\n"));
  }
}
