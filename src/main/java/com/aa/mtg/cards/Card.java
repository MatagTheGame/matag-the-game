package com.aa.mtg.cards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Card {
    public static String HIDDEN_CARD_NAME = "card";

    private String name;
    private List<Color> colors;
    private List<Cost> cost;
    private List<Type> types;
    private List<String> subtypes;
    private String ruleText;
    private int power;
    private int toughness;

    public Card(String name) {
        this.name = name;
    }

    public Card(Card card) {
        this(card.getName(), new ArrayList<>(card.getColors()), new ArrayList<>(card.getCost()), new ArrayList<>(card.getTypes()),
                new ArrayList<>(card.getSubtypes()), card.getRuleText(), card.getPower(), card.getToughness());
    }

    public static Card hiddenCard() {
        return new Card(HIDDEN_CARD_NAME);
    }
}
