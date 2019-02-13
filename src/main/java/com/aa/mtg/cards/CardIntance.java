package com.aa.mtg.cards;

import lombok.Data;

@Data
public class CardIntance {
    private int id;
    private Card card;

    public CardIntance(int id, Card card) {
        this.id = id;
        this.card = card;
    }
}
