package com.aa.mtg.cards.search;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint;
import com.aa.mtg.cards.properties.Type;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CardInstanceSearch {

    private final List<CardInstance> cards;

    public CardInstanceSearch(List<CardInstance> cards) {
        this.cards = cards;
    }

    public Optional<CardInstance> withId(int cardId) {
        return cards.stream()
                .filter(cardInstance -> cardInstance.getId() == cardId)
                .findFirst();
    }

    public CardInstanceSearch ofType(Type type) {
        List<CardInstance> cards = this.cards.stream()
                .filter(cardInstance -> cardInstance.isOfType(type))
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch ofAnyOfTheTypes(List<Type> types) {
        List<CardInstance> cards = this.cards.stream()
                .filter(cardInstance -> cardInstance.ofAnyOfTheTypes(types))
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch tapped() {
        List<CardInstance> cards = this.cards.stream()
                .filter(cardInstance -> cardInstance.getModifiers().isTapped())
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch untapped() {
        List<CardInstance> cards = this.cards.stream()
                .filter(cardInstance -> cardInstance.getModifiers().isUntapped())
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch withSummoningSickness() {
        List<CardInstance> cards = this.cards.stream()
                .filter(cardInstance -> cardInstance.getModifiers().isSummoningSickness())
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch withoutSummoningSickness() {
        List<CardInstance> cards = this.cards.stream()
                .filter(cardInstance -> !cardInstance.getModifiers().isSummoningSickness())
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch attacking() {
        List<CardInstance> cards = this.cards.stream()
                .filter(cardInstance -> cardInstance.getModifiers().isAttacking())
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch blocking() {
        List<CardInstance> cards = this.cards.stream()
                .filter(cardInstance -> cardInstance.getModifiers().isBlocking())
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch blockingCreatureFor(int attackingCardId) {
        List<CardInstance> cards = blocking().getCards().stream()
                .filter(cardInstance -> cardInstance.getModifiers().getBlocking().contains(attackingCardId))
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch ofTargetPowerToughnessConstraint(TargetPowerToughnessConstraint targetPowerToughnessConstraint) {
        List<CardInstance> cards = this.cards.stream()
                .filter(targetPowerToughnessConstraint::check)
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch concat(List<CardInstance> moreCards) {
        List<CardInstance> cards = Stream.concat(this.cards.stream(), moreCards.stream()).collect(toList());
        return new CardInstanceSearch(cards);
    }

    public CardInstanceSearch controlledBy(String playerName) {
        List<CardInstance> cards = this.cards.stream()
                .filter(cardInstance -> cardInstance.getController().equals(playerName))
                .collect(toList());
        return new CardInstanceSearch(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public boolean isNotEmpty() {
        return !cards.isEmpty();
    }

    public List<CardInstance> getCards() {
        return cards;
    }

    public CardInstanceSearch attachedToId(int attachedToId) {
        List<CardInstance> cards = this.cards.stream()
                .filter((cardInstance -> cardInstance.getModifiers().getAttachedToId() == attachedToId))
                .collect(Collectors.toList());
        return new CardInstanceSearch(cards);
    }
}
