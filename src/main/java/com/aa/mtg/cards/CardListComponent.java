package com.aa.mtg.cards;

import com.aa.mtg.cards.search.CardSearch;

import java.util.ArrayList;
import java.util.List;

public abstract class CardListComponent {
    protected List<CardInstance> cards;

    public CardListComponent() {
        this.cards = new ArrayList<>();
    }

    public CardListComponent(List<CardInstance> cards) {
        this.cards = cards;
    }

    public List<CardInstance> getCards() {
        return cards;
    }

    public int size() {
        return cards.size();
    }

    public void addCard(CardInstance cardInstance) {
        this.cards.add(cardInstance);
    }

    public boolean hasCardById(int cardId) {
        return new CardSearch(cards).withId(cardId).isPresent();
    }

    public CardInstance findCardById(int cardId) {
        return new CardSearch(cards).withId(cardId)
                .orElseThrow(() -> new RuntimeException("Card with id " + cardId + " not found."));
    }

    public CardInstance extractCardById(int cardId) {
        CardInstance cardInstance = findCardById(cardId);
        this.cards.remove(cardInstance);
        return cardInstance;
    }

}
