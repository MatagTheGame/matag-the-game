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
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.properties.Type.INSTANT;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.AZORIUS_GUILDGATE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

public class WarOfTheSpark implements MtgSet {

    public static final String WAR = "WAR";

    public static Card BANEHOUND = new Card("Banehound", singleton(Color.BLACK), singletonList(Cost.BLACK), singletonList(CREATURE), asList("Nightmare", "Hound"), COMMON, "Lifelink, haste", 1, 1, asList(LIFELINK, HASTE));
    public static Card BULWARK_GIANT = new Card("Bulwark Giant", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Giant", "Soldier"), COMMON, "When Bulwark Giant enters the battlefield, you gain 5 life.", 3, 6, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_5_LIFE));
    public static Card CHARITY_EXTRACTOR = new Card("Charity Extractor", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Knight"), COMMON, "Lifelink", 1, 5, singletonList(LIFELINK));
    public static Card DEFIANT_STRIKE = new Card("Defiant Strike", singleton(Color.WHITE), singletonList(Cost.WHITE), singletonList(Type.INSTANT), emptyList(), COMMON, "Target creature gets +1/+0 until end of turn. Draw a card.", 0, 0, asList(TARGET_CREATURE_GETS_PLUS_1_0_UNTIL_END_OF_TURN, DRAW_1_CARD));
    public static Card DESPERATE_LUNGE = new Card("Desperate Lunge", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Target creature gets +2/+2 and gains flying until end of turn. You gain 2 life.", 0, 0, asList(TARGET_CREATURE_GETS_PLUS_2_2_AND_FLYING_UNTIL_END_OF_TURN, GAIN_2_LIFE));
    public static Card DIVINE_ARROW = new Card("Divine Arrow", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Divine Arrow deals 4 damage to target attacking or blocking creature.", 0, 0, singletonList(DEAL_4_DAMAGE_TO_TARGET_ATTACKING_OR_BLOCKING_CREATURE));
    public static Card ELITE_GUARDMAGE = new Card("Elite Guardmage", asSet(Color.WHITE, Color.BLUE), asList(Cost.WHITE, Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Wizard"), UNCOMMON, "Flying. When Elite Guardmage enters the battlefield, you gain 3 life and draw a card.", 2, 3, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD_AND_GAIN_3_LIFE));
    public static Card GIANT_GROWTH = new Card("Giant Growth", singleton(Color.GREEN), singletonList(Cost.GREEN), singletonList(INSTANT), emptyList(), COMMON, "Target creature gets +3/+3 until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_PLUS_3_3_UNTIL_END_OF_TURN));
    public static Card ENFORCER_GRIFFIN = new Card("Enforcer Griffin", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Griffin"), COMMON, "Flying", 3, 4, singletonList(FLYING));
    public static Card GOBLIN_ASSAILANT = new Card("Goblin Assailant", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(CREATURE), asList("Goblin", "Warrior"), COMMON, "", 2, 2, emptyList());
    public static Card IRONCLAD_KROVOD = new Card("Ironclad Krovod", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Beast"), COMMON, "", 2, 5, emptyList());
    public static Card KRAUL_STINGER = new Card("Kraul Stinger", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Insect", "Assassin"), COMMON, "Deathtouch", 2, 2, singletonList(DEATHTOUCH));
    public static Card LAZOTEP_BEHEMOTH = new Card("Lazotep Behemoth", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Zombie", "Hippo"), COMMON, "", 5, 4, emptyList());
    public static Card LEYLINE_PROWLER = new Card("Leyline Prowler", asSet(Color.BLACK, Color.GREEN), asList(Cost.BLACK, Cost.GREEN, Cost.COLORLESS), singletonList(CREATURE), asList("Nightmare", "Beast"), UNCOMMON, "Deathtouch, lifelink. TAP: Add one mana of any color.", 2, 3, asList(DEATHTOUCH, LIFELINK, TAP_ADD_WHITE_MANA, TAP_ADD_BLUE_MANA, TAP_ADD_BLACK_MANA, TAP_ADD_RED_MANA, TAP_ADD_GREEN_MANA));
    public static Card LOXODON_SERGEANT = new Card("Loxodon Sergeant", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Elephant", "Soldier"), COMMON, "Vigilance. When Loxodon Sergeant enters the battlefield, other creatures you control gain vigilance until end of turn.", 3, 3, asList(VIGILANCE, WHEN_IT_ENTERS_THE_BATTLEFIELD_OTHER_CREATURES_YOU_CONTROL_GET_VIGILANCE_UNTIL_END_OF_TURN));
    public static Card NAGA_ETERNAL = new Card("Naga Eternal", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Zombie", "Naga"), COMMON, "", 3, 2, emptyList());
    public static Card THUNDERING_CERATOK = new Card("Thundering Ceratok", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Rhino"), COMMON, "Trample. When Thundering Ceratok enters the battlefield, other creatures you control gain trample until end of turn.", 4, 5, asList(TRAMPLE, WHEN_IT_ENTERS_THE_BATTLEFIELD_OTHER_CREATURES_YOU_CONTROL_GET_TRAMPLE_UNTIL_END_OF_TURN));
    public static Card TITHEBEARER_GIANT = new Card("Tithebearer Giant", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Giant", "Warrior"), COMMON, "When Tithebearer Giant enters the battlefield, you draw a card and you lose 1 life. When Thundering Ceratok enters the battlefield, other creatures you control gain trample until end of turn.", 4, 5, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD_AND_LOSE_1_LIFE));


    private static WarOfTheSpark instance;

    private List<Card> cards = new ArrayList<>();

    private WarOfTheSpark() {
        cards.add(AZORIUS_GUILDGATE);
        cards.add(BANEHOUND);
        cards.add(BULWARK_GIANT);
        cards.add(CHARITY_EXTRACTOR);
        cards.add(DEFIANT_STRIKE);
        cards.add(Ixalan.DEMOLISH);
        cards.add(DESPERATE_LUNGE);
        cards.add(DIVINE_ARROW);
        cards.add(ELITE_GUARDMAGE);
        cards.add(ENFORCER_GRIFFIN);
        cards.add(GIANT_GROWTH);
        cards.add(GOBLIN_ASSAILANT);
        cards.add(IRONCLAD_KROVOD);
        cards.add(KRAUL_STINGER);
        cards.add(LAZOTEP_BEHEMOTH);
        cards.add(LEYLINE_PROWLER);
        cards.add(LOXODON_SERGEANT);
        cards.add(NAGA_ETERNAL);
        cards.add(RavnicaAllegiance.ORZHOV_GUILDGATE);
        cards.add(Dominaria.PRIMORDIAL_WURM);
        cards.add(RavnicaAllegiance.SIMIC_GUILDGATE);
        cards.add(THUNDERING_CERATOK);
        cards.add(TITHEBEARER_GIANT);
    }

    @Override
    public String getName() {
        return "War of the Spark";
    }

    @Override
    public String getCode() {
        return WAR;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static WarOfTheSpark warOfTheSpark() {
        if (instance == null) {
            instance = new WarOfTheSpark();
        }
        return instance;
    }
}
