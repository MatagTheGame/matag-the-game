package com.aa.mtg.cards;

public class CardInstance {

    private final int id;
    private final Card card;

    public CardInstance(int id, Card card) {
        this.id = id;
        this.card = card;
    }

    public CardInstance(CardInstance cardInstance) {
        this(cardInstance.getId(), cardInstance.getCard());
    }

    public int getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }
}
