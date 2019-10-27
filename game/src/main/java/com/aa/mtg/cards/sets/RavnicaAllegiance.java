package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.properties.Rarity.*;
import static com.aa.mtg.cards.properties.Subtype.*;
import static com.aa.mtg.cards.properties.Type.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

public class RavnicaAllegiance implements MtgSet {

    public static final String RNA = "RNA";

    public static Card AXEBANE_BEAST = new Card("Axebane Beast", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), singleton(BEAST), COMMON, "", 3, 4, emptyList());
    public static Card AZORIUS_GUILDGATE = new Card("Azorius Guildgate", emptySet(), emptyList(), singleton(LAND), singleton(GATE), COMMON, "Azorius Guildgate enters the battlefield tapped. TAP: Add WHITE or BLUE to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_WHITE_MANA, TAP_ADD_BLUE_MANA));
        public static Card BEDEVIL = new Card("Bedevil", asSet(Color.BLACK, Color.RED), asList(Cost.BLACK, Cost.BLACK, Cost.RED), singleton(INSTANT), emptySet(), RARE, "Destroy target artifact, creature, or planeswalker.", 0, 0, singletonList(DESTROY_TARGET_ARTIFACT_OR_CREATURE_OR_PLANESWALKER));
    public static Card BLADEBRAND = new Card("Bladebrand", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singleton(INSTANT), emptySet(), COMMON, "Target creature gains deathtouch until end of turn. Draw a card.", 0, 0, asList(TARGET_CREATURE_GETS_DEATHTOUCH_UNTIL_END_OF_TURN, DRAW_1_CARD));
    public static Card BURN_BRIGHT = new Card("Burn Bright", asSet(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singleton(INSTANT), emptySet(), COMMON, "Creatures you control get +2/+0 until end of turn.", 0, 0, singletonList(CREATURES_YOU_CONTROL_GET_PLUS_2_0_UNTIL_END_OF_TURN));
    public static Card CATACOMB_CROCODILE = new Card("Catacomb Crocodile", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), singleton(CROCODILE), COMMON, "", 3, 7, emptyList());
    public static Card CIVIC_STALWART = new Card("Civic Stalwart", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), asSet(ELEPHANT, SOLDIER), COMMON, "When Civic Stalwart enters the battlefield, creatures you control get +1/+1 until end of turn.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_1_1_UNTIL_END_OF_TURN));
    public static Card CLEAR_THE_MIND = new Card("Clear the Mind", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singleton(Type.SORCERY), emptySet(), COMMON, "Target player shuffles their graveyard into their library. Draw a card.", 0, 0, asList(SHUFFLE_GRAVEYARD_INTO_LIBRARY_OF_TARGET_PLAYER, DRAW_1_CARD));
    public static Card CONCORDIA_PEGASUS = new Card("Concordia Pegasus", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singleton(CREATURE), singleton(PEGASUS), COMMON, "Flying", 1, 3, singletonList(FLYING));
    public static Card CONSIGN_TO_THE_PIT = new Card("Consign to the Pit", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singleton(Type.SORCERY), emptySet(), COMMON, "Destroy target creature. Consign to the Pit deals 2 damage to that creature's controller.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_2_DAMAGE_TO_CONTROLLER));
    public static Card CORAL_COMMANDO = new Card("Coral Commando", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), asSet(MERFOLK, WARRIOR), COMMON, "", 3, 2, emptyList());
    public static Card END_RAZE_FORERUNNERS = new Card("End-Raze Forerunners", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), singleton(BOAR), RARE, "Vigilance, trample, haste. When End-Raze Forerunners enters the battlefield, other creatures you control get +2/+2 and gain vigilance and trample until end of turn.", 7, 7, asList(VIGILANCE, TRAMPLE, HASTE, WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_2_2_VIGILANCE_AND_TRAMPLE_UNTIL_END_OF_TURN));
    public static Card FERAL_MAAKA = new Card("Feral Maaka", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singleton(CREATURE), singleton(CAT), COMMON, "", 2, 2, emptyList());
    public static Card GIFT_OF_STRENGTH = new Card("Gift of Strength", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS), singleton(INSTANT), emptySet(), COMMON, "Target creature gets +3/+3 and gains reach until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_PLUS_3_3_AND_REACH_UNTIL_END_OF_TURN));
    public static Card GRAVEL_HIDE_GOBLIN = new Card("Gravel-Hide Goblin", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singleton(CREATURE), asSet(GOBLIN, SHAMAN), COMMON, "Pay 3 1 GREEN: Gravel-Hide Goblin gets +2/+2 until end of turn.", 2, 1, singletonList(PAY_3_1_GREEN_IT_GETS_PLUS_1_PLUS_0_UNTIL_END_OF_TURN));
    public static Card GRUUL_GUILDGATE = new Card("Gruul Guildgate", emptySet(), emptyList(), singleton(LAND), singleton(GATE), COMMON, "Gruul Guildgate enters the battlefield tapped. TAP: Add RED or GREEN to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_RED_MANA, TAP_ADD_GREEN_MANA));
    public static Card GYRE_ENGINEER = new Card("Gyre Engineer", asSet(Color.GREEN, Color.BLUE), asList(Cost.GREEN, Cost.BLUE, Cost.COLORLESS), singleton(CREATURE), asSet(VEDALKEN, WIZARD), UNCOMMON, "TAP: Add GREEN and BLUE.", 1, 1, singletonList(TAP_ADD_1_GREEN_1_BLUE_MANA));
    public static Card HAAZDA_OFFICER = new Card("Haazda Officer", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), asSet(HUMAN, SOLDIER), COMMON, "When Haazda Officer enters the battlefield, target creature you control gets +1/+1 until end of turn.", 3, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_YOU_CONTROL_GETS_PLUS_1_1));
    public static Card IMPASSIONED_ORATOR = new Card("Impassionate Orator", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singleton(CREATURE), asSet(HUMAN, CLERIC), COMMON, "Whenever another creature enters the battlefield under your control, you gain 1 life.", 2, 2, singletonList(WHENEVER_A_CREATURE_ENTERS_THE_BATTLEFIELD_UNDER_YOUR_CONTROL_YOU_GAIN_1_LIFE));
    public static Card JUDITH_THE_SCOURGE_DIVA = new Card("Judith, the Scourge Diva", asSet(Color.BLACK, Color.RED), asList(Cost.BLACK, Cost.RED, Cost.COLORLESS), asSet(LEGENDARY, CREATURE), asSet(HUMAN, SHAMAN), RARE, "Other creatures you control get +1/+0. Whenever a nontoken creature you control dies, Judith, the Scourge Diva deals 1 damage to any target.", 2, 2, asList(OTHER_CREATURES_YOU_CONTROL_GET_PLUS_1_0, WHENEVER_A_NON_TOKEN_CREATURE_YOU_CONTROL_DIES_IT_DEALS_1_DAMAGE_TO_ANY_TARGET));
    public static Card MORTIFY = new Card("Mortify", asSet(Color.WHITE, Color.BLACK), asList(Cost.WHITE, Cost.BLACK, Cost.COLORLESS), singleton(INSTANT), emptySet(), UNCOMMON, "Destroy target creature or enchantment.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_OR_ENCHANTMENT));
    public static Card NOXIOUS_GROODION = new Card("Noxious Groodion", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), singleton(BEAST), COMMON, "Deathtouch", 2, 2, singletonList(DEATHTOUCH));
    public static Card ORZHOV_GUILDGATE = new Card("Orzhov Guildgate", emptySet(), emptyList(), singleton(LAND), singleton(GATE), COMMON, "Orzhov Guildgate enters the battlefield tapped. TAP: Add WHITE or BLACK to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_WHITE_MANA, TAP_ADD_BLACK_MANA));
    public static Card PRECISION_BOLT = new Card("Precision Bolt", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singleton(SORCERY), emptySet(), COMMON, "Precision Bolt deals 3 damage to any target.", 0, 0, singletonList(DEAL_3_DAMAGE_TO_ANY_TARGET));
    public static Card PROWLING_CARACAL = new Card("Prowling Caracal", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singleton(CREATURE), singleton(CAT), COMMON, "", 3, 1, emptyList());
    public static Card RAKDOS_GUILDGATE = new Card("Rakdos Guildgate", emptySet(), emptyList(), singleton(LAND), singleton(GATE), COMMON, "Rakdos Guildgate enters the battlefield tapped. TAP: Add BLACK or RED to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_BLACK_MANA, TAP_ADD_RED_MANA));
    public static Card SENATE_COURIER = new Card("Senate Courier", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), singleton(BIRD), COMMON, "Flying. PAY 1 1 WHITE: Senate Courier gains vigilance until end of turn.", 1, 4, asList(FLYING, PAY_1_1_WHITE_IT_GETS_VIGILANCE_UNTIL_END_OF_TURN));
    public static Card SIMIC_GUILDGATE = new Card("Simic Guildgate", emptySet(), emptyList(), singleton(LAND), singleton(GATE), COMMON, "Simic Guildgate enters the battlefield tapped. TAP: Add GREEN or BLUE to your mana pool.", 0, 0, asList(ENTERS_THE_BATTLEFIELD_TAPPED, TAP_ADD_GREEN_MANA, TAP_ADD_BLUE_MANA));
    public static Card SPIRIT_OF_THE_SPIRES = new Card("Spirit of the Spires", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), singleton(SPIRIT), UNCOMMON, "Flying. Other creatures you control with flying get +0/+1.", 2, 4, asList(FLYING, OTHER_CREATURES_YOU_CONTROL_WITH_FLYING_GET_PLUS_0_1));
    public static Card STEEPLE_CREEPER = new Card("Steeple Creeper", singleton(Color.GREEN), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), asSet(FROG, SNAKE), COMMON, "PAY 3 1 BLUE: Steeple Creeper gains flying until end of turn.", 4, 2, singletonList(PAY_3_1_BLUE_IT_GETS_FLYING_UNTIL_END_OF_TURN));
    public static Card SYLVAN_BRUSHSTRIDER = new Card("Sylvan Brushstrider", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), singleton(BEAST), COMMON, "When Sylvan Brushstrider enters the battlefield, you gain 2 life.", 3, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_2_LIFE));
    public static Card THIRSTING_SHADE = new Card("Thirsting Shade", singleton(Color.BLACK), singletonList(Cost.BLACK), singleton(CREATURE), singleton(SHADE), COMMON, "Lifelink. PAY 2 1 BLACK: Thirsting Shade gets +1/+1 until end of turn.", 1, 1, asList(LIFELINK, PAY_2_1_BLACK_IT_GETS_PLUS_1_PLUS_1_UNTIL_END_OF_TURN));
    public static Card TWILIGHT_PANTHER = new Card("Twilight Panther", singleton(Color.WHITE), singletonList(Cost.WHITE), singleton(CREATURE), asSet(CAT, SPIRIT), COMMON, "PAY BLACK: Twilight Panther gains deathtouch until end of turn.", 1, 2, singletonList(PAY_BLACK_IT_GETS_DEATHTOUCH_UNTIL_END_OF_TURN));
    public static Card WINDSTORM_DRAKE = new Card("Windstorm Drake", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singleton(CREATURE), singleton(DRAKE), UNCOMMON, "Flying. Other creatures you control with flying get +1/+0.", 3, 3, asList(FLYING, OTHER_CREATURES_YOU_CONTROL_WITH_FLYING_GET_PLUS_1_0));

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
        cards.add(GRAVEL_HIDE_GOBLIN);
        cards.add(GRUUL_GUILDGATE);
        cards.add(GYRE_ENGINEER);
        cards.add(HAAZDA_OFFICER);
        cards.add(IMPASSIONED_ORATOR);
        cards.add(JUDITH_THE_SCOURGE_DIVA);
        cards.add(Dominaria.MAMMOTH_SPIDER);
        cards.add(MORTIFY);
        cards.add(NOXIOUS_GROODION);
        cards.add(ORZHOV_GUILDGATE);
        cards.add(PRECISION_BOLT);
        cards.add(PROWLING_CARACAL);
        cards.add(RAKDOS_GUILDGATE);
        cards.add(SENATE_COURIER);
        cards.add(SIMIC_GUILDGATE);
        cards.add(SPIRIT_OF_THE_SPIRES);
        cards.add(STEEPLE_CREEPER);
        cards.add(SYLVAN_BRUSHSTRIDER);
        cards.add(THIRSTING_SHADE);
        cards.add(TWILIGHT_PANTHER);
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
