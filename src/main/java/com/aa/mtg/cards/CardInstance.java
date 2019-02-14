package com.aa.mtg.cards;

import lombok.Data;

@Data
public class CardInstance {

    private int id;
    private Card card;

    public CardInstance(int id, Card card) {
        this.id = id;
        this.card = card;
    }

    public CardInstance(CardInstance cardInstance) {
        this(cardInstance.getId(), new Card(cardInstance.getCard()));
    }
}
