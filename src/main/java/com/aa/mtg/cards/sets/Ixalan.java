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
import static com.aa.mtg.cards.properties.Type.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

public class Ixalan implements MtgSet {

    public static final String XLN = "XLN";

    public static Card AIR_ELEMENTAL = new Card("Air Elemental", singleton(Color.BLUE), asList(Cost.BLUE, Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Elemental"), UNCOMMON, "Flying", 4, 4, singletonList(FLYING));
    public static Card ANCIENT_BRONTODON = new Card("Ancient Brontodon", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "", 9, 9, emptyList());
    public static Card BISHOPS_SOLDIER = new Card("Bishop's Soldier", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList("Vampire", "Soldier"), COMMON, "", 2, 2, singletonList(LIFELINK));
    public static Card BRIGHT_REPRISAL = new Card("Bright Reprisal", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(INSTANT), emptyList(), UNCOMMON, "Destroy target attacking creature. Draw a card.", 0, 0, asList(DESTROY_TARGET_ATTACKING_CREATURE, DRAW_1_CARD));
    public static Card CHARGING_MONSTROSAUR = new Card("Charging Monstrosaur", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), UNCOMMON, "Trample, haste", 5, 5, asList(TRAMPLE, HASTE));
    public static Card COBBLED_WINGS = new Card("Cobbled Wings", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS), singletonList(ARTIFACT), singletonList("Equipment"), COMMON, "Equipped creature has flying. Equip 1", 0, 0, singletonList(PAY_1_EQUIP_CREATURE_GETS_FLYING));
    public static Card COLOSSAL_DREADMAW = new Card("Colossal Dreadmaw", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "Trample", 6, 6, singletonList(TRAMPLE));
    public static Card CRASH_THE_RAMPARTS = new Card("Crash the Ramparts", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(INSTANT), emptyList(), COMMON, "Target creature gets +3/+3 and gains trample until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_PLUS_3_3_AND_TRAMPLE_UNTIL_END_OF_TURN));
    public static Card DARK_NOURISHMENT = new Card("Dark Nourishment", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(INSTANT), emptyList(), UNCOMMON, "Dark Nourishment deals 3 damage to target creature or player. You gain 3 life.", 0, 0, asList(DEAL_3_DAMAGE_TO_TARGET_CREATURE, GAIN_3_LIFE));
    public static Card DEMOLISH = new Card("Demolish", singleton(Color.RED), singletonList(Cost.RED), singletonList(Type.SORCERY), emptyList(), COMMON, "Destroy target artifact or land.", 0, 0, singletonList(DESTROY_TARGET_ARTIFACT_OR_LAND));
    public static Card DEMYSTIFY = new Card("Demystify", singleton(Color.WHITE), singletonList(Cost.WHITE), singletonList(INSTANT), emptyList(), COMMON, "Destroy target enchantment.", 0, 0, singletonList(TARGET_ENCHANTMENT_GETS_DESTROYED));
    public static Card FRENZIED_RAPTOR = new Card("Frenzied Raptor", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "", 4, 2, emptyList());
    public static Card GILDED_SENTINEL = new Card("Gilded Sentinel", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), asList(ARTIFACT, CREATURE), singletonList("Golem"), COMMON, "", 3, 3, emptyList());
    public static Card GRAZING_WHIPTAIL = new Card("Grazing Whiptail", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "", 3, 4, singletonList(REACH));
    public static Card HEADWATER_SENTRIES = new Card("Headwater Sentries", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Merfolk Warrior"), COMMON, "", 2, 5, emptyList());
    public static Card HUATLIS_SNUBHORN = new Card("Huatli's Snubhorn", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "Vigilance", 2, 2, singletonList(VIGILANCE));
    public static Card IMPERIAL_AEROSAUR = new Card("Imperial Aerosaur", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), UNCOMMON, "Flying. When Imperial Aerosaur enters the battlefield, another target creature you control gets +1/+1 and gains flying until end of turn.", 3, 3, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_ANOTHER_TARGET_CREATURE_YOU_CONTROL_GETS_PLUS_1_1_AND_FLYING));
    public static Card INSPIRING_CLERIC = new Card("Inspiring Cleric", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Vampire", "Cleric"), UNCOMMON, "When Inspiring Cleric enters the battlefield, you gain 4 life.", 3, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_4_LIFE));
    public static Card LEGIONS_JUDGMENT = new Card("Legion's Judgment", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), COMMON, "Destroy target creature with power 4 or greater.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_WITH_POWER_GREATER_OR_EQUAL_4));
    public static Card LOOMING_ALTISAUR = new Card("Looming Altisaur", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "", 1, 7, emptyList());
    public static Card MARK_OF_THE_VAMPIRE = new Card("Mark of the Vampire", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(ENCHANTMENT), singletonList("Aura"), COMMON, "Enchant creature. Enchanted creature gets +2/+2 and has lifelink.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_2_2_AND_LIFELINK));
    public static Card NEST_ROBBER = new Card("Nest Robber", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "Haste", 2, 1, singletonList(HASTE));
    public static Card ONE_WITH_THE_WIND = new Card("One With the Wind", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS), singletonList(ENCHANTMENT), singletonList("Aura"), COMMON, "Enchant creature. Enchanted creature gets +2/+2 and has flying.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_2_2_AND_FLYING));
    public static Card QUEENS_BAY_SOLDIER = new Card("Queen's Bay Soldier", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(CREATURE), asList("Vampire", "Soldier"), COMMON, "", 2, 2, emptyList());
    public static Card RAGING_SWORDTOOTH = new Card("Raging Swordtooth", asSet(Color.RED, Color.GREEN), asList(Cost.RED, Cost.GREEN, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), UNCOMMON, "Trample. When Raging Swordtooth enters the battlefield, it deals 1 damage to each other creature.", 5, 5, asList(TRAMPLE, WHEN_IT_ENTERS_THE_BATTLEFIELD_OTHER_CREATURES_GET_1_DAMAGE));
    public static Card RAPTOR_COMPANION = new Card("Raptor Companion", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "", 3, 1, emptyList());
    public static Card RILE = new Card("Rile", singleton(Color.RED), singletonList(Cost.RED), singletonList(SORCERY), emptyList(), COMMON, "Rile deals 1 damage to target creature you control. That creature gains trample until end of turn. Draw a card.", 0, 0, asList(DEAL_1_DAMAGE_TO_CREATURE_YOU_CONTROL_THAT_CREATURE_GAINS_TRAMPLE, DRAW_1_CARD));
    public static Card SHINING_AEROSAUR = new Card("Shining Aerosaur", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "Flying.", 3, 4, singletonList(FLYING));
    public static Card SKYBLADE_OF_THE_LEGION = new Card("Skyblade of the Legion", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList("Vampire", "Solder"), COMMON, "Flying.", 1, 3, singletonList(FLYING));
    public static Card SWASHBUCKLING = new Card("SwashBuckling", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(ENCHANTMENT), singletonList("Aura"), COMMON, "Enchant creature. Enchanted creature gets +2/+2 and has haste.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_2_2_AND_HASTE));

    private static Ixalan instance;

    private List<Card> cards = new ArrayList<>();

    private Ixalan() {
        cards.add(AIR_ELEMENTAL);
        cards.add(ANCIENT_BRONTODON);
        cards.add(BISHOPS_SOLDIER);
        cards.add(BRIGHT_REPRISAL);
        cards.add(CHARGING_MONSTROSAUR);
        cards.add(COBBLED_WINGS);
        cards.add(COLOSSAL_DREADMAW);
        cards.add(CRASH_THE_RAMPARTS);
        cards.add(DARK_NOURISHMENT);
        cards.add(DEMYSTIFY);
        cards.add(DEMOLISH);
        cards.add(FRENZIED_RAPTOR);
        cards.add(GILDED_SENTINEL);
        cards.add(GRAZING_WHIPTAIL);
        cards.add(HEADWATER_SENTRIES);
        cards.add(HUATLIS_SNUBHORN);
        cards.add(IMPERIAL_AEROSAUR);
        cards.add(INSPIRING_CLERIC);
        cards.add(LEGIONS_JUDGMENT);
        cards.add(LOOMING_ALTISAUR);
        cards.add(MARK_OF_THE_VAMPIRE);
        cards.add(NEST_ROBBER);
        cards.add(ONE_WITH_THE_WIND);
        cards.add(QUEENS_BAY_SOLDIER);
        cards.add(RAGING_SWORDTOOTH);
        cards.add(RAPTOR_COMPANION);
        cards.add(RILE);
        cards.add(SHINING_AEROSAUR);
        cards.add(SKYBLADE_OF_THE_LEGION);
        cards.add(SWASHBUCKLING);
    }

    @Override
    public String getName() {
        return "Ixalan";
    }

    @Override
    public String getCode() {
        return XLN;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static Ixalan ixalan() {
        if (instance == null) {
            instance = new Ixalan();
        }
        return instance;
    }
}
