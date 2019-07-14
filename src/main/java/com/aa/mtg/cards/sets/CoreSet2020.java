package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class CoreSet2020 implements MtgSet {

    public static final String M20 = "M20";

    public static Card ANGELIC_GIFT = new Card("Angelic Gift", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList("Aura"), "Enchant creature. When Angelic Gift enters the battlefield, draw a card. Enchanted creature has flying.", 0, 0, asList(ENCHANTED_CREATURE_GETS_FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card MARAUDERS_AXE = new Card("Marauder's Axe", emptyList(), asList(Cost.COLORLESS, Cost.COLORLESS), singletonList(ARTIFACT), singletonList("Equipment"), "Equipped creature gets +2/+0. Equip 2", 0, 0, singletonList(PAY_2_EQUIP_CREATURE_GETS_PLUS_2_0));
    public static Card OAKENFORM = new Card("Oakenform", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList("Aura"), "Enchant creature. Enchanted creature gets +3/+3.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_3_3));

    private static CoreSet2020 instance;

    private List<Card> cards = new ArrayList<>();

    private CoreSet2020() {
        cards.add(ANGELIC_GIFT);
        cards.add(MARAUDERS_AXE);
        cards.add(OAKENFORM);
    }

    @Override
    public String getName() {
        return "Core Set 2020";
    }

    @Override
    public String getCode() {
        return M20;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static CoreSet2020 m20() {
        if (instance == null) {
            instance = new CoreSet2020();
        }
        return instance;
    }
}
