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
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

public class CoreSet2020 implements MtgSet {

    public static final String M20 = "M20";

    public static Card AGONIZING_SYPHON = new Card("Agonizing Syphon", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), COMMON, "Agonizing Syphon deals 3 damage to any target and you gain 3 life.", 0, 0, asList(DEAL_3_DAMAGE_TO_TARGET_CREATURE, GAIN_3_LIFE)); // FIXME this is bugged, and means that if the target disappears spell cannot be resolved and player doesn't gain the life
    public static Card ANGELIC_GIFT = new Card("Angelic Gift", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.ENCHANTMENT), singletonList("Aura"), COMMON, "Enchant creature. When Angelic Gift enters the battlefield, draw a card. Enchanted creature has flying.", 0, 0, asList(ENCHANTED_CREATURE_GETS_FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card BARONY_VAMPIRE = new Card("Barony Vampire", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Vampire"), COMMON, "", 3, 2, emptyList());
    public static Card BASTION_ENFORCER = new Card("Bastion Enforcer", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Dwarf", "Soldier"), COMMON, "", 3, 2, emptyList());
    public static Card CANOPY_SPIDER = new Card("Canopy Spider", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Spider"), COMMON, "", 1, 3, singletonList(REACH));
    public static Card CHANDRAS_OUTRAGE = new Card("Chandra's Outrage", singleton(Color.RED), asList(Cost.RED, Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Chandra's Outrage deals 4 damage to target creature and 2 damage to that creature's controller.", 0, 0, singletonList( DEAL_4_DAMAGE_TO_TARGET_CREATURE_2_DAMAGE_TO_CONTROLLER));
    public static Card CLOUDKIN_SEER = new Card("Cloudkin Seer", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Elemental", "Wizard"), COMMON, "Flying. When Cloudkin Seer enters the battlefield, draw a card.", 2, 1, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD));
    public static Card CORAL_MERFOLK = new Card("Coral Merfolk", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Merfolk"), COMMON, "", 2, 1, emptyList());
    public static Card DARK_REMEDY = new Card("Dark Remedy", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Target creature gets +1/+3 until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_PLUS_1_3_UNTIL_END_OF_TURN));
    public static Card DAWNING_ANGEL = new Card("Dawning Angel", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Angel"), COMMON, "Flying. When Dawning Angel enters the battlefield, you gain 4 life.", 3, 2, asList(FLYING, WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_4_LIFE));
    public static Card DISENCHANT = new Card("Disenchant", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Destroy target artifact or enchantment.", 0, 0, singletonList(DESTROY_TARGET_ARTIFACT_OR_ENCHANTMENT));
    public static Card DISFIGURE = new Card("Disfigure", singleton(Color.BLACK), singletonList(Cost.BLACK), singletonList(Type.INSTANT), emptyList(), UNCOMMON, "Target creature gets -2/-2 until end of turn.", 0, 0, singletonList(TARGET_CREATURE_GETS_MINUS_2_2_UNTIL_END_OF_TURN));
    public static Card ENGULFING_ERUPTION = new Card("Engulfing Eruption", singleton(Color.RED), asList(Cost.RED, Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), COMMON, "Engulfing Eruption deals 5 damage to target creature.", 0, 0, singletonList(DEAL_5_DAMAGE_TO_TARGET_CREATURE));

    private static CoreSet2020 instance;

    private List<Card> cards = new ArrayList<>();

    private CoreSet2020() {
        cards.add(AGONIZING_SYPHON);
        cards.add(Ixalan.AIR_ELEMENTAL);
        cards.add(ANGELIC_GIFT);
        cards.add(BARONY_VAMPIRE);
        cards.add(GuildsOfRavnica.BARTIZAN_BATS);
        cards.add(BASTION_ENFORCER);
        cards.add(Dominaria.BEFUDDLE);
        cards.add(RavnicaAllegiance.BLADEBRAND);
        cards.add(CoreSet2019.BOGSTOMPER);
        cards.add(CANOPY_SPIDER);
        cards.add(CoreSet2019.CENTAUR_COURSER);
        cards.add(CHANDRAS_OUTRAGE);
        cards.add(CLOUDKIN_SEER);
        cards.add(RavnicaAllegiance.CONCORDIA_PEGASUS);
        cards.add(CORAL_MERFOLK);
        cards.add(DARK_REMEDY);
        cards.add(DAWNING_ANGEL);
        cards.add(CoreSet2019.DAYBREAK_CHAPLAIN);
        cards.add(DISENCHANT);
        cards.add(DISFIGURE);
        cards.add(ENGULFING_ERUPTION);
        cards.add(WarOfTheSpark.GOBLIN_ASSAILANT);
        cards.add(WarOfTheSpark.IRONCLAD_KROVOD);
        cards.add(CoreSet2019.MARAUDERS_AXE);
        cards.add(CoreSet2019.OAKENFORM);
        cards.add(RavnicaAllegiance.PROWLING_CARACAL);
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
