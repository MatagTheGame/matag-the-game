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
import static com.aa.mtg.cards.properties.Type.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

public class CoreSet2019 implements MtgSet {

    public static final String M19 = "M19";

    public static Card AEGIS_OF_THE_HEAVENS = new Card("Aegis of the Heavens", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), UNCOMMON, "Target creature gets +1/+7 until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_PLUS_1_7_UNTIL_END_OF_TURN));
    public static Card ANGEL_OF_THE_DAWN = new Card("Angel of the Dawn", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(ANGEL), COMMON, "Flying. When Angel of the Dawn enters the battlefield, creatures you control get +1/+1 and gain vigilance until end of turn.", 3, 3, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_1_1_AND_VIGILANCE_UNTIL_END_OF_TURN));
    public static Card BOGSTOMPER = new Card("Bogstomper", singleton(Color.BLACK), asList(Cost.BLACK, Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(BEAST), COMMON, "", 6, 5, emptyList());
    public static Card CENTAUR_COURSER = new Card("Centaur Courser", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(CENTAUR, WARRIOR), COMMON, "", 3, 3, emptyList());
    public static Card CHILD_OF_NIGHT = new Card("Child of Night", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(CREATURE), singletonList(VAMPIRE), COMMON, "Lifelink", 2, 1, singletonList(LIFELINK));
    public static Card CRASH_THROUGH = new Card("Crash Through", singleton(Color.RED), singletonList(Cost.RED), singletonList(SORCERY), emptyList(), COMMON, "Creatures you control gain trample until end of turn. Draw a card.", 0, 0, asList(CREATURES_YOU_CONTROL_GET_TRAMPLE_UNTIL_END_OF_TURN, DRAW_1_CARD));
    public static Card DAGGERBACK_BASILISK = new Card("Daggerback Basilisk", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(BASILISK), COMMON, "Deathtouch", 0, 0, singletonList(DEATHTOUCH));
    public static Card DAYBREAK_CHAPLAIN = new Card("Daybreak Chaplain", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, CLERIC), COMMON, "Lifelink", 1, 3, singletonList(LIFELINK));
    public static Card DISPERSE = new Card("Disperse", singleton(Color.BLUE), singletonList(Cost.BLUE), singletonList(INSTANT), emptyList(), COMMON, "Return target nonland permanent to its owner's hand.", 0, 0, singletonList(RETURN_TARGET_NONLAND_TO_ITS_OWNER_HAND));
    public static Card DIREGRAF_GHOUL = new Card("Diregraf Ghoul", singleton(Color.BLACK), singletonList(Cost.BLACK), singletonList(CREATURE), singletonList(ZOMBIE), UNCOMMON, "Diregraf Ghoul enters the battlefield tapped.", 2, 2, singletonList(ENTERS_THE_BATTLEFIELD_TAPPED));
    public static Card DRUID_OF_THE_COWL = new Card("Druid of the Cowl", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS), singletonList(CREATURE), asList(ELF, DRUID), COMMON, "TAP: add GREEN.", 1, 3, singletonList(TAP_ADD_GREEN_MANA));
    public static Card ELECTRIFY = new Card("Electrify", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Electrify deals 4 damage to target creature.", 0, 0, singletonList(DEAL_4_DAMAGE_TO_TARGET_CREATURE));
    public static Card EXCLUSION_MAGE = new Card("Exclusion Mage", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, WIZARD), UNCOMMON, "\tWhen Exclusion Mage enters the battlefield, return target creature an opponent controls to its owner's hand.", 2, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_RETURN_TARGET_OPPONENT_CREATURE_TO_ITS_OWNERS_HAND));
    public static Card FIELD_CREEPER = new Card("Field Creeper", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS), asList(ARTIFACT, CREATURE), singletonList(SCARECROW), COMMON, "", 2, 1, emptyList());
    public static Card FIERY_FINISH = new Card("Fiery Finish", singleton(Color.RED), asList(Cost.RED, Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(SORCERY), emptyList(), UNCOMMON, "Fiery Finish deals 7 damage to target creature.", 0, 0, singletonList(DEAL_7_DAMAGE_TO_TARGET_CREATURE));
    public static Card GIANT_SPIDER = new Card("Giant Spider", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(SPIDER), COMMON, "Reach.", 2, 4, singletonList(REACH));
    public static Card GIGANTOSAURUS = new Card("Gigantosaurus", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.GREEN, Cost.GREEN, Cost.GREEN), singletonList(CREATURE), singletonList(DINOSAUR), RARE, "", 10, 10, emptyList());
    public static Card GREENWOOD_SENTINEL = new Card("Greenwood Sentinel", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS), singletonList(CREATURE), asList(ELF, SCOUT), COMMON, "Vigilance.", 2, 2, singletonList(VIGILANCE));
    public static Card HAVOC_DEVILS = new Card("Havoc Devils", singleton(Color.RED), asList(Cost.RED, Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(DEVIL), COMMON, "Trample.", 4, 3, singletonList(TRAMPLE));
    public static Card HOSTILE_MINOTAUR = new Card("Hostile Minotaur", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(MINOTAUR), COMMON, "Haste.", 3, 3, singletonList(HASTE));
    public static Card INSPIRED_CHARGE = new Card("Inspired Charge", singleton(Color.WHITE), asList(Cost.WHITE, Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(INSTANT), emptyList(), COMMON, "Creatures you control get +2/+1 until end of turn.", 0, 0, singletonList(CREATURES_YOU_CONTROL_GET_PLUS_2_1_UNTIL_END_OF_TURN));
    public static Card KNIGHT_OF_THE_TUSK = new Card("Knight of the Tusk", singleton(Color.WHITE), asList(Cost.WHITE, Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(HUMAN, KNIGHT), COMMON, "Vigilance.", 3, 7, emptyList());
    public static Card KNIGHTS_PLEDGE = new Card("Knight's Pledge", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(ENCHANTMENT), emptyList(), COMMON, "Draw two cards.", 0, 0, singletonList(DRAW_2_CARDS));
    public static Card LAVA_AXE = new Card("Lava Axe", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(SORCERY), emptyList(), COMMON, "Lava Axe deals 5 damage to target player.", 0, 0, singletonList(DEAL_5_DAMAGE_TO_TARGET_PLAYER_OR_PLANESWALKER));
    public static Card LICHS_CARESS = new Card("Lich's Caress", singleton(Color.BLACK), asList(Cost.BLACK, Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(SORCERY), emptyList(), COMMON, "Destroy target creature. You gain 3 life.", 0, 0, asList(DESTROY_TARGET_CREATURE, GAIN_3_LIFE));
    public static Card LOXODON_LINE_BREAKER = new Card("Loxodon Line Breaker", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(ELEPHANT, SOLDIER), COMMON, "", 3, 2, emptyList());
    public static Card MARAUDERS_AXE = new Card("Marauder's Axe", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS), singletonList(ARTIFACT), singletonList(EQUIPMENT), COMMON, "Equipped creature gets +2/+0. Equip 2", 0, 0, singletonList(PAY_2_EQUIP_CREATURE_GETS_PLUS_2_0));
    public static Card METEOR_GOLEM = new Card("Meteor Golem", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), asList(ARTIFACT, CREATURE), singletonList(GOLEM), UNCOMMON, "When Meteor Golem enters the battlefield, destroy target nonland permanent an opponent controls.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_NONLAND_OPPONENT_PERMANENT_GET_DESTROYED));
    public static Card MIGHTY_LEAP = new Card("Mighty Leap", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(INSTANT), emptyList(), COMMON, "Target creature gets +2/+2 and gains flying until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_PLUS_2_2_AND_FLYING_UNTIL_END_OF_TURN));
    public static Card MURDER = new Card("Murder", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singletonList(INSTANT), emptyList(), COMMON, "Destroy target creature.", 0, 0, singletonList(DESTROY_TARGET_CREATURE));
    public static Card OAKENFORM = new Card("Oakenform", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(ENCHANTMENT), singletonList(AURA), COMMON, "Enchant creature. Enchanted creature gets +3/+3.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_3_3));
    public static Card ONAKKE_OGRE = new Card("Onakke Ogre", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(OGRE, WARRIOR), COMMON, "", 4, 2, emptyList());
    public static Card ORESKOS_SWIFTCLAW = new Card("Oreskos Swiftclaw", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList(CAT, WARRIOR), COMMON, "", 3, 1, emptyList());
    public static Card PRODIGIOUS_GROWTH = new Card("Prodigious Growth", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(ENCHANTMENT), singletonList(AURA), RARE, "Enchant creature. Enchanted creature gets +7/+7 and has trample.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_7_7_AND_TRAMPLE));
    public static Card RHOX_ORACLE = new Card("Rhox Oracle", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(RHINO, MONK), COMMON, "When Rhox Oracle enters the battlefield, draw a card.", 4, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    //public static Card RIDDLEMASTER_SPHINX = new Card("Riddlemaster Sphinx", singleton(Color.BLUE), asList(Cost.BLUE, Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Sphinx"), RARE, "Flying. When Riddlemaster Sphinx enters the battlefield, you may return target creature an opponent controls to its owner's hand.", 5, 5, asList(FLYING, )); // TODO Antonio: you may!
    public static Card RUSTWING_FALCON = new Card("Rustwing Falcon", singleton(Color.WHITE), singletonList(Cost.WHITE), singletonList(CREATURE), singletonList(BIRD), COMMON, "Flying.", 1, 2, singletonList(FLYING));
    public static Card SKELETON_ARCHER = new Card("Skeleton Archer", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList(SKELETON, ARCHER), COMMON, "When Skeleton Archer enters the battlefield, it deals 1 damage to any target.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_ANY_TARGET_GET_1_DAMAGE));
    public static Card SKYSCANNER = new Card("Skyscanner", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), asList(ARTIFACT, CREATURE), singletonList(THOPTER), COMMON, "Flying. When Skyscanner enters the battlefield, draw a card.", 1, 1, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card SNAPPING_DRAKE = new Card("Snapping Drake", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(DRAKE), COMMON, "Flying.", 3, 2, singletonList(FLYING));
    public static Card THORNHIDE_WOLVES = new Card("Thornhide Wolves", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(WOLF), COMMON, "", 4, 5, emptyList());
    //public static Card VAMPIRE_SOVEREIGN = new Card("Vampire Sovereign", singleton(Color.BLACK), asList(Cost.BLACK, Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(VAMPIRE), UNCOMMON, "Flying. When Vampire Sovereign enters the battlefield, target opponent loses 3 life and you gain 3 life.", 3, 4, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_OPPONENT_LOSES_3_LIFE_YOU_GAIN_3_LIFE));
    public static Card VIGILANT_BALOTH = new Card("Vigilant Baloth", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList(BEAST), UNCOMMON, "Vigilance", 5, 5, asList(VIGILANCE));
    public static Card WALKING_CORPSE = new Card("Walking Corpse", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(CREATURE), singletonList(ZOMBIE), COMMON, "", 2, 2, emptyList());

    private static CoreSet2019 instance;

    private List<Card> cards = new ArrayList<>();

    private CoreSet2019() {
        cards.add(AEGIS_OF_THE_HEAVENS);
        cards.add(Ixalan.AIR_ELEMENTAL);
        cards.add(ANGEL_OF_THE_DAWN);
        cards.add(Dominaria.BEFUDDLE);
        cards.add(BOGSTOMPER);
        cards.add(CENTAUR_COURSER);
        cards.add(CHILD_OF_NIGHT);
        cards.add(Ixalan.COLOSSAL_DREADMAW);
        cards.add(CRASH_THROUGH);
        cards.add(DAGGERBACK_BASILISK);
        cards.add(DAYBREAK_CHAPLAIN);
        cards.add(DISPERSE);
        cards.add(DIREGRAF_GHOUL);
        cards.add(Dominaria.DIVINATION);
        cards.add(DRUID_OF_THE_COWL);
        cards.add(ELECTRIFY);
        cards.add(EXCLUSION_MAGE);
        cards.add(FIELD_CREEPER);
        cards.add(FIERY_FINISH);
        cards.add(Dominaria.FIRE_ELEMENTAL);
        cards.add(GIANT_SPIDER);
        cards.add(GIGANTOSAURUS);
        cards.add(GREENWOOD_SENTINEL);
        cards.add(HAVOC_DEVILS);
        cards.add(HOSTILE_MINOTAUR);
        cards.add(INSPIRED_CHARGE);
        cards.add(Dominaria.INVOKE_THE_DIVINE);
        cards.add(KNIGHT_OF_THE_TUSK);
        cards.add(KNIGHTS_PLEDGE);
        cards.add(Dominaria.LLANOWAR_ELVES);
        cards.add(LAVA_AXE);
        cards.add(LICHS_CARESS);
        cards.add(LOXODON_LINE_BREAKER);
        cards.add(MARAUDERS_AXE);
        cards.add(METEOR_GOLEM);
        cards.add(MIGHTY_LEAP);
        cards.add(MURDER);
        cards.add(RivalsOfIxalan.NATURALIZE);
        cards.add(ONAKKE_OGRE);
        cards.add(OAKENFORM);
        cards.add(ORESKOS_SWIFTCLAW);
        cards.add(PRODIGIOUS_GROWTH);
        cards.add(RHOX_ORACLE);
        cards.add(RUSTWING_FALCON);
        cards.add(SKELETON_ARCHER);
        cards.add(SKYSCANNER);
        cards.add(RivalsOfIxalan.SUN_SENTINEL);
        cards.add(THORNHIDE_WOLVES);
        cards.add(Dominaria.TOLARIAN_SCOLAR);
        cards.add(VIGILANT_BALOTH);
        cards.add(WALKING_CORPSE);
    }

    @Override
    public String getName() {
        return "Core Set 2019";
    }

    @Override
    public String getCode() {
        return M19;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static CoreSet2019 m19() {
        if (instance == null) {
            instance = new CoreSet2019();
        }
        return instance;
    }
}
