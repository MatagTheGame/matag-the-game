package com.aa.mtg.player;

import com.aa.mtg.cards.model.CardInstance;

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
}
