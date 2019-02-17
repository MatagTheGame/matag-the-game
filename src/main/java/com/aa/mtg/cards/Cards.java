package com.aa.mtg.cards;

import com.aa.mtg.cards.model.Card;
import com.aa.mtg.cards.model.FullCard;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Cards {

    public static Card PLAINS = new FullCard("Plains", singletonList(Color.WHITE), emptyList(), singletonList(Type.LAND), singletonList("Plains"), "", 0, 0);
    public static Card ISLAND = new FullCard("Island", singletonList(Color.BLUE), emptyList(), singletonList(Type.LAND), singletonList("Island"), "", 0, 0);
    public static Card SWAMP = new FullCard("Swamp", singletonList(Color.BLACK), emptyList(), singletonList(Type.LAND), singletonList("Swamp"), "", 0, 0);
    public static Card MOUNTAIN = new FullCard("Mountain", singletonList(Color.RED), emptyList(), singletonList(Type.LAND), singletonList("Mountain"), "", 0, 0);
    public static Card FOREST = new FullCard("Forest", singletonList(Color.GREEN), emptyList(), singletonList(Type.LAND), singletonList("Forest"), "", 0, 0);

}
