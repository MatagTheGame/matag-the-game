package com.aa.mtg.cards.model;

public class CardHelper {

    public static Card copy(Card card) {
        if (card instanceof HiddenCard) {
            return new HiddenCard();
        } else if (card instanceof FullCard) {
            return new FullCard((FullCard) card);
        }

        throw new RuntimeException("Cannot clone card of type " + card.getClass());
    }

}
