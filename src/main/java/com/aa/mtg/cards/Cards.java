package com.aa.mtg.cards;

import com.aa.mtg.cards.properties.Rarity;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.properties.Type.BASIC;
import static com.aa.mtg.cards.properties.Type.LAND;
import static java.util.Collections.*;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

public class Cards {

    public static Card PLAINS = new Card("Plains", emptySet(), emptyList(), asSet(BASIC, LAND), emptySet(), Rarity.COMMON, "", 0, 0, singletonList(TAP_ADD_WHITE_MANA));
    public static Card ISLAND = new Card("Island", emptySet(), emptyList(), asSet(BASIC, LAND), emptySet(), Rarity.COMMON, "", 0, 0, singletonList(TAP_ADD_BLUE_MANA));
    public static Card SWAMP = new Card("Swamp", emptySet(), emptyList(), asSet(BASIC, LAND), emptySet(), Rarity.COMMON, "", 0, 0, singletonList(TAP_ADD_BLACK_MANA));
    public static Card MOUNTAIN = new Card("Mountain", emptySet(), emptyList(), asSet(BASIC, LAND), emptySet(), Rarity.COMMON, "", 0, 0, singletonList(TAP_ADD_RED_MANA));
    public static Card FOREST = new Card("Forest", emptySet(), emptyList(), asSet(BASIC, LAND), emptySet(), Rarity.COMMON, "", 0, 0, singletonList(TAP_ADD_GREEN_MANA));

}
