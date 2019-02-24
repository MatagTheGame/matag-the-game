package com.aa.mtg.game.player;

import com.aa.mtg.cards.CardInstance;

import java.util.ArrayList;
import java.util.List;

public class Battlefield {
    private List<CardInstance> cards;

    public Battlefield() {
        this.cards = new ArrayList<>();
    }

    public List<CardInstance> getCards() {
        return cards;
    }

    public void addCard(CardInstance cardInstance) {
        this.cards.add(cardInstance);
    }
}
