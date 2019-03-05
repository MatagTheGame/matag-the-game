package com.aa.mtg.game.player;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CardListComponent;

import java.util.List;

public class Hand extends CardListComponent {
    public List<CardInstance> maskedHand() {
        return CardInstance.mask(this.cards);
    }
}
