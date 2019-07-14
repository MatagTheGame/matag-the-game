package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.ENCHANTED_CREATURE_GETS_MINUS_2_2;
import static com.aa.mtg.cards.ability.Abilities.PAY_1_EQUIP_CREATURE_GETS_PLUS_1_1_AND_HASTE;
import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class RivalsOfIxalan implements MtgSet {

    public static final String RIX = "RIX";

    public static Card CANAL_MONITOR = new Card("Canal Monitor", singletonList(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Lizard"), "", 5, 3, emptyList());
    public static Card DEAD_WEIGHT = new Card("Dead Weight", singletonList(Color.BLACK), singletonList(Cost.BLACK), singletonList(Type.ENCHANTMENT), singletonList("Aura"), "Enchant creature. Enchanted creature gets -2/-2.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_MINUS_2_2));
    public static Card STRIDER_HARNESS = new Card("Strider Harness", emptyList(), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(ARTIFACT), singletonList("Equipment"), "Equipped creature gets +1/+1 and has haste. Equip 1", 0, 0, singletonList(PAY_1_EQUIP_CREATURE_GETS_PLUS_1_1_AND_HASTE));

    private static RivalsOfIxalan instance;

    private List<Card> cards = new ArrayList<>();

    private RivalsOfIxalan() {
        cards.add(CANAL_MONITOR);
        cards.add(DEAD_WEIGHT);
        cards.add(STRIDER_HARNESS);
    }

    @Override
    public String getName() {
        return "Rivals of Ixalan";
    }

    @Override
    public String getCode() {
        return RIX;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static RivalsOfIxalan rivalsOfIxalan() {
        if (instance == null) {
            instance = new RivalsOfIxalan();
        }
        return instance;
    }
}
