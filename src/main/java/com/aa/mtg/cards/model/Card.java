package com.aa.mtg.cards.model;

import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private final String name;
    private final List<Color> colors;
    private final List<Cost> cost;
    private final List<Type> types;
    private final List<String> subtypes;
    private final String ruleText;
    private final int power;
    private final int toughness;

    public Card(String name, List<Color> colors, List<Cost> cost, List<Type> types, List<String> subtypes, String ruleText, int power, int toughness) {
        this.name = name;
        this.colors = colors;
        this.cost = cost;
        this.types = types;
        this.subtypes = subtypes;
        this.ruleText = ruleText;
        this.power = power;
        this.toughness = toughness;
    }

    public Card(Card card) {
        this(card.getName(), new ArrayList<>(card.getColors()), new ArrayList<>(card.getCost()), new ArrayList<>(card.getTypes()),
                new ArrayList<>(card.getSubtypes()), card.getRuleText(), card.getPower(), card.getToughness());
    }

    public static Card hiddenCard() {
        return new Card("card", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "", 0, 0);
    }

    public String getName() {
        return name;
    }

    public List<Color> getColors() {
        return colors;
    }

    public List<Cost> getCost() {
        return cost;
    }

    public List<Type> getTypes() {
        return types;
    }

    public List<String> getSubtypes() {
        return subtypes;
    }

    public String getRuleText() {
        return ruleText;
    }

    public int getPower() {
        return power;
    }

    public int getToughness() {
        return toughness;
    }
}
