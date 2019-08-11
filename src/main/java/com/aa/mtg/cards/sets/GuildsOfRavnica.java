package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.properties.Rarity.COMMON;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

public class GuildsOfRavnica implements MtgSet {

    public static final String GRN = "GRN";

    public static Card BARTIZAN_BATS = new Card("Bartizan Bats", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Bat"), COMMON, "", 3, 1, singletonList(FLYING));
    public static Card CANDLELIGHT_VIGIL = new Card("Candlelight Vigil", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList("Aura"), COMMON, "Enchant Creature. Enchanted creature gets +3/+2 and has vigilance.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_3_2_AND_VIGILANCE));
    public static Card CENTAUR_PEACEMAKER = new Card("Centaur Peacemaker", asSet(Color.WHITE, Color.GREEN), asList(Cost.WHITE, Cost.GREEN, Cost.COLORLESS), singletonList(CREATURE), asList("Centaur", "Cleric"), COMMON, "When Centaur Peacemaker enters the battlefield, each player gains 4 life.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_EACH_PLAYERS_GAIN_4_LIFE));
    public static Card COLLAR_THE_CULPRIT = new Card("Collar the Culprit", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Destroy target creature with toughness 4 or greater.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_WITH_TOUGHNESS_GREATER_OR_EQUAL_4));
    public static Card COMMAND_THE_STORM = new Card("Command the Storm", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Command the Storm deals 5 damage to target creature.", 0, 0, singletonList(DEAL_5_DAMAGE_TO_TARGET_CREATURE));
    public static Card DOUSER_OF_LIGHTS = new Card("Douser of Lights", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Horror"), COMMON, "", 4, 5, emptyList());
    public static Card FEARLESS_HALBERDIER = new Card("Fearless Halberdier", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Warrior"), COMMON, "", 3, 2, emptyList());
    public static Card GENEROUS_STRAY = new Card("Generous Stray", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Cat"), COMMON, "When Generous Stray enters the battlefield, draw a card.", 1, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card HEALERS_HAWK = new Card("Healer's Hawk", singleton(Color.WHITE), singletonList(Cost.WHITE), singletonList(CREATURE), singletonList("Bird"), COMMON, "Flying, lifelink", 1, 1, asList(FLYING, LIFELINK));
    public static Card HIRED_POISONER = new Card("Hired Poisoner", singleton(Color.BLACK), singletonList(Cost.BLACK), singletonList(CREATURE), asList("Human", "Assassin"), COMMON, "Deathtouch.", 1, 1, singletonList(DEATHTOUCH));
    public static Card HITCHCLAW_RECLUSE = new Card("Hitchclaw Recluse", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Spider"), COMMON, "Reach.", 1, 4, singletonList(REACH));
    public static Card MUSE_DRAKE = new Card("Muse Drake", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Drake"), COMMON, "Flying. When Muse Drake enters the battlefield, draw a card.", 1, 3, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card RUBBLEBELT_BOAR = new Card("Rubblebelt Boar", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Boar"), COMMON, "When Rubblebelt Boar enters the battlefield, target creature gets +2/+0 until end of turn.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_GETS_PLUS_2_0));
    public static Card SKYKNIGHT_LEGIONNAIRE = new Card("Skyknight Legionnaire", asSet(Color.RED, Color.WHITE), asList(Cost.RED, Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Knight"), COMMON, "Flying, haste.", 2, 2, asList(FLYING, HASTE));

    private static GuildsOfRavnica instance;

    private List<Card> cards = new ArrayList<>();

    private GuildsOfRavnica() {
        cards.add(BARTIZAN_BATS);
        cards.add(CANDLELIGHT_VIGIL);
        cards.add(CENTAUR_PEACEMAKER);
        cards.add(CoreSet2019.CHILD_OF_NIGHT);
        cards.add(COLLAR_THE_CULPRIT);
        cards.add(COMMAND_THE_STORM);
        cards.add(DOUSER_OF_LIGHTS);
        cards.add(FEARLESS_HALBERDIER);
        cards.add(GENEROUS_STRAY);
        cards.add(HEALERS_HAWK);
        cards.add(HIRED_POISONER);
        cards.add(HITCHCLAW_RECLUSE);
        cards.add(MUSE_DRAKE);
        cards.add(RUBBLEBELT_BOAR);
        cards.add(SKYKNIGHT_LEGIONNAIRE);
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
