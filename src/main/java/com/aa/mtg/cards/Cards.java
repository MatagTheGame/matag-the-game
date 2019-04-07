package com.aa.mtg.cards;

import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Cards {

    public static Card PLAINS = new Card("Plains", singletonList(Color.WHITE), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Plains"), "", 0, 0, emptyList());
    public static Card ISLAND = new Card("Island", singletonList(Color.BLUE), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Island"), "", 0, 0, emptyList());
    public static Card SWAMP = new Card("Swamp", singletonList(Color.BLACK), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Swamp"), "", 0, 0, emptyList());
    public static Card MOUNTAIN = new Card("Mountain", singletonList(Color.RED), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Mountain"), "", 0, 0, emptyList());
    public static Card FOREST = new Card("Forest", singletonList(Color.GREEN), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Forest"), "", 0, 0, emptyList());

}
