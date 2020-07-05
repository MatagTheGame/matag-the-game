package com.matag.game.cardinstance;

import java.util.ArrayList;
import java.util.List;

public abstract class CardListComponent {
  protected List<CardInstance> cards = new ArrayList<>();

  public CardInstanceSearch search() {
    return new CardInstanceSearch(cards);
  }

  public List<CardInstance> getCards() {
    return cards;
  }

  public void setCards(List<CardInstance> cards) {
    this.cards = cards;
  }

  public int size() {
    return cards.size();
  }

  public void addCard(CardInstance cardInstance) {
    this.cards.add(cardInstance);
  }

  public void addCards(List<CardInstance> cardInstance) {
    this.cards.addAll(cardInstance);
  }

  public boolean hasCardById(int cardId) {
    return search().withId(cardId).isPresent();
  }

  public CardInstance findCardById(int cardId) {
    return search().withId(cardId).orElseThrow(() -> new RuntimeException("Card with id " + cardId + " not found."));
  }

  public CardInstance extractCardById(int cardId) {
    CardInstance cardInstance = findCardById(cardId);
    this.cards.remove(cardInstance);
    return cardInstance;
  }

  public ArrayList<CardInstance> extractAllCards() {
    ArrayList<CardInstance> removedCards = new ArrayList<>();
    while (!cards.isEmpty()) {
      removedCards.add(cards.remove(0));
    }
    return removedCards;
  }

}
