package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.ENCHANTED_CREATURE_GETS_PLUS_3_2_AND_VIGILANCE;
import static com.aa.mtg.cards.ability.Abilities.LIFELINK;
import static com.aa.mtg.cards.ability.Abilities.WHEN_IT_ENTERS_THE_BATTLEFIELD_EACH_PLAYERS_GAIN_4_LIFE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class GuildsOfRavnica implements MtgSet {

    public static final String GRN = "GRN";

    public static Card CANDLELIGHT_VIGIL = new Card("Candlelight Vigil", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList("Aura"), "Enchant Creature. Enchanted creature gets +3/+2 and has vigilance.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_3_2_AND_VIGILANCE));
    public static Card CENTAUR_PEACEMAKER = new Card("Centaur Peacemaker", asList(Color.WHITE, Color.GREEN), asList(Cost.WHITE, Cost.GREEN, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Centaur", "Cleric"), "When Centaur Peacemaker enters the battlefield, each player gains 4 life.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_EACH_PLAYERS_GAIN_4_LIFE));
    public static Card CHILD_OF_NIGHT = new Card("Child of Night", singletonList(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Vampire"), "Lifelink", 2, 1, singletonList(LIFELINK));

    private static GuildsOfRavnica instance;

    private List<Card> cards = new ArrayList<>();

    private GuildsOfRavnica() {
        cards.add(CANDLELIGHT_VIGIL);
        cards.add(CENTAUR_PEACEMAKER);
        cards.add(CHILD_OF_NIGHT);
    }

    @Override
    public String getName() {
        return "Guild of Ravnica";
    }

    @Override
    public String getCode() {
        return GRN;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static GuildsOfRavnica guildsOfRavnica() {
        if (instance == null) {
            instance = new GuildsOfRavnica();
        }
        return instance;
    }
}
