package com.aa.mtg.cards.search;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CardSearch {

    private final List<Card> cards;

    public CardSearch(List<Card> cards) {
        this.cards = cards;
    }

    public CardSearch ofType(Type type) {
        List<Card> cards = this.cards.stream()
                .filter(card -> card.isOfType(type))
                .collect(toList());
        return new CardSearch(cards);
    }

    public CardSearch ofColor(Color color) {
        List<Card> cards = this.cards.stream()
                .filter(card -> card.isOfColor(color))
                .collect(toList());
        return new CardSearch(cards);
    }

    public CardSearch ofAnyOfTheColors(List<Color> colors) {
        List<Card> cards = this.cards.stream()
                .filter(card -> card.ofAnyOfTheColors(colors))
                .collect(toList());
        return new CardSearch(cards);
    }

    public CardSearch concat(List<Card> moreCards) {
        List<Card> cards = Stream.concat(this.cards.stream(), moreCards.stream()).collect(toList());
        return new CardSearch(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public boolean isNotEmpty() {
        return !cards.isEmpty();
    }

    public List<Card> getCards() {
        return cards;
    }
}
