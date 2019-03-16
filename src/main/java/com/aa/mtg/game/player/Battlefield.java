package com.aa.mtg.game.player;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CardListComponent;

import java.util.List;
import java.util.stream.Collectors;

public class Battlefield extends CardListComponent {

    public void untap() {
        cards.stream()
                .filter(cardInstance -> cardInstance.getModifiers().isTapped())
                .forEach(cardInstance -> cardInstance.getModifiers().untap());
    }

    public void removeSummoningSickness() {
        cards.stream()
                .filter(cardInstance -> cardInstance.getModifiers().isSummoningSickness())
                .forEach(cardInstance -> cardInstance.getModifiers().setSummoningSickness(false));
    }

    public void removeAttacking() {
        cards.stream()
                .filter(cardInstance -> cardInstance.getModifiers().isAttacking())
                .forEach(cardInstance -> cardInstance.getModifiers().setAttacking(false));
    }

    public List<CardInstance> getAttackingCreatures() {
        return cards.stream()
                .filter(cardInstance -> cardInstance.getModifiers().isAttacking())
                .collect(Collectors.toList());
    }

    public List<CardInstance> getBlockingCreatures() {
        return cards.stream()
                .filter(cardInstance -> cardInstance.getModifiers().isBlocking())
                .collect(Collectors.toList());
    }
}
