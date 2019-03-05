package com.aa.mtg.cards;

import java.util.Objects;

public class CardModifiers {
    private boolean tapped;

    public boolean isTapped() {
        return tapped;
    }

    public void setTapped(boolean tapped) {
        this.tapped = tapped;
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
