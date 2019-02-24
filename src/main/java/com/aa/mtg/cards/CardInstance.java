package com.aa.mtg.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public static List<CardInstance> mask(List<CardInstance> cardInstances) {
        List<CardInstance> library = new ArrayList<>();
        for (CardInstance cardInstance : cardInstances) {
            library.add(new CardInstance(cardInstance.getId(), Card.hiddenCard()));
        }
        return library;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardInstance that = (CardInstance) o;
        return id == that.id &&
                Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, card);
    }

    @Override
    public String toString() {
        return "CardInstance{" +
                "id=" + id +
                ", card=" + card +
                '}';
    }
}
