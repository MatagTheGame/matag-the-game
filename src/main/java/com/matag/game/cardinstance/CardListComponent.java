package com.matag.game.cardinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

  public List<CardInstance> extractCardsByIds(List<Integer> cardIds) {
    return cardIds.stream()
      .map(this::findCardById)
      .peek(cardInstance -> cards.remove(cardInstance))
      .collect(Collectors.toList());
  }

  public CardInstance extractCardById(int cardId) {
    return extractCardsByIds(List.of(cardId)).get(0);
  }

  public ArrayList<CardInstance> extractAllCards() {
    var removedCards = new ArrayList<CardInstance>();
    while (!cards.isEmpty()) {
      removedCards.add(cards.remove(0));
    }
    return removedCards;
  }

}
