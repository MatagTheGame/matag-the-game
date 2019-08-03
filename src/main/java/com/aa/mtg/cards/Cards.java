package com.aa.mtg.cards;

import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Rarity;
import com.aa.mtg.cards.properties.Type;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

public class Cards {

    public static Card PLAINS = new Card("Plains", singleton(Color.WHITE), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Plains"), Rarity.COMMON, "", 0, 0, emptyList());
    public static Card ISLAND = new Card("Island", singleton(Color.BLUE), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Island"), Rarity.COMMON, "", 0, 0, emptyList());
    public static Card SWAMP = new Card("Swamp", singleton(Color.BLACK), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Swamp"), Rarity.COMMON, "", 0, 0, emptyList());
    public static Card MOUNTAIN = new Card("Mountain", singleton(Color.RED), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Mountain"), Rarity.COMMON, "", 0, 0, emptyList());
    public static Card FOREST = new Card("Forest", singleton(Color.GREEN), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Forest"), Rarity.COMMON, "", 0, 0, emptyList());

}
