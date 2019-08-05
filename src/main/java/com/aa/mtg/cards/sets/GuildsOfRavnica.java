package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.properties.Rarity.COMMON;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

public class GuildsOfRavnica implements MtgSet {

    public static final String GRN = "GRN";

    public static Card BARTIZAN_BATS = new Card("Bartizan Bats", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Bat"), COMMON, "", 3, 1, singletonList(FLYING));
    public static Card CANDLELIGHT_VIGIL = new Card("Candlelight Vigil", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList("Aura"), COMMON, "Enchant Creature. Enchanted creature gets +3/+2 and has vigilance.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_3_2_AND_VIGILANCE));
    public static Card CENTAUR_PEACEMAKER = new Card("Centaur Peacemaker", asSet(Color.WHITE, Color.GREEN), asList(Cost.WHITE, Cost.GREEN, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Centaur", "Cleric"), COMMON, "When Centaur Peacemaker enters the battlefield, each player gains 4 life.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_EACH_PLAYERS_GAIN_4_LIFE));
    public static Card COLLAR_THE_CULPRIT = new Card("Collar the Culprit", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Destroy target creature with toughness 4 or greater.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_WITH_TOUGHNESS_GREATER_OR_EQUAL_4));
    public static Card COMMAND_THE_STORM = new Card("Command the Storm", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Command the Storm deals 5 damage to target creature.", 0, 0, singletonList(DEAL_5_DAMAGE_TO_TARGET_CREATURE));

    private static GuildsOfRavnica instance;

    private List<Card> cards = new ArrayList<>();

    private GuildsOfRavnica() {
        cards.add(BARTIZAN_BATS);
        cards.add(CANDLELIGHT_VIGIL);
        cards.add(CENTAUR_PEACEMAKER);
        cards.add(CoreSet2019.CHILD_OF_NIGHT);
        cards.add(COLLAR_THE_CULPRIT);
        cards.add(COMMAND_THE_STORM);
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
