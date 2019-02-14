package com.aa.mtg.cards;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Cards {

    public static Card PLAINS = new Card("Plains", singletonList(Color.WHITE), emptyList(), singletonList(Type.LAND), singletonList("Plains"), "", 0, 0);
    public static Card ISLAND = new Card("Island", singletonList(Color.BLUE), emptyList(), singletonList(Type.LAND), singletonList("Island"), "", 0, 0);
    public static Card SWAMP = new Card("Swamp", singletonList(Color.BLACK), emptyList(), singletonList(Type.LAND), singletonList("Swamp"), "", 0, 0);
    public static Card MOUNTAIN = new Card("Mountain", singletonList(Color.RED), emptyList(), singletonList(Type.LAND), singletonList("Mountain"), "", 0, 0);
    public static Card FOREST = new Card("Forest", singletonList(Color.GREEN), emptyList(), singletonList(Type.LAND), singletonList("Forest"), "", 0, 0);

    public static Card ALPHA_TYRRANAX = new Card("Alpha Tyrranax", singletonList(Color.GREEN), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.GREEN, Cost.GREEN), singletonList(Type.CREATURE), singletonList("Beast"), "", 6, 5);
    public static Card FERAL_MAAKA = new Card("Feral Maaka", singletonList(Color.RED), asList(Cost.COLORLESS, Cost.RED), singletonList(Type.CREATURE), singletonList("Cat"), "", 2, 2);
}
