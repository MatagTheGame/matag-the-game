package com.aa.mtg.cards;

import com.aa.mtg.cards.modifiers.TappedModifiers;

import java.util.Objects;

public class CardModifiers {
    private TappedModifiers tapped;

    public TappedModifiers getTapped() {
        return tapped;
    }

    public boolean isTapped() {
        return tapped != null;
    }

    public void tap() {
        this.tapped = TappedModifiers.TAPPED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardModifiers that = (CardModifiers) o;
        return tapped == that.tapped;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tapped);
    }

    @Override
    public String toString() {
        return "CardModifiers{" +
                "tapped=" + tapped +
                '}';
    }
}
