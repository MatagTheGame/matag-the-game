package com.aa.mtg.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.aa.mtg.cards.CardsConfiguration.getResourcesPath;

@Component
public class Cards {
  private static final String CARDS_PATH = getResourcesPath() + "/cards";
  private static final Logger LOGGER = Logger.getLogger(Cards.class.getName());

  private Map<String, Card> CARDS = new LinkedHashMap<>();

  public Cards(ObjectMapper objectMapper) {
    LOGGER.log(Level.INFO, "Current Directory: " + new File("").getAbsolutePath());
    LOGGER.log(Level.INFO, "Cards Path: " + CARDS_PATH);
    LOGGER.log(Level.INFO, "ls: " + new File("").list());
    String[] cardsFile = new File(CARDS_PATH).list();
    Objects.requireNonNull(cardsFile);
    for (String cardFile : cardsFile) {
      try {
        Card card = objectMapper.readValue(new File(CARDS_PATH + "/" + cardFile), Card.class);
        CARDS.put(card.getName(), card);

      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "", e);
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
