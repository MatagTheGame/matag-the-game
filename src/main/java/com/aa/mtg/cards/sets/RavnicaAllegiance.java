package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.model.Card;
import com.aa.mtg.cards.model.FullCard;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class RavnicaAllegiance {

    public static Card FERAL_MAAKA = new FullCard("Feral Maaka", singletonList(Color.RED), asList(Cost.COLORLESS, Cost.RED), singletonList(Type.CREATURE), singletonList("Cat"), "", 2, 2);
    public static Card AXEBANE_BEAST = new FullCard("Axebane Beast", singletonList(Color.GREEN), asList(Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.GREEN), singletonList(Type.CREATURE), singletonList("Beast"), "", 3, 4);

}
