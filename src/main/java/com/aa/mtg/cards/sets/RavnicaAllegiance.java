package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class RavnicaAllegiance {

    public static Card AXEBANE_BEAST = new Card("Axebane Beast", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Beast"), "", 3, 4);
    public static Card CATACOMB_CROCODILE = new Card("Catacomb Crocodile", singletonList(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Crocodile"), "", 3, 7);
    public static Card CORAL_COMMANDO = new Card("Coral Commando", singletonList(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Merfolk", "Warrior"), "", 3, 2);
    public static Card FERAL_MAAKA = new Card("Feral Maaka", singletonList(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Cat"), "", 2, 2);
    public static Card PROWLING_CARACAL = new Card("Prowling Caracal", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Cat"), "", 3, 1);

}
