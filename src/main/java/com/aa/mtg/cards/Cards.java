package com.aa.mtg.cards;

import com.aa.mtg.cards.properties.Rarity;
import com.aa.mtg.cards.properties.Type;

import static com.aa.mtg.cards.ability.Abilities.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;

public class Cards {

    public static Card PLAINS = new Card("Plains", emptySet(), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Plains"), Rarity.COMMON, "", 0, 0, singletonList(TAP_ADD_WHITE_MANA));
    public static Card ISLAND = new Card("Island", emptySet(), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Island"), Rarity.COMMON, "", 0, 0, singletonList(TAP_ADD_BLUE_MANA));
    public static Card SWAMP = new Card("Swamp", emptySet(), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Swamp"), Rarity.COMMON, "", 0, 0, singletonList(TAP_ADD_BLACK_MANA));
    public static Card MOUNTAIN = new Card("Mountain", emptySet(), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Mountain"), Rarity.COMMON, "", 0, 0, singletonList(TAP_ADD_RED_MANA));
    public static Card FOREST = new Card("Forest", emptySet(), emptyList(), asList(Type.BASIC, Type.LAND), singletonList("Forest"), Rarity.COMMON, "", 0, 0, singletonList(TAP_ADD_GREEN_MANA));

}
