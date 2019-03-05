package com.aa.mtg.cards;

import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public boolean isInstantSpeed() {
        return types.contains(Type.INSTANT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return power == card.power &&
                toughness == card.toughness &&
                Objects.equals(name, card.name) &&
                Objects.equals(colors, card.colors) &&
                Objects.equals(cost, card.cost) &&
                Objects.equals(types, card.types) &&
                Objects.equals(subtypes, card.subtypes) &&
                Objects.equals(ruleText, card.ruleText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, colors, cost, types, subtypes, ruleText, power, toughness);
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", colors=" + colors +
                ", cost=" + cost +
                ", types=" + types +
                ", subtypes=" + subtypes +
                ", ruleText='" + ruleText + '\'' +
                ", power=" + power +
                ", toughness=" + toughness +
                '}';
    }
}
