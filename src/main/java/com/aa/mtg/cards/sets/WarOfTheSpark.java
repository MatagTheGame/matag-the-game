package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

public class WarOfTheSpark implements MtgSet {

    public static final String WAR = "WAR";

    public static Card BANEHOUND = new Card("Banehound", singleton(Color.BLACK), singletonList(Cost.BLACK), singletonList(Type.CREATURE), asList("Nightmare", "Hound"), "Lifelink, haste", 1, 1, asList(LIFELINK, HASTE));
    public static Card BULWARK_GIANT = new Card("Bulwark Giant", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Giant", "Soldier"), "When Bulwark Giant enters the battlefield, you gain 5 life.", 3, 6, singletonList(WHEN_IT_ENTERS_THE_BATTLEFIELD_GAIN_5_LIFE));
    public static Card CHARITY_EXTRACTOR = new Card("Charity Extractor", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Human", "Knight"), "Lifelink", 1, 5, singletonList(LIFELINK));
    public static Card ENFORCER_GRIFFIN = new Card("Enforcer Griffin", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Griffin"), "Flying", 3, 4, singletonList(FLYING));
    public static Card GOBLIN_ASSAILANT = new Card("Goblin Assailant", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Goblin", "Warrior"), "", 2, 2, emptyList());
    public static Card IRONCLAD_KROVOD = new Card("Ironclad Krovod", singleton(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Beast"), "", 2, 5, emptyList());
    public static Card KRAUL_STINGER = new Card("Kraul Stinger", singleton(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Insect", "Assassin"), "Deathtouch", 2, 2, singletonList(DEATHTOUCH));
    public static Card LAZOTEP_BEHEMOTH = new Card("Lazotep Behemoth", singleton(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Zombie", "Hippo"), "", 5, 4, emptyList());
    public static Card NAGA_ETERNAL = new Card("Naga Eternal", singleton(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Zombie", "Naga"), "", 3, 2, emptyList());
    public static Card PRIMORDIAL_WURM = new Card("Primordial Wurm", singleton(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Wurm"), "", 7, 6, emptyList());

    private static WarOfTheSpark instance;

    private List<Card> cards = new ArrayList<>();

    private WarOfTheSpark() {
        cards.add(BANEHOUND);
        cards.add(BULWARK_GIANT);
        cards.add(CHARITY_EXTRACTOR);
        cards.add(ENFORCER_GRIFFIN);
        cards.add(GOBLIN_ASSAILANT);
        cards.add(IRONCLAD_KROVOD);
        cards.add(KRAUL_STINGER);
        cards.add(LAZOTEP_BEHEMOTH);
        cards.add(NAGA_ETERNAL);
        cards.add(PRIMORDIAL_WURM);
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
