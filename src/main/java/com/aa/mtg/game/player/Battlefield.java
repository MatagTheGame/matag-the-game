package com.aa.mtg.game.player;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CardListComponent;
import com.aa.mtg.cards.search.CardSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Battlefield extends CardListComponent {

    public void untap() {
        new CardSearch(cards).tapped().getCards()
                .forEach(cardInstance -> cardInstance.getModifiers().untap());
    }

    public void removeSummoningSickness() {
        new CardSearch(cards).withoutSummoningSickness().getCards()
                .forEach(cardInstance -> cardInstance.getModifiers().setSummoningSickness(false));
    }

    public void removeAttacking() {
        new CardSearch(cards).attacking().getCards()
                .forEach(cardInstance -> cardInstance.getModifiers().setAttacking(false));
    }

    public void removeBlocking() {
        new CardSearch(cards).blocking().getCards()
                .forEach(cardInstance -> cardInstance.getModifiers().setBlocking(new ArrayList<>()));
    }

    public List<CardInstance> getAttackingCreatures() {
        return new CardSearch(cards).attacking().getCards();
    }

    public List<CardInstance> getBlockingCreatures() {
        return new CardSearch(cards).blocking().getCards();
    }

    public List<CardInstance> getBlockingCreaturesFor(int attackingCardId) {
        return getBlockingCreatures().stream()
                .filter(cardInstance -> cardInstance.getModifiers().getBlocking().contains(attackingCardId))
                .collect(Collectors.toList());
    }
}
