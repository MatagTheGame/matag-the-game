package com.aa.mtg.game.player;

import com.aa.mtg.cards.CardInstance;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<CardInstance> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public List<CardInstance> getCards() {
        return cards;
    }

    public CardInstance findCardById(int cardId) {
        return cards.stream()
                .filter(cardInstance -> cardInstance.getId() == cardId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Card with id " + cardId + " not found in player hand."));
    }

    public CardInstance extractCardById(int cardId) {
        CardInstance cardInstance = findCardById(cardId);
        this.cards.remove(cardInstance);
        return cardInstance;
    }
}
