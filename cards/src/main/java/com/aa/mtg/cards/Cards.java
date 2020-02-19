package com.aa.mtg.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.aa.mtg.cards.CardsConfiguration.RESOURCES_PATH;

@Component
public class Cards {
  private static final String CARDS_PATH = RESOURCES_PATH + "/cards";

  private Map<String, Card> CARDS = new LinkedHashMap<>();

  public Cards(ObjectMapper objectMapper) throws Exception {
    String[] cardsFile = new File(CARDS_PATH).list();
    Objects.requireNonNull(cardsFile);
    for (String cardFile : cardsFile) {
      Card card = objectMapper.readValue(new File(CARDS_PATH + "/" + cardFile), Card.class);
      CARDS.put(card.getName(), card);
    }
  }

  public Map<String, Card> getCards() {
    return CARDS;
  }

  public Card getCard(String name) {
    return CARDS.get(name);
  }
}
