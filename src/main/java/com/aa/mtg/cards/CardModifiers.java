package com.aa.mtg.cards;

import com.aa.mtg.cards.modifiers.TappedModifiers;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class CardModifiers {
    private TappedModifiers tapped;
    private boolean summoningSickness;
    private boolean attacking;
    private List<Integer> blocking = new ArrayList<>();

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

    public List<Integer> getBlocking() {
        return blocking;
    }

    public boolean isBlocking() {
        return !blocking.isEmpty();
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

    public void setBlocking(List<Integer> blocking) {
        this.blocking = blocking;
    }

    public void addBlocking(int blocking) {
        this.blocking.add(blocking);
    }
}
