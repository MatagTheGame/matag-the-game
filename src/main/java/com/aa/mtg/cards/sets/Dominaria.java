package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.properties.Rarity.COMMON;
import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.properties.Type.SORCERY;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

public class Dominaria implements MtgSet {

    public static final String DOM = "DOM";

    public static Card ARCANE_FLIGHT = new Card("Arcane Flight", singleton(Color.BLUE), singletonList(Cost.BLUE), singletonList(Type.ENCHANTMENT), singletonList("Aura"), COMMON, "Enchant creature. Enchanted creature gets +1/+1 and has flying.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_1_1_AND_FLYING));
    public static Card AVEN_SENTRY = new Card("Aven Sentry", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Bird", "Soldier"), COMMON, "Flying.", 3, 2, singletonList(FLYING));
    public static Card BEFUDDLE = new Card("Befuddle", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Target creature gets -4/-0 until end of turn. Draw a card.", 0, 0, asList(TARGET_CREATURE_GETS_MINUS_4_0_UNTIL_END_OF_TURN, DRAW_1_CARD));
    public static Card CABAL_EVANGEL = new Card("Cabal Evangel", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Cleric"), COMMON, ".", 2, 2, emptyList());
    public static Card CHARGE = new Card("Charge", singleton(Color.WHITE), singletonList(Cost.WHITE), singletonList(Type.INSTANT), emptyList(), COMMON, "Creatures you control get +1/+1 until end of turn.", 0, 0, singletonList(CREATURES_YOU_CONTROL_GET_PLUS_1_1_UNTIL_END_OF_TURN));
    public static Card DIVINATION = new Card("Divination", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(SORCERY), emptyList(), COMMON, "Draw 2 cards.", 0, 0, singletonList(DRAW_2_CARD));
    public static Card FERAL_ABOMINATION = new Card("Feral Abomination", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Thrull"), COMMON, "Deathtouch", 5, 5, singletonList(DEATHTOUCH));
    public static Card FIRE_ELEMENTAL = new Card("Fire Elemental", singleton(Color.RED), asList(Cost.RED, Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Elemental"), COMMON, "", 5, 4, emptyList());
    public static Card KNIGHT_OF_NEW_BENALIA = new Card("Knight of New Benalia", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Knight"), COMMON, "", 3, 1, emptyList());
    public static Card MAMMOTH_SPIDER = new Card("Mammoth Spider", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Spider"), COMMON, "Reach", 3, 5, singletonList(REACH));
    public static Card MESA_UNICORN = new Card("Mesa Unicorn", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), singletonList("Unicorn"), COMMON, "Lifelink", 2, 2, singletonList(LIFELINK));
    public static Card PARDIC_WANDERER = new Card("Pardic Wanderer", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), asList(ARTIFACT, CREATURE), singletonList("Golem"), COMMON, "Trample", 5, 5, singletonList(TRAMPLE));
    public static Card PRIMORDIAL_WURM = new Card("Primordial Wurm", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Wurm"), COMMON, "", 7, 6, emptyList());
    public static Card PYROMANTIC_PILGRIM = new Card("Pyromantic Pilgrim", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Wizard"), COMMON, "Haste", 3, 1, singletonList(HASTE));
    public static Card SHORT_SWORD = new Card("Short Sword", emptySet(), singletonList(Cost.COLORLESS), singletonList(ARTIFACT), singletonList("Equipment"), COMMON, "Equipment Creatures get +1/+1. Equip 2", 0, 0, singletonList(PAY_1_EQUIP_CREATURE_GETS_PLUS_1_1));

    private static Dominaria instance;

    private List<Card> cards = new ArrayList<>();

    private Dominaria() {
        cards.add(ARCANE_FLIGHT);
        cards.add(AVEN_SENTRY);
        cards.add(BEFUDDLE);
        cards.add(CABAL_EVANGEL);
        cards.add(CHARGE);
        cards.add(DIVINATION);
        cards.add(FERAL_ABOMINATION);
        cards.add(FIRE_ELEMENTAL);
        cards.add(KNIGHT_OF_NEW_BENALIA);
        cards.add(MAMMOTH_SPIDER);
        cards.add(MESA_UNICORN);
        cards.add(PRIMORDIAL_WURM);
        cards.add(PARDIC_WANDERER);
        cards.add(PYROMANTIC_PILGRIM);
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
