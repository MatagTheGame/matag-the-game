package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import static com.aa.mtg.cards.ability.Abilities.DESTROY_TARGET_CREATURE_WITH_POWER_GREATER_OR_EQUAL_4;
import static com.aa.mtg.cards.ability.type.AbilityType.*;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Ixalan {

    public static Card AIR_ELEMENTAL = new Card("Air Elemental", singletonList(Color.BLUE), asList(Cost.BLUE, Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Elemental"), "Flying", 4, 4, singletonList(new Ability(FLYING)));
    public static Card ANCIENT_BRONTODON = new Card("Ancient Brontodon", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), "", 9, 9, emptyList());
    public static Card CHARGING_MONSTROSAUR = new Card("Charging Monstrosaur", singletonList(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), "Trample, haste", 5, 5, asList(new Ability(TRAMPLE), new Ability(HASTE)));
    public static Card COLOSSAL_DREADMAW = new Card("Colossal Dreadmaw", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), "Trample", 6, 6, singletonList(new Ability(TRAMPLE)));
    public static Card FRENZIED_RAPTOR = new Card("Frenzied Raptor", singletonList(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), "", 4, 2, emptyList());
    public static Card GRAZING_WHIPTAIL = new Card("Grazing Whiptail", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), "", 3, 4, singletonList(new Ability(REACH)));
    public static Card HEADWATER_SENTRIES = new Card("Headwater Sentries", singletonList(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(CREATURE), singletonList("Merfolk Warrior"), "", 2, 5, emptyList());
    public static Card LEGIONS_JUDGMENT = new Card("Legion's Judgment", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), "Destroy target creature with power 4 or greater.", 0, 0, singletonList(DESTROY_TARGET_CREATURE_WITH_POWER_GREATER_OR_EQUAL_4));
    public static Card NEST_ROBBER = new Card("Nest Robber", singletonList(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), "Haste", 2, 1, singletonList(new Ability(HASTE)));
    public static Card HUATLIS_SNUBHORN = new Card("Huatli's Snubhorn", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(CREATURE), singletonList("Dinosaur"), "Vigilance", 2, 2, singletonList(new Ability(VIGILANCE)));

}
