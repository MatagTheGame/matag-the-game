package com.aa.mtg.game.player;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CardListComponent;
import com.aa.mtg.cards.search.CardInstanceSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Battlefield extends CardListComponent {

    public void untap() {
        new CardInstanceSearch(cards).tapped().getCards()
                .forEach(cardInstance -> cardInstance.getModifiers().untap());
    }

    public void removeSummoningSickness() {
        new CardInstanceSearch(cards).withSummoningSickness().getCards()
                .forEach(cardInstance -> cardInstance.getModifiers().setSummoningSickness(false));
    }

    public void removeAttacking() {
        new CardInstanceSearch(cards).attacking().getCards()
                .forEach(cardInstance -> cardInstance.getModifiers().setAttacking(false));
    }

    public void removeBlocking() {
        new CardInstanceSearch(cards).blocking().getCards()
                .forEach(cardInstance -> cardInstance.getModifiers().unsetBlockingCardId());
    }

    public List<CardInstance> getAttackingCreatures() {
        return new CardInstanceSearch(cards).attacking().getCards();
    }

    public List<CardInstance> getBlockingCreatures() {
        return new CardInstanceSearch(cards).blocking().getCards();
    }

    public List<CardInstance> getBlockingCreaturesFor(int attackingCardId) {
        return getBlockingCreatures().stream()
                .filter(cardInstance -> cardInstance.getModifiers().getBlockingCardId() == attackingCardId)
                .collect(Collectors.toList());
    }
}
