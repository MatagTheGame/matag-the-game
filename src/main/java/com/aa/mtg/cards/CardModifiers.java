package com.aa.mtg.cards;

import com.aa.mtg.cards.modifiers.TappedModifiers;

import java.util.Objects;

public class CardModifiers {
    private TappedModifiers tapped;
    private boolean summoningSickness;

    public TappedModifiers getTapped() {
        return tapped;
    }

    public boolean isSummoningSickness() {
        return summoningSickness;
    }

    public boolean isTapped() {
        return tapped != null;
    }

    public void tap() {
        this.tapped = TappedModifiers.TAPPED;
    }

    public void untap() {
        this.tapped = null;
    }

    public void setSummoningSickness(boolean summoningSickness) {
        this.summoningSickness = summoningSickness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardModifiers that = (CardModifiers) o;
        return summoningSickness == that.summoningSickness &&
                tapped == that.tapped;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tapped, summoningSickness);
    }

    @Override
    public String toString() {
        return "CardModifiers{" +
                "tapped=" + tapped +
                ", summoningSickness=" + summoningSickness +
                '}';
    }
}
