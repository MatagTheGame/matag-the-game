package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import static com.aa.mtg.cards.ability.Abilities.ENCHANTED_CREATURE_GETS_PLUS_3_2_AND_VIGILANCE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class GuildsOfRavnica {

    public static Card CANDLELIGHT_VIGIL = new Card("Candlelight Vigil", singletonList(Color.WHITE), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList("Aura"), "Enchant Creature. Enchanted creature gets +3/+2 and has vigilance.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_3_2_AND_VIGILANCE));

}
