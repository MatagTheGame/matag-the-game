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
import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.properties.Type.ENCHANTMENT;
import static com.aa.mtg.cards.properties.Type.SORCERY;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

public class CoreSet2019 implements MtgSet {

    public static final String M19 = "M19";

    public static Card AEGIS_OF_THE_HEAVENS = new Card("Aegis of the Heavens", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), UNCOMMON, "Target creature gets +1/+7 until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_PLUS_1_7_UNTIL_END_OF_TURN));
    public static Card ANGEL_OF_THE_DAWN = new Card("Angel of the Dawn", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Angel"), COMMON, "Flying. When Angel of the Dawn enters the battlefield, creatures you control get +1/+1 and gain vigilance until end of turn.", 3, 3, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_CREATURES_YOU_CONTROL_GET_PLUS_1_1_AND_VIGILANCE_UNTIL_END_OF_TURN));
    public static Card BOGSTOMPER = new Card("Bogstomper", singleton(Color.BLACK), asList(Cost.BLACK, Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Beast"), COMMON, "", 6, 5, emptyList());
    public static Card CENTAUR_COURSER = new Card("Centaur Courser", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Centaur", "Warrior"), COMMON, "", 3, 3, emptyList());
    public static Card CHILD_OF_NIGHT = new Card("Child of Night", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(CREATURE), singletonList("Vampire"), COMMON, "Lifelink", 2, 1, singletonList(LIFELINK));
    public static Card CRASH_THROUGH = new Card("Crash Through", singleton(Color.RED), singletonList(Cost.RED), singletonList(SORCERY), emptyList(), COMMON, "Creatures you control gain trample until end of turn. Draw a card.", 0, 0, asList(CREATURES_YOU_CONTROL_GET_TRAMPLE_UNTIL_END_OF_TURN, DRAW_1_CARD));
    public static Card DAGGERBACK_BASILISK = new Card("Daggerback Basilisk", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Basilisk"), COMMON, "Deathtouch", 0, 0, singletonList(DEATHTOUCH));
    public static Card DAYBREAK_CHAPLAIN = new Card("Daybreak Chaplain", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Cleric"), COMMON, "Lifelink", 1, 3, singletonList(LIFELINK));
    public static Card DIREGRAF_GHOUL = new Card("Diregraf Ghoul", singleton(Color.BLACK), singletonList(Cost.BLACK), singletonList(CREATURE), singletonList("Zombie"), UNCOMMON, "Diregraf Ghoul enters the battlefield tapped.", 2, 2, singletonList(ENTERS_THE_BATTLEFIELD_TAPPED));
    public static Card ELECTRIFY = new Card("Electrify", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Electrify deals 4 damage to target creature.", 0, 0, singletonList(DEAL_4_DAMAGE_TO_TARGET_CREATURE));
    public static Card EXCLUSION_MAGE = new Card("Exclusion Mage", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Wizard"), UNCOMMON, "\tWhen Exclusion Mage enters the battlefield, return target creature an opponent controls to its owner's hand.", 2, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_RETURN_TARGET_OPPONENT_CREATURE_TO_ITS_OWNERS_HAND));
    public static Card FIELD_CREEPER = new Card("Field Creeper", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS), asList(ARTIFACT, CREATURE), singletonList("Scarecrow"), COMMON, "", 2, 1, emptyList());
    public static Card GIANT_SPIDER = new Card("Giant Spider", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Spider"), COMMON, "Reach.", 2, 4, singletonList(REACH));
    public static Card GIGANTOSAURUS = new Card("Gigantosaurus", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.GREEN, Cost.GREEN, Cost.GREEN), singletonList(CREATURE), singletonList("Dinosaur"), RARE, "", 10, 10, emptyList());
    public static Card GREENWOOD_SENTINEL = new Card("Greenwood Sentinel", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS), singletonList(CREATURE), asList("Elf", "Scout"), COMMON, "Vigilance.", 2, 2, singletonList(VIGILANCE));
    public static Card HAVOC_DEVILS = new Card("Havoc Devils", singleton(Color.RED), asList(Cost.RED, Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Devil"), COMMON, "Trample.", 4, 3, singletonList(TRAMPLE));
    public static Card HOSTILE_MINOTAUR = new Card("Hostile Minotaur", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Minotaur"), COMMON, "Haste.", 3, 3, singletonList(HASTE));
    public static Card KNIGHT_OF_THE_TUSK = new Card("Knight of the Tusk", singleton(Color.WHITE), asList(Cost.WHITE, Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Knight"), COMMON, "Vigilance.", 3, 7, emptyList());
    public static Card KNIGHTS_PLEDGE = new Card("Knight's Pledge", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(ENCHANTMENT), emptyList(), COMMON, "Draw two cards.", 0, 0, singletonList(DRAW_2_CARD));
    public static Card LOXODON_LINE_BREAKER = new Card("Loxodon Line Breaker", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Elephant", "Soldier"), COMMON, "", 3, 2, emptyList());
    public static Card MARAUDERS_AXE = new Card("Marauder's Axe", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS), singletonList(ARTIFACT), singletonList("Equipment"), COMMON, "Equipped creature gets +2/+0. Equip 2", 0, 0, singletonList(PAY_2_EQUIP_CREATURE_GETS_PLUS_2_0));
    public static Card METEOR_GOLEM = new Card("Meteor Golem", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), asList(ARTIFACT, CREATURE), singletonList("Golem"), UNCOMMON, "When Meteor Golem enters the battlefield, destroy target nonland permanent an opponent controls.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_NONLAND_OPPONENT_PERMANENT_GET_DESTROYED));
    public static Card OAKENFORM = new Card("Oakenform", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList("Aura"), COMMON, "Enchant creature. Enchanted creature gets +3/+3.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_3_3));
    public static Card PRODIGIOUS_GROWTH = new Card("Prodigious Growth", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(ENCHANTMENT), singletonList("Aura"), RARE, "Enchant creature. Enchanted creature gets +7/+7 and has trample.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_7_7_AND_TRAMPLE));

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
        cards.add(DIREGRAF_GHOUL);
        cards.add(Dominaria.DIVINATION);
        cards.add(ELECTRIFY);
        cards.add(EXCLUSION_MAGE);
        cards.add(FIELD_CREEPER);
        cards.add(Dominaria.FIRE_ELEMENTAL);
        cards.add(GIANT_SPIDER);
        cards.add(GIGANTOSAURUS);
        cards.add(GREENWOOD_SENTINEL);
        cards.add(HAVOC_DEVILS);
        cards.add(HOSTILE_MINOTAUR);
        cards.add(KNIGHT_OF_THE_TUSK);
        cards.add(KNIGHTS_PLEDGE);
        cards.add(LOXODON_LINE_BREAKER);
        cards.add(MARAUDERS_AXE);
        cards.add(METEOR_GOLEM);
        cards.add(OAKENFORM);
        cards.add(PRODIGIOUS_GROWTH);
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
