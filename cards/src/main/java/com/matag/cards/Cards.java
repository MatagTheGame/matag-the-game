package com.matag.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class Cards {
  private static final String CARDS_PATH = CardsConfiguration.getResourcesPath() + "/cards";

  private Map<String, Card> CARDS = new LinkedHashMap<>();

  public Cards(ObjectMapper objectMapper) {
    String[] cardsFile = new File(CARDS_PATH).list();
    Objects.requireNonNull(cardsFile);
    for (String cardFile : cardsFile) {
      try {
        Card card = objectMapper.readValue(new File(CARDS_PATH + "/" + cardFile), Card.class);
        CARDS.put(card.getName(), card);

      } catch (Exception e) {
        throw new RuntimeException("Failed to load card: " + cardFile, e);
      }
    }
  }

  public List<Card> getAll() {
    return new ArrayList<>(CARDS.values());
  }

  public Map<String, Card> getCardsMap() {
    return CARDS;
  }

  public Card get(String name) {
    if (CARDS.containsKey(name)) {
      return CARDS.get(name);
    }
    throw new RuntimeException("Card " + name + " not found!");
  }
}
