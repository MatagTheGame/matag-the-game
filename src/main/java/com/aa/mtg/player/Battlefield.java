package com.aa.mtg.player;

import com.aa.mtg.cards.model.CardInstance;

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
}
