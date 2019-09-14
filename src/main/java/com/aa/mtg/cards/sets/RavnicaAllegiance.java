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
import static com.aa.mtg.cards.properties.Subtype.*;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.properties.Type.INSTANT;
import static com.aa.mtg.cards.properties.Type.LAND;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

public class RavnicaAllegiance implements MtgSet {

    public static final String RNA = "RNA";

    public static Card AXEBANE_BEAST = new Card("Axebane Beast", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(BEAST), COMMON, "", 3, 4, emptyList());
    public static Card AZORIUS_GUILDGATE = new Card("Azorius Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList(GATE), COMMON, "Azorius Guildgate enters the battlefield tapped. TAP: Add WHITE or BLUE to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_WHITE_MANA, TAP_ADD_BLUE_MANA));
    public static Card BEDEVIL = new Card("Bedevil", asSet(Color.BLACK, Color.RED), asList(Cost.BLACK, Cost.BLACK, Cost.RED), singletonList(INSTANT), emptyList(), RARE, "Destroy target artifact, creature, or planeswalker.", 0, 0, singletonList(DESTROY_TARGET_ARTIFACT_OR_CREATURE_OR_PLANESWALKER));
    public static Card BLADEBRAND = new Card("Bladebrand", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(INSTANT), emptyList(), COMMON, "Target creature gains deathtouch until end of turn. Draw a card.", 0, 0, asList(TARGET_CREATURE_GETS_DEATHTOUCH_UNTIL_END_OF_TURN, DRAW_1_CARD));
    public static Card BURN_BRIGHT = new Card("Burn Bright", asSet(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(INSTANT), emptyList(), RARE, "Creatures you control get +2/+0 until end of turn.", 0, 0, singletonList(CREATURES_YOU_CONTROL_GET_PLUS_2_0_UNTIL_END_OF_TURN));
    public static Card CATACOMB_CROCODILE = new Card("Catacomb Crocodile", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(CROCODILE), COMMON, "", 3, 7, emptyList());
    public static Card CIVIC_STALWART = new Card("Civic Stalwart", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(ELEPHANT, SOLDIER), COMMON, "When Civic Stalwart enters the battlefield, creatures you control get +1/+1 until end of turn.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_1_1_UNTIL_END_OF_TURN));
    public static Card CLEAR_THE_MIND = new Card("Clear the Mind", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), COMMON, "Target player shuffles their graveyard into their library. Draw a card.", 0, 0, asList(SHUFFLE_GRAVEYARD_INTO_LIBRARY_OF_TARGET_PLAYER, DRAW_1_CARD));
    public static Card CONCORDIA_PEGASUS = new Card("Concordia Pegasus", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), singletonList(PEGASUS), COMMON, "Flying", 1, 3, singletonList(FLYING));
    public static Card CONSIGN_TO_THE_PIT = new Card("Consign to the Pit", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), COMMON, "Destroy target creature. Consign to the Pit deals 2 damage to that creature's controller.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_2_DAMAGE_TO_CONTROLLER));
    public static Card CORAL_COMMANDO = new Card("Coral Commando", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(MERFOLK, WARRIOR), COMMON, "", 3, 2, emptyList());
    public static Card END_RAZE_FORERUNNERS = new Card("End-Raze Forerunners", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(BOAR), RARE, "Vigilance, trample, haste. When End-Raze Forerunners enters the battlefield, other creatures you control get +2/+2 and gain vigilance and trample until end of turn.", 7, 7, asList(VIGILANCE, TRAMPLE, HASTE, WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_2_2_VIGILANCE_AND_TRAMPLE_UNTIL_END_OF_TURN));
    public static Card FERAL_MAAKA = new Card("Feral Maaka", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(CREATURE), singletonList(CAT), COMMON, "", 2, 2, emptyList());
    public static Card GIFT_OF_STRENGTH = new Card("Gift of Strength", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS), singletonList(INSTANT), emptyList(), COMMON, "Target creature gets +3/+3 and gains reach until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_PLUS_3_3_AND_REACH_UNTIL_END_OF_TURN));
    public static Card GRUUL_GUILDGATE = new Card("Gruul Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList(GATE), COMMON, "Gruul Guildgate enters the battlefield tapped. TAP: Add RED or GREEN to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_RED_MANA, TAP_ADD_GREEN_MANA));
    public static Card GYRE_ENGINEER = new Card("Gyre Engineer", asSet(Color.GREEN, Color.BLUE), asList(Cost.GREEN, Cost.BLUE, Cost.COLORLESS), singletonList(CREATURE), asList(VEDALKEN, WIZARD), UNCOMMON, "TAP: Add GREEN or BLUE.", 1, 1, singletonList(TAP_ADD_1_GREEN_1_BLUE_MANA));
    public static Card HAAZDA_OFFICER = new Card("Haazda Officer", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, SOLDIER), COMMON, "When Haazda Officer enters the battlefield, target creature you control gets +1/+1 until end of turn.", 3, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_YOU_CONTROL_GETS_PLUS_1_1));
    public static Card MORTIFY = new Card("Mortify", asSet(Color.WHITE, Color.BLACK), asList(Cost.WHITE, Cost.BLACK, Cost.COLORLESS), singletonList(INSTANT), emptyList(), UNCOMMON, "Destroy target creature or enchantment.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_OR_ENCHANTMENT));
    public static Card NOXIOUS_GROODION = new Card("Noxious Groodion", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(BEAST), COMMON, "Deathtouch", 2, 2, singletonList(DEATHTOUCH));
    public static Card ORZHOV_GUILDGATE = new Card("Orzhov Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList(GATE), COMMON, "Orzhov Guildgate enters the battlefield tapped. TAP: Add WHITE or BLACK to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_WHITE_MANA, TAP_ADD_BLACK_MANA));
    public static Card PRECISION_BOLT = new Card("Precision Bolt", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), COMMON, "Precision Bolt deals 3 damage to any target.", 0, 0, singletonList(DEAL_3_DAMAGE_TO_ANY_TARGET));
    public static Card PROWLING_CARACAL = new Card("Prowling Caracal", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), singletonList(CAT), COMMON, "", 3, 1, emptyList());
    public static Card RAKDOS_GUILDGATE = new Card("Rakdos Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList(GATE), COMMON, "Rakdos Guildgate enters the battlefield tapped. TAP: Add BLACK or RED to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_BLACK_MANA, TAP_ADD_RED_MANA));
    public static Card SIMIC_GUILDGATE = new Card("Simic Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList(GATE), COMMON, "Simic Guildgate enters the battlefield tapped. TAP: Add GREEN or BLUE to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_GREEN_MANA, TAP_ADD_BLUE_MANA));
    public static Card SPIRIT_OF_THE_SPIRES = new Card("Spirit of the Spires", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(SPIRIT), UNCOMMON, "Flying. Other creatures you control with flying get +0/+1.", 2, 4, asList(FLYING, OTHER_CREATURES_YOU_CONTROL_WITH_FLYING_GET_PLUS_0_1));
    public static Card SYLVAN_BRUSHSTRIDER = new Card("Sylvan Brushstrider", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(BEAST), COMMON, "When Sylvan Brushstrider enters the battlefield, you gain 2 life.", 3, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_2_LIFE));
    public static Card WINDSTORM_DRAKE = new Card("Windstorm Drake", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(DRAKE), UNCOMMON, "Flying. Other creatures you control with flying get +1/+0.", 3, 3, asList(FLYING, OTHER_CREATURES_YOU_CONTROL_WITH_FLYING_GET_PLUS_1_0));

    private static RavnicaAllegiance instance;

    private List<Card> cards = new ArrayList<>();

    private RavnicaAllegiance() {
        cards.add(CoreSet2019.ACT_OF_TREASON);
        cards.add(AXEBANE_BEAST);
        cards.add(AZORIUS_GUILDGATE);
        cards.add(BEDEVIL);
        cards.add(BLADEBRAND);
        cards.add(BURN_BRIGHT);
        cards.add(CATACOMB_CROCODILE);
        cards.add(CIVIC_STALWART);
        cards.add(CLEAR_THE_MIND);
        cards.add(CONCORDIA_PEGASUS);
        cards.add(CONSIGN_TO_THE_PIT);
        cards.add(CORAL_COMMANDO);
        cards.add(END_RAZE_FORERUNNERS);
        cards.add(FERAL_MAAKA);
        cards.add(GIFT_OF_STRENGTH);
        cards.add(GRUUL_GUILDGATE);
        cards.add(GYRE_ENGINEER);
        cards.add(HAAZDA_OFFICER);
        cards.add(Dominaria.MAMMOTH_SPIDER);
        cards.add(MORTIFY);
        cards.add(NOXIOUS_GROODION);
        cards.add(ORZHOV_GUILDGATE);
        cards.add(PRECISION_BOLT);
        cards.add(PROWLING_CARACAL);
        cards.add(RAKDOS_GUILDGATE);
        cards.add(SIMIC_GUILDGATE);
        cards.add(SPIRIT_OF_THE_SPIRES);
        cards.add(SYLVAN_BRUSHSTRIDER);
        cards.add(WINDSTORM_DRAKE);
    }

    @Override
    public String getName() {
        return "Ravnica Allegiance";
    }

    @Override
    public String getCode() {
        return RNA;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static RavnicaAllegiance ravnicaAllegiance() {
        if (instance == null) {
            instance = new RavnicaAllegiance();
        }
        return instance;
    }
}
