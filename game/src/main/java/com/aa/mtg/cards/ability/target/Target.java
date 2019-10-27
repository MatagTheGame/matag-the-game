package com.aa.mtg.cards.ability.target;

import com.aa.mtg.cards.selector.CardInstanceSelector;
import lombok.Builder;

@Builder
public class Target {
    private final CardInstanceSelector cardInstanceSelector;
    private boolean optional;
    private boolean other;

    public CardInstanceSelector getCardInstanceSelector() {
        return cardInstanceSelector;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean isOther() {
        return other;
    }
}
