package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import static com.aa.mtg.cards.ability.Ability.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Ixalan {

    public static Card AIR_ELEMENTAL = new Card("Air Elemental", singletonList(Color.BLUE), asList(Cost.BLUE, Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Elemental"), "Flying", 4, 4, singletonList(FLYING));
    public static Card ANCIENT_BRONTODON = new Card("Ancient Brontodon", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Dinosaur"), "", 9, 9, emptyList());
    public static Card FRENZIED_RAPTOR = new Card("Frenzied Raptor", singletonList(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Dinosaur"), "", 4, 2, emptyList());
    public static Card GRAZING_WHIPTAIL = new Card("Grazing Whiptail", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Dinosaur"), "", 3, 4, singletonList(REACH));
    public static Card HEADWATER_SENTRIES = new Card("Headwater Sentries", singletonList(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Merfolk Warrior"), "", 2, 5, emptyList());
    public static Card LEGIONS_JUDGMENT = new Card("Legion's Judgment", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), "Destroy target creature with power 4 or greater.", 2, 5, singletonList(DESTROY_TARGET_CREATURE));
    public static Card NEST_ROBBER = new Card("Nest Robber", singletonList(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Dinosaur"), "Haste", 2, 1, singletonList(HASTE));
    public static Card HUATLIS_SNUBHORN = new Card("Huatli's Snubhorn", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Dinosaur"), "Vigilance", 2, 2, singletonList(VIGILANCE));

}
