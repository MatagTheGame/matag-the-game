package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class CoreSet2020 implements MtgSet {

    public static final String M20 = "M20";

    public static Card ANGELIC_GIFT = new Card("Angelic Gift", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList("Aura"), "Enchant creature. When Angelic Gift enters the battlefield, draw a card. Enchanted creature has flying.", 0, 0, asList(ENCHANTED_CREATURE_GETS_FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card BARONY_VAMPIRE = new Card("Barony Vampire", singletonList(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Vampire"), "", 3, 2, emptyList());
    public static Card BARTIZAN_BATS = new Card("Bartizan_Bats", singletonList(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Bat"), "", 3, 1, singletonList(FLYING));
    public static Card BASTION_ENFORCER = new Card("Bastion Enforcer", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Dwarf", "Soldier"), "", 3, 2, emptyList());
    public static Card BOGSTOMPER = new Card("Bogstomper", singletonList(Color.BLACK), asList(Cost.BLACK, Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Beast"), "", 6, 5, emptyList());
    public static Card CANOPY_SPIDER = new Card("Canopy Spider", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Spider"), "", 1, 3, singletonList(REACH));
    public static Card CENTAUR_COURSER = new Card("Centaur Courser", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Centaur", "Warrior"), "", 3, 3, emptyList());
    public static Card CLOUDKIN_SEER = new Card("Cloudkin Seer", singletonList(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Elemental", "Wizard"), "Flying. When Cloudkin Seer enters the battlefield, draw a card.", 2, 1, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card CORAL_MERFOLK = new Card("Coral Merfolk", singletonList(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Merfolk"), "", 2, 1, emptyList());
    public static Card DAWNING_ANGEL = new Card("Dawning Angel", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Angel"), "Flying. When Dawning Angel enters the battlefield, you gain 4 life.", 3, 2, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_4_LIFE));
    public static Card DAYBREAK_CHAPLAIN = new Card("Daybreak Chaplain", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Human", "Cleric"), "Lifelink", 1, 3, singletonList(LIFELINK));
    public static Card MARAUDERS_AXE = new Card("Marauder's Axe", emptyList(), asList(Cost.COLORLESS, Cost.COLORLESS), singletonList(ARTIFACT), singletonList("Equipment"), "Equipped creature gets +2/+0. Equip 2", 0, 0, singletonList(PAY_2_EQUIP_CREATURE_GETS_PLUS_2_0));
    public static Card OAKENFORM = new Card("Oakenform", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList("Aura"), "Enchant creature. Enchanted creature gets +3/+3.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_PLUS_3_3));

    private static CoreSet2020 instance;

    private List<Card> cards = new ArrayList<>();

    private CoreSet2020() {
        cards.add(ANGELIC_GIFT);
        cards.add(BARONY_VAMPIRE);
        cards.add(BARTIZAN_BATS);
        cards.add(BASTION_ENFORCER);
        cards.add(BOGSTOMPER);
        cards.add(CANOPY_SPIDER);
        cards.add(CENTAUR_COURSER);
        cards.add(CLOUDKIN_SEER);
        cards.add(CORAL_MERFOLK);
        cards.add(DAWNING_ANGEL);
        cards.add(DAYBREAK_CHAPLAIN);
        cards.add(MARAUDERS_AXE);
        cards.add(OAKENFORM);
    }

    @Override
    public String getName() {
        return "Core Set 2020";
    }

    @Override
    public String getCode() {
        return M20;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static CoreSet2020 m20() {
        if (instance == null) {
            instance = new CoreSet2020();
        }
        return instance;
    }
}
