package com.aa.mtg.cards;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.properties.Cost.COLORLESS;
import static java.util.Collections.emptyList;

@ToString
@EqualsAndHashCode
public class Card {
    private final String name;
    private final List<Color> colors;
    private final List<Cost> cost;
    private final List<Type> types;
    private final List<String> subtypes;
    private final String ruleText;
    private final int power;
    private final int toughness;
    private final List<Ability> abilities;

    public Card(String name, List<Color> colors, List<Cost> cost, List<Type> types, List<String> subtypes, String ruleText, int power, int toughness, List<Ability> abilities) {
        this.name = name;
        this.colors = colors;
        this.cost = cost;
        this.types = types;
        this.subtypes = subtypes;
        this.ruleText = ruleText;
        this.power = power;
        this.toughness = toughness;
        this.abilities = abilities;
    }

    public Card(Card card) {
        this(card.getName(), new ArrayList<>(card.getColors()), new ArrayList<>(card.getCost()), new ArrayList<>(card.getTypes()),
                new ArrayList<>(card.getSubtypes()), card.getRuleText(), card.getPower(), card.getToughness(), card.getAbilities());
    }

    public static Card hiddenCard() {
        return new Card("card", emptyList(), emptyList(), emptyList(), emptyList(), "", 0, 0, emptyList());
    }

    public String getName() {
        return name;
    }

    public List<Color> getColors() {
        return colors;
    }

    public boolean isColorless() {
        return getCost().stream().noneMatch(cost -> cost != COLORLESS);
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

    public List<Ability> getAbilities() {
        return abilities;
    }

    public boolean isOfType(Type type) {
        return types.contains(type);
    }

    public boolean isOfColor(Color color) {
        return colors.contains(color);
    }

    public boolean ofAnyOfTheColors(List<Color> colors) {
        for (Color color : colors) {
            if (isOfColor(color)) {
                return true;
            }
        }
        return false;
    }
}
