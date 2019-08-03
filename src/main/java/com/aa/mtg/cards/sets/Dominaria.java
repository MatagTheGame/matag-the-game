package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.ENCHANTED_CREATURE_GETS_PLUS_1_1_AND_FLYING;
import static com.aa.mtg.cards.ability.Abilities.FLYING;
import static com.aa.mtg.cards.ability.Abilities.PAY_1_EQUIP_CREATURE_GETS_PLUS_1_1;
import static com.aa.mtg.cards.properties.Rarity.COMMON;
import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

public class Dominaria implements MtgSet {

    public static final String DOM = "DOM";

    public static Card ARCANE_FLIGHT = new Card("Arcane Flight", singleton(Color.BLUE), singletonList(Cost.BLUE), singletonList(Type.ENCHANTMENT), singletonList("Aura"), COMMON, "Enchant creature. Enchanted creature gets +1/+1 and has flying.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_1_1_AND_FLYING));
    public static Card AVEN_SENTRY = new Card("Aven Sentry", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Bird", "Soldier"), COMMON, "Flying.", 3, 2, singletonList(FLYING));
    public static Card CABAL_EVANGEL = new Card("Cabal Evangel", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Human", "Cleric"), COMMON, ".", 2, 2, emptyList());
    public static Card SHORT_SWORD = new Card("Short Sword", emptySet(), singletonList(Cost.COLORLESS), singletonList(ARTIFACT), singletonList("Equipment"), COMMON, "Equipment Creatures get +1/+1. Equip 2", 0, 0, singletonList(PAY_1_EQUIP_CREATURE_GETS_PLUS_1_1));

    private static Dominaria instance;

    private List<Card> cards = new ArrayList<>();

    private Dominaria() {
        cards.add(ARCANE_FLIGHT);
        cards.add(AVEN_SENTRY);
        cards.add(CABAL_EVANGEL);
        cards.add(SHORT_SWORD);
    }

    @Override
    public String getName() {
        return "Ixalan";
    }

    @Override
    public String getCode() {
        return DOM;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static Dominaria dominaria() {
        if (instance == null) {
            instance = new Dominaria();
        }
        return instance;
    }
}
