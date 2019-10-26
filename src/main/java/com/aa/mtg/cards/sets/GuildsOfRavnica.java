package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.properties.Rarity.COMMON;
import static com.aa.mtg.cards.properties.Rarity.UNCOMMON;
import static com.aa.mtg.cards.properties.Subtype.*;
import static com.aa.mtg.cards.properties.Type.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

public class GuildsOfRavnica implements MtgSet {

    public static final String GRN = "GRN";

    public static Card BARTIZAN_BATS = new Card("Bartizan Bats", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(BAT), COMMON, "", 3, 1, singletonList(FLYING));
    public static Card BOROS_GUILDGATE = new Card("Boros Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList(GATE), COMMON, "Boros Guildgate enters the battlefield tapped. TAP: Add RED or WHITE to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_RED_MANA, TAP_ADD_WHITE_MANA));
    public static Card CANDLELIGHT_VIGIL = new Card("Candlelight Vigil", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList(AURA), COMMON, "Enchant Creature. Enchanted creature gets +3/+2 and has vigilance.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_3_2_AND_VIGILANCE));
    public static Card CENTAUR_PEACEMAKER = new Card("Centaur Peacemaker", asSet(Color.WHITE, Color.GREEN), asList(Cost.WHITE, Cost.GREEN, Cost.COLORLESS), singletonList(CREATURE), asList(CENTAUR, CLERIC), COMMON, "When Centaur Peacemaker enters the battlefield, each player gains 4 life.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_EACH_PLAYERS_GAIN_4_LIFE));
    public static Card COLLAR_THE_CULPRIT = new Card("Collar the Culprit", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Destroy target creature with toughness 4 or greater.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_WITH_TOUGHNESS_GREATER_OR_EQUAL_4));
    public static Card COMMAND_THE_STORM = new Card("Command the Storm", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Command the Storm deals 5 damage to target creature.", 0, 0, singletonList(DEAL_5_DAMAGE_TO_TARGET_CREATURE));
    public static Card DEVKARIN_DISSIDENT = new Card("Devkarin Dissident", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS), singletonList(CREATURE), asList(ELF, WARRIOR), COMMON, "PAY 4, 1 GREEN: Devkarin Dissident gets +2/+2 until end of turn.", 2, 2, singletonList(PAY_4_1_RED_IT_GETS_PLUS_2_PLUS_2_UNTIL_END_OF_TURN));
    public static Card DIMIR_GUILDGATE = new Card("Dimir Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList(GATE), COMMON, "Dimir Guildgate enters the battlefield tapped. TAP: Add BLUE or BLACK to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_BLUE_MANA, TAP_ADD_BLACK_MANA));
    public static Card DOUSER_OF_LIGHTS = new Card("Douser of Lights", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(HORROR), COMMON, "", 4, 5, emptyList());
    public static Card FEARLESS_HALBERDIER = new Card("Fearless Halberdier", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, WARRIOR), COMMON, "", 3, 2, emptyList());
    public static Card GENEROUS_STRAY = new Card("Generous Stray", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(CAT), COMMON, "When Generous Stray enters the battlefield, draw a card.", 1, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card GIRD_FOR_BATTLE = new Card("Gird for Battle", singleton(Color.WHITE), singletonList(Cost.WHITE), singletonList(SORCERY), emptyList(), UNCOMMON, "Put a +1/+1 counter on each of up to two target creatures.", 0, 0, singletonList(PUT_A_PLUS_1_COUNTER_UP_TO_2_TARGET_CREATURES));
    public static Card GOLGARI_GUILDGATE = new Card("Golgari Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList(GATE), COMMON, "Golgari Guildgate enters the battlefield tapped. TAP: Add BLACK or GREEN to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_BLACK_MANA, TAP_ADD_GREEN_MANA));
    public static Card HEALERS_HAWK = new Card("Healer's Hawk", singleton(Color.WHITE), singletonList(Cost.WHITE), singletonList(CREATURE), singletonList(BIRD), COMMON, "Flying, lifelink", 1, 1, asList(FLYING, LIFELINK));
    public static Card HIRED_POISONER = new Card("Hired Poisoner", singleton(Color.BLACK), singletonList(Cost.BLACK), singletonList(CREATURE), asList(HUMAN, ASSASSIN), COMMON, "Deathtouch.", 1, 1, singletonList(DEATHTOUCH));
    public static Card HITCHCLAW_RECLUSE = new Card("Hitchclaw Recluse", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(SPIDER), COMMON, "Reach.", 1, 4, singletonList(REACH));
    public static Card IZZET_GUILDGATE = new Card("Izzet Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList(GATE), COMMON, "Izzet Guildgate enters the battlefield tapped. TAP: Add BLUE or RED to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_BLUE_MANA, TAP_ADD_RED_MANA));
    public static Card MUSE_DRAKE = new Card("Muse Drake", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(DRAKE), COMMON, "Flying. When Muse Drake enters the battlefield, draw a card.", 1, 3, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card RUBBLEBELT_BOAR = new Card("Rubblebelt Boar", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(BOAR), COMMON, "When Rubblebelt Boar enters the battlefield, target creature gets +2/+0 until end of turn.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_GETS_PLUS_2_0));
    public static Card SELESNYA_GUILDGATE = new Card("Selesnya Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList(GATE), COMMON, "Selesnya Guildgate enters the battlefield tapped. TAP: Add GREEN or WHITE to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_GREEN_MANA, TAP_ADD_WHITE_MANA));
    public static Card SKYKNIGHT_LEGIONNAIRE = new Card("Skyknight Legionnaire", asSet(Color.RED, Color.WHITE), asList(Cost.RED, Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, KNIGHT), COMMON, "Flying, haste.", 2, 2, asList(FLYING, HASTE));
    public static Card SPINAL_CENTIPEDE = new Card("Spinal Centipede", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(INSECT), COMMON, "When Spinal Centipede dies, put a +1/+1 counter on target creature you control.", 3, 2, singletonList(WHEN_IT_DIES_PUT_A_PLUS_1_COUNTER_ON_TARGET_CREATURE_YOU_CONTROL));
    public static Card TENTH_DISTRICT_GUARD = new Card("Tenth District Guard", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, SOLDIER), COMMON, "When Tenth District Guard enters the battlefield, target creature gets +0/+1 until end of turn.", 2, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_GETS_PLUS_0_1));
    public static Card VEILED_SHADE = new Card("Veiled Shade", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(SHADE), COMMON, "PAY 1 1 BLACK: Veiled Shade gets +1/+1 until end of turn.", 2, 2, singletonList(PAY_1_1_BLACK_IT_GETS_PLUS_1_PLUS_1_UNTIL_END_OF_TURN));
    public static Card WARY_OKAPY = new Card("Wary Okapi", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(ANTELOPE), COMMON, "Vigilance", 3, 2, singletonList(VIGILANCE));
    public static Card WILD_CERATOK = new Card("Wild Ceratok", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(RHINO), COMMON, "", 4, 3, emptyList());
    public static Card WISHCOIN_CRAB = new Card("Wishcoin Crab", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(CRAB), COMMON, "", 2, 5, emptyList());

    private static GuildsOfRavnica instance;

    private List<Card> cards = new ArrayList<>();

    private GuildsOfRavnica() {
        cards.add(BARTIZAN_BATS);
        cards.add(BOROS_GUILDGATE);
        cards.add(CANDLELIGHT_VIGIL);
        cards.add(CENTAUR_PEACEMAKER);
        cards.add(CoreSet2019.CHILD_OF_NIGHT);
        cards.add(COLLAR_THE_CULPRIT);
        cards.add(COMMAND_THE_STORM);
        cards.add(DEVKARIN_DISSIDENT);
        cards.add(DIMIR_GUILDGATE);
        cards.add(DOUSER_OF_LIGHTS);
        cards.add(FEARLESS_HALBERDIER);
        cards.add(GENEROUS_STRAY);
        cards.add(GOLGARI_GUILDGATE);
        cards.add(HEALERS_HAWK);
        cards.add(HIRED_POISONER);
        cards.add(HITCHCLAW_RECLUSE);
        cards.add(IZZET_GUILDGATE);
        cards.add(MUSE_DRAKE);
        cards.add(RUBBLEBELT_BOAR);
        cards.add(GuildsOfRavnica.SELESNYA_GUILDGATE);
        cards.add(SKYKNIGHT_LEGIONNAIRE);
        cards.add(SPINAL_CENTIPEDE);
        cards.add(TENTH_DISTRICT_GUARD);
        cards.add(VEILED_SHADE);
        cards.add(WARY_OKAPY);
        cards.add(WILD_CERATOK);
        cards.add(WISHCOIN_CRAB);
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
