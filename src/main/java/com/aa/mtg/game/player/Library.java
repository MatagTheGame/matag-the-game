package com.aa.mtg.game.player;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CardListComponent;

import java.util.Collections;
import java.util.List;

public class Library extends CardListComponent {
    public Library() {}

    public Library(List<CardInstance> cards) {
        super(cards);
    }

    public CardInstance draw() {
        return this.cards.remove(0);
    }

    public List<CardInstance> maskedLibrary() {
        return CardInstance.mask(this.cards);
    }

    public Library shuffle() {
        Collections.shuffle(cards);
        return this;
    }
}
