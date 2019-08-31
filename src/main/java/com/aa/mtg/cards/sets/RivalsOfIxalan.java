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
import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.properties.Type.INSTANT;
import static com.aa.mtg.cards.properties.Type.SORCERY;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

public class RivalsOfIxalan implements MtgSet {

    public static final String RIX = "RIX";

    public static Card AGGRESSIVE_URGE = new Card("Aggressive Urge", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Target creature gets +1/+1 until end of turn. Draw a card.", 0, 0, asList(TARGET_CREATURE_GETS_PLUS_1_1_UNTIL_END_OF_TURN, DRAW_1_CARD));
    public static Card BOMBARD = new Card("Bombard", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Bombard deals 4 damage to target creature.", 0, 0, singletonList(DEAL_4_DAMAGE_TO_TARGET_CREATURE));
    public static Card CANAL_MONITOR = new Card("Canal Monitor", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Lizard"), COMMON, "", 5, 3, emptyList());
    public static Card DEAD_WEIGHT = new Card("Dead Weight", singleton(Color.BLACK), singletonList(Cost.BLACK), singletonList(Type.ENCHANTMENT), singletonList("Aura"), COMMON, "Enchant creature. Enchanted creature gets -2/-2.", 0, 0, singletonList(ENCHANTED_CREATURE_GETS_MINUS_2_2));
    public static Card DIVINE_VERDICT = new Card("Divine Verdict", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.INSTANT), emptyList(), COMMON, "Destroy target attacking or blocking creature.", 0, 0, singletonList(DESTROY_TARGET_ATTACKING_OF_BLOCKING_CREATURE));
    public static Card DUSK_LEGION_ZEALOT = new Card("Dusk Legion Zealot", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(CREATURE), asList("Vampire", "Soldier"), COMMON, "When Dusk Legion Zealot enters the battlefield, you draw a card and you lose 1 life.", 1, 1, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_DRAW_A_CARD_AND_LOSE_1_LIFE));
    public static Card EXULTANT_SKYMARCHER = new Card("Exultant Skymarcher", singleton(Color.WHITE), asList(Cost.WHITE, Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList("Vampire", "Soldier"), COMMON, "Flying.", 2, 3, singletonList(FLYING));
    public static Card IMPALE = new Card("Impale", singleton(Color.BLACK), asList(Cost.BLACK, Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singletonList(SORCERY), emptyList(), COMMON, "Destroy target creature.", 0, 0, singletonList(DESTROY_TARGET_CREATURE));
    public static Card JADECRAFT_ARTISAN = new Card("Jadecraft Artisan", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Merfolk", "Shaman"), COMMON, "When Jadecraft Artisan enters the battlefield, target creature gets +2/+2 until end of turn.", 3, 3, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_CREATURE_GETS_PLUS_2_2));
    public static Card MOMENT_OF_CRAVING = new Card("Moment of Craving", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS), singletonList(INSTANT), emptyList(), COMMON, "Target creature gets -2/-2 until end of turn. You gain 2 life.", 0, 0, asList(TARGET_CREATURE_GETS_MINUS_2_2_UNTIL_END_OF_TURN, GAIN_2_LIFE));
    public static Card ORAZCA_FRILLBACK = new Card("Orazca Frillback", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "", 4, 3, emptyList());
    public static Card ORAZCA_RAPTOR = new Card("Orazca Raptor", singleton(Color.RED), asList(Cost.RED, Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), COMMON, "", 3, 4, emptyList());
    public static Card RAVENOUS_CHUPACABRA = new Card("Ravenous Chupacabra", singleton(Color.BLACK), asList(Cost.BLACK, Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Beast", "Horror"), UNCOMMON, "When Ravenous Chupacabra enters the battlefield, destroy target creature an opponent controls.", 2, 2, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_TARGET_OPPONENT_CREATURE_GETS_DESTROYED));
    public static Card SUN_SENTINEL = new Card("Sun Sentinel", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), asList("Human", "Soldier"), COMMON, "Vigilance.", 2, 2, singletonList(VIGILANCE));
    public static Card STRIDER_HARNESS = new Card("Strider Harness", emptySet(), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(ARTIFACT), singletonList("Equipment"), COMMON, "Equipped creature gets +1/+1 and has haste. Equip 1", 0, 0, singletonList(PAY_1_EQUIP_CREATURE_GETS_PLUS_1_1_AND_HASTE));
    public static Card SWAB_GOBLIN = new Card("Swab Goblin", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(CREATURE), asList("Goblin", "Pirate"), COMMON, "", 2, 2, emptyList());
    public static Card SWORN_GUARDIAN = new Card("Sworn Guardian", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS), singletonList(CREATURE), asList("Merfolk", "Warrior"), COMMON, "", 1, 3, emptyList());
    public static Card VAMPIRE_CHAMPION = new Card("Vampire Champion", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Vampire", "Soldier"), COMMON, "Deathtouch.", 3, 3, singletonList(DEATHTOUCH));
    public static Card VAMPIRE_REVENANT = new Card("Vampire Revenant", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), asList("Vampire", "Spirit"), COMMON, "Flying.", 3, 1, singletonList(FLYING));

    private static RivalsOfIxalan instance;

    private List<Card> cards = new ArrayList<>();

    private RivalsOfIxalan() {
        cards.add(AGGRESSIVE_URGE);
        cards.add(BOMBARD);
        cards.add(CANAL_MONITOR);
        cards.add(Ixalan.COLOSSAL_DREADMAW);
        cards.add(DEAD_WEIGHT);
        cards.add(DIVINE_VERDICT);
        cards.add(DUSK_LEGION_ZEALOT);
        cards.add(EXULTANT_SKYMARCHER);
        cards.add(IMPALE);
        cards.add(JADECRAFT_ARTISAN);
        cards.add(MOMENT_OF_CRAVING);
        cards.add(ORAZCA_FRILLBACK);
        cards.add(ORAZCA_RAPTOR);
        cards.add(Ixalan.RAPTOR_COMPANION);
        cards.add(RAVENOUS_CHUPACABRA);
        cards.add(SUN_SENTINEL);
        cards.add(STRIDER_HARNESS);
        cards.add(SWAB_GOBLIN);
        cards.add(SWORN_GUARDIAN);
        cards.add(VAMPIRE_CHAMPION);
        cards.add(VAMPIRE_REVENANT);
    }

    @Override
    public String getName() {
        return "Rivals of Ixalan";
    }

    @Override
    public String getCode() {
        return RIX;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static RivalsOfIxalan rivalsOfIxalan() {
        if (instance == null) {
            instance = new RivalsOfIxalan();
        }
        return instance;
    }
}
