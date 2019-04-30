package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.target.TargetType;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;

import static com.aa.mtg.cards.ability.type.AbilityType.DEALS_X_DAMAGE_TO_TARGET;
import static com.aa.mtg.cards.ability.type.AbilityType.DEATHTOUCH;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class RavnicaAllegiance {

    public static Card AXEBANE_BEAST = new Card("Axebane Beast", singletonList(Color.GREEN), asList(Cost.GREEN, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Beast"), "", 3, 4, emptyList());
    public static Card CATACOMB_CROCODILE = new Card("Catacomb Crocodile", singletonList(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Crocodile"), "", 3, 7, emptyList());
    public static Card CORAL_COMMANDO = new Card("Coral Commando", singletonList(Color.BLUE), asList(Cost.BLUE, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), asList("Merfolk", "Warrior"), "", 3, 2, emptyList());
    public static Card FERAL_MAAKA = new Card("Feral Maaka", singletonList(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Cat"), "", 2, 2, emptyList());
    public static Card NOXIOUS_GROODION = new Card("Noxious Groodion", singletonList(Color.BLACK), asList(Cost.BLACK, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Beast"), "Deathtouch", 2, 2, singletonList(new Ability(DEATHTOUCH)));
    public static Card PRECISION_BOLT = new Card("Precision Bolt", singletonList(Color.RED), asList(Cost.RED, Cost.COLORLESS, Cost.COLORLESS), singletonList(Type.SORCERY), emptyList(), "Precision Bolt deals 3 damage to any target.", 0, 0, singletonList(new Ability(DEALS_X_DAMAGE_TO_TARGET, singletonList(Target.builder().targetType(TargetType.ANY).build()), singletonList("3"))));
    public static Card PROWLING_CARACAL = new Card("Prowling Caracal", singletonList(Color.WHITE), asList(Cost.WHITE, Cost.COLORLESS), singletonList(Type.CREATURE), singletonList("Cat"), "", 3, 1, emptyList());

}
