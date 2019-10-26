package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.properties.Rarity.COMMON;
import static com.aa.mtg.cards.properties.Rarity.UNCOMMON;
import static com.aa.mtg.cards.properties.Subtype.*;
import static com.aa.mtg.cards.properties.Type.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

public class ThroneOfEldraine implements MtgSet {

    public static final String ELD = "ELD";

    public static Card ARDENVALE_PALADIN = new Card("Ardenvale Paladin", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, KNIGHT), COMMON, "Adamant — If at least three white mana was spent to cast this spell, Ardenvale Paladin enters the battlefield with a +1/+1 counter on it.", 2, 5, singletonList(ADAMANT_WHITE_ENTER_PLUS_1_COUNTER));
    public static Card CLOCKWORK_SERVANT = new Card("Clockwork Servant", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), asList(ARTIFACT, CREATURE), singletonList(GNOME), UNCOMMON, "Adamant — When Clockwork Servant enters the battlefield, if at least three mana of the same color was spent to cast it, draw a card.", 2, 3, singletonList(ADAMANT_SAME_ENTER_DRAW_A_CARD));
    public static Card CRYSTAL_SLIPPER = new Card("Crystal Slipper", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(ARTIFACT), singletonList(EQUIPMENT), COMMON, "Equipped creature gets +1/+0 and has haste. Equip 1", 0, 0, singletonList(PAY_1_EQUIP_CREATURE_GETS_PLUS_1_0_AND_HASTE));
    public static Card EMBERETH_PALADIN = new Card("Embereth Paladin", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, KNIGHT), COMMON, "Haste. Adamant — If at least three red mana was spent to cast this spell, Embereth Paladin enters the battlefield with a +1/+1 counter on it.", 4, 1, asList(HASTE, ADAMANT_RED_ENTER_PLUS_1_COUNTER));
    public static Card HENGE_WALKER = new Card("Henge Walker", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), asList(ARTIFACT, CREATURE), singletonList(GOLEM), COMMON, "Adamant — If at least three mana of the same color was spent to cast this spell, Henge Walker enters the battlefield with a +1/+1 counter on it.", 2, 2, singletonList(ADAMANT_SAME_ENTER_PLUS_1_COUNTER));
    public static Card INSPIRING_VETERAN = new Card("Inspiring Veteran", asSet(Color.RED, Color.WHITE), asList(Cost.RED, Cost.WHITE), singletonList(CREATURE), asList(HUMAN, KNIGHT), UNCOMMON, "Other Knights you control get +1/+1.", 2, 2, singletonList(OTHER_KNIGHTS_YOU_CONTROL_GET_PLUS_1_1));
    public static Card JOUSTING_DUMMY = new Card("Jousting Dummy", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS), asList(ARTIFACT, CREATURE), asList(SCARECROW, KNIGHT), COMMON, "PAY 3: Jousting Dummy gets +1/+0 until end of turn.", 2, 1, singletonList(PAY_3_IT_GETS_PLUS_1_PLUS_0_UNTIL_END_OF_TURN));
    public static Card KNIGHT_OF_THE_KEEP = new Card("Knight of the Keep", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, KNIGHT), COMMON, "", 3, 2, emptyList());
    public static Card LASH_OF_THORNS = new Card("Lash of Thorns", singleton(Color.BLACK), singletonList(Cost.BLACK), singletonList(INSTANT), emptyList(), COMMON, "Target creature gets +2/+1 and gains deathtouch until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_PLUS_2_1_AND_DEATHTOUCH_UNTIL_END_OF_TURN));
    public static Card LOCTHWAIN_GARGOYLE = new Card("Locthwain Gargoyle", emptySet(), singletonList(Cost.COLORLESS), asList(ARTIFACT, CREATURE), singletonList(GARGOYLE), COMMON, "Pay 4: Locthwain Gargoyle gets +2/+0 and gains flying until end of turn.", 0, 3, singletonList(PAY_4_IT_GETS_PLUS_2_PLUS_0_AND_FLYING_UNTIL_END_OF_TURN));
    public static Card MARALEAF_PIXIE = new Card("Maraleaf Pixie", asSet(Color.GREEN, Color.BLUE), asList(Cost.GREEN, Cost.BLUE), singletonList(CREATURE), singletonList(FAERIE), UNCOMMON, "Flying. TAP: Add GREEN or BLUE.", 2, 2, asList(FLYING, TAP_ADD_GREEN_MANA, TAP_ADD_BLUE_MANA));
    public static Card OKOS_ACCOMPLICES = new Card("Oko's Accomplices", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(FAERIE), COMMON, "Flying", 2, 3, singletonList(FLYING));
    public static Card PRIZED_GRIFFIN = new Card("Prized Griffin", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(GRIFFIN), COMMON, "Flying", 3, 4, singletonList(FLYING));
    public static Card REAVE_SOUL = new Card("Reave Soul", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(SORCERY), emptyList(), COMMON, "Destroy target creature with power 3 or less.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_WITH_POWER_LESS_OR_EQUAL_3));
    public static Card RIGHTEOUSNESS = new Card("Righteousness", singleton(Color.WHITE), singletonList(Cost.WHITE), singletonList(INSTANT), emptyList(), COMMON, "Target blocking creature gets +7/+7 until end of turn.", 0, 0, singletonList(TARGET_BLOCKING_CREATURE_GETS_PLUS_7_7_END_OF_TURN));
    public static Card SPORECAP_SPIDER = new Card("Sporecap Spider", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(SPIDER), COMMON, "Reach", 1, 5, singletonList(REACH));
    public static Card TOME_RAIDER = new Card("Tome raider", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS), singletonList(CREATURE), singletonList(FAERIE), COMMON, "Flying. When Tome Raider enters the battlefield, draw a card.", 1, 1, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card VANTRESS_PALADIN = new Card("Vantress Paladin", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, KNIGHT), COMMON, "Flying. Adamant — If at least three blue mana was spent to cast this spell, Vantress Paladin enters the battlefield with an additional +1/+1 counter on it.", 2, 2, singletonList(ADAMANT_BLUE_ENTER_PLUS_1_COUNTER));
    public static Card WEASELBACK_REDCAP = new Card("Weaselback Redcap", singleton(Color.RED), singletonList(Cost.RED), singletonList(CREATURE), asList(GOBLIN, KNIGHT), COMMON, "PAY 1 1 RED: Weaselback Redcap gets +2/+0 until end of turn.", 1, 1, singletonList(PAY_1_1_RED_IT_GETS_PLUS_2_PLUS_0_UNTIL_END_OF_TURN));

    private static ThroneOfEldraine instance;

    private List<Card> cards = new ArrayList<>();

    private ThroneOfEldraine() {
        cards.add(ARDENVALE_PALADIN);
        cards.add(CLOCKWORK_SERVANT);
        cards.add(CRYSTAL_SLIPPER);
        cards.add(EMBERETH_PALADIN);
        cards.add(HENGE_WALKER);
        cards.add(JOUSTING_DUMMY);
        cards.add(INSPIRING_VETERAN);
        cards.add(KNIGHT_OF_THE_KEEP);
        cards.add(LASH_OF_THORNS);
        cards.add(MARALEAF_PIXIE);
        cards.add(OKOS_ACCOMPLICES);
        cards.add(PRIZED_GRIFFIN);
        cards.add(REAVE_SOUL);
        cards.add(RIGHTEOUSNESS);
        cards.add(SPORECAP_SPIDER);
        cards.add(TOME_RAIDER);
        cards.add(VANTRESS_PALADIN);
        cards.add(WEASELBACK_REDCAP);
    }

    @Override
    public String getName() {
        return "Throne of Eldraine";
    }

    @Override
    public String getCode() {
        return ELD;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static ThroneOfEldraine throneOfEldraine() {
        if (instance == null) {
            instance = new ThroneOfEldraine();
        }
        return instance;
    }
}
