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
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.properties.Type.LAND;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

public class RavnicaAllegiance implements MtgSet {

    public static final String RNA = "RNA";

    public static Card AXEBANE_BEAST = new Card("Axebane Beast", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Beast"), COMMON, "", 3, 4, emptyList());
    public static Card AZORIUS_GUILDGATE = new Card("Azorius Guildgate", emptySet(), emptyList(), singletonList(LAND), singletonList("Gate"), COMMON, "Azorius Guildgate enters the battlefield tapped. TAP: Add WHITE or BLUE to your mana pool.", 0, 0, singletonList(TAP_ADD_WHITE_MANA));
    public static Card BEDEVIL = new Card("Bedevil", asSet(Color.BLACK, Color.RED), asList(Cost.BLACK, Cost.BLACK, Cost.RED), singletonList(Type.INSTANT), emptyList(), RARE, "Destroy target artifact, creature, or planeswalker.", 0, 0, singletonList(DESTROY_TARGET_ARTIFACT_CREATURE_OR_PLANESWALKER));
    public static Card BLADEBRAND = new Card("Bladebrand", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Target creature gains deathtouch until end of turn. Draw a card.", 0, 0, asList(TARGET_CREATURE_GETS_DEATHTOUCH_UNTIL_END_OF_TURN, DRAW_1_CARD));
    public static Card BURN_BRIGHT = new Card("Burn Bright", asSet(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), RARE, "Creatures you control get +2/+0 until end of turn.", 0, 0, singletonList(CREATURES_YOU_CONTROL_GET_PLUS_2_0_UNTIL_END_OF_TURN));
    public static Card CATACOMB_CROCODILE = new Card("Catacomb Crocodile", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Crocodile"), COMMON, "", 3, 7, emptyList());
    public static Card CIVIC_STALWART = new Card("Civic Stalwart", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Elephant", "Soldier"), COMMON, "When Civic Stalwart enters the battlefield, creatures you control get +1/+1 until end of turn.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_1_1_UNTIL_END_OF_TURN));
    public static Card CLEAR_THE_MIND = new Card("Clear the Mind", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), COMMON, "Target player shuffles their graveyard into their library. Draw a card.", 0, 0, asList(SHUFFLE_GRAVEYARD_INTO_LIBRARY_OF_TARGET_PLAYER, DRAW_1_CARD));
    public static Card CONCORDIA_PEGASUS = new Card("Concordia Pegasus", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), singletonList("Pegasus"), COMMON, "Flying", 1, 3, singletonList(FLYING));
    public static Card CONSIGN_TO_THE_PIT = new Card("Consign to the Pit", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), COMMON, "Destroy target creature. Consign to the Pit deals 2 damage to that creature's controller.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_2_DAMAGE_TO_CONTROLLER));
    public static Card CORAL_COMMANDO = new Card("Coral Commando", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Merfolk", "Warrior"), COMMON, "", 3, 2, emptyList());
    public static Card END_RAZE_FORERUNNERS = new Card("End-Raze Forerunners", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Boar"), RARE, "Vigilance, trample, haste. When End-Raze Forerunners enters the battlefield, other creatures you control get +2/+2 and gain vigilance and trample until end of turn.", 7, 7, asList(VIGILANCE, TRAMPLE, HASTE, WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_2_2_VIGILANCE_AND_TRAMPLE_UNTIL_END_OF_TURN));
    public static Card FERAL_MAAKA = new Card("Feral Maaka", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(CREATURE), singletonList("Cat"), COMMON, "", 2, 2, emptyList());
    public static Card HAAZDA_OFFICER = new Card("Haazda Officer", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Solder"), COMMON, "When Haazda Officer enters the battlefield, target creature you control gets +1/+1 until end of turn.", 2, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_YOU_CONTROL_GETS_PLUS_1_1));
    public static Card NOXIOUS_GROODION = new Card("Noxious Groodion", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Beast"), COMMON, "Deathtouch", 2, 2, singletonList(DEATHTOUCH));
    public static Card PRECISION_BOLT = new Card("Precision Bolt", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), COMMON, "Precision Bolt deals 3 damage to any target.", 0, 0, singletonList(DEAL_3_DAMAGE_TO_ANY_TARGET));
    public static Card PROWLING_CARACAL = new Card("Prowling Caracal", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), singletonList("Cat"), COMMON, "", 3, 1, emptyList());
    public static Card SYLVAN_BRUSHSTRIDER = new Card("Sylvan Brushstrider", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Beast"), COMMON, "When Sylvan Brushstrider enters the battlefield, you gain 2 life.", 3, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_2_LIFE));

    private static RavnicaAllegiance instance;

    private List<Card> cards = new ArrayList<>();

    private RavnicaAllegiance() {
        cards.add(AXEBANE_BEAST);
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
        cards.add(HAAZDA_OFFICER);
        cards.add(Dominaria.MAMMOTH_SPIDER);
        cards.add(NOXIOUS_GROODION);
        cards.add(PRECISION_BOLT);
        cards.add(PROWLING_CARACAL);
        cards.add(SYLVAN_BRUSHSTRIDER);
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
