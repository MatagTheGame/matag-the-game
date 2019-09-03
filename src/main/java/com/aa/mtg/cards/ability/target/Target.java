package com.aa.mtg.cards.ability.target;

import com.aa.mtg.cards.selector.CardInstanceSelector;

public class Target {
    private final CardInstanceSelector cardInstanceSelector;

    public Target(CardInstanceSelector cardInstanceSelector) {
        this.cardInstanceSelector = cardInstanceSelector;
    }

    public CardInstanceSelector getCardInstanceSelector() {
        return cardInstanceSelector;
    }
}
