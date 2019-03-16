package com.aa.mtg.cards;

import com.aa.mtg.cards.modifiers.TappedModifiers;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class CardModifiers {
    private TappedModifiers tapped;
    private boolean summoningSickness;
    private boolean attacking;
    private boolean blocking;

    public TappedModifiers getTapped() {
        return tapped;
    }

    public boolean isTapped() {
        return tapped != null;
    }

    public boolean isSummoningSickness() {
        return summoningSickness;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public boolean isBlocking() {
        return blocking;
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

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }
}
