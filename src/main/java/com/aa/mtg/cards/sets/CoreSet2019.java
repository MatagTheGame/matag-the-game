package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.properties.Rarity.COMMON;
import static com.aa.mtg.cards.properties.Rarity.RARE;
import static com.aa.mtg.cards.properties.Rarity.UNCOMMON;
import static com.aa.mtg.cards.properties.Type.ENCHANTMENT;
import static com.aa.mtg.cards.properties.Type.SORCERY;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

public class CoreSet2019 implements MtgSet {

    public static final String M19 = "M19";

    public static Card AEGIS_OF_THE_HEAVENS = new Card("Aegis of the Heavens", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), UNCOMMON, "Target creature gets +1/+7 until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_PLUS_1_7_UNTIL_END_OF_TURN));
    public static Card ANGEL_OF_THE_DAWN = new Card("Angel of the Dawn", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Angel"), COMMON, "Flying. When Angel of the Dawn enters the battlefield, creatures you control get +1/+1 and gain vigilance until end of turn.", 3, 3, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_1_1_AND_VIGILANCE_UNTIL_END_OF_TURN));
    public static Card CRASH_THROUGH = new Card("Crash Through", singleton(Color.RED), singletonList(Cost.RED), singletonList(SORCERY), emptyList(), COMMON, "Creatures you control gain trample until end of turn. Draw a card.", 0, 0, asList(CREATURES_YOU_CONTROL_GET_TRAMPLE_UNTIL_END_OF_TURN, DRAW_1_CARD));
    public static Card DAGGERBACK_BASILISK = new Card("Daggerback Basilisk", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Basilisk"), COMMON, "Deathtouch", 0, 0, singletonList(DEATHTOUCH));
    public static Card DIVINATION = new Card("Divination", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(SORCERY), singletonList("Basilisk"), COMMON, "Deathtouch", 0, 0, singletonList(DEATHTOUCH));
    public static Card KNIGHTS_PLEDGE = new Card("Knight's Pledge", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(ENCHANTMENT), emptyList(), COMMON, "Draw two cards.", 0, 0, singletonList(DRAW_2_CARD));
    public static Card PRODIGIOUS_GROWTH = new Card("Prodigious Growth", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(ENCHANTMENT), singletonList("Aura"), RARE, "Enchant creature. Enchanted creature gets +7/+7 and has trample.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_7_7_AND_TRAMPLE));

    private static CoreSet2019 instance;

    private List<Card> cards = new ArrayList<>();

    private CoreSet2019() {
        cards.add(AEGIS_OF_THE_HEAVENS);
        cards.add(ANGEL_OF_THE_DAWN);
        cards.add(CRASH_THROUGH);
        cards.add(DAGGERBACK_BASILISK);
        cards.add(DIVINATION);
        cards.add(KNIGHTS_PLEDGE);
        cards.add(PRODIGIOUS_GROWTH);
    }

    @Override
    public String getName() {
        return "Core Set 2019";
    }

    @Override
    public String getCode() {
        return M19;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static CoreSet2019 m19() {
        if (instance == null) {
            instance = new CoreSet2019();
        }
        return instance;
    }
}
