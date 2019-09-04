package com.aa.mtg.cards.selector;

import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static java.util.Collections.singletonList;

public class CardInstanceSelectors {
    public static final CardInstanceSelector CREATURES_YOU_CONTROL = CardInstanceSelector.builder().selectorType(SelectorType.PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build();
    public static final CardInstanceSelector OTHER_CREATURES_YOU_CONTROL = CardInstanceSelector.builder().selectorType(SelectorType.PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).others(true).build();
    public static final CardInstanceSelector OTHER_CREATURES = CardInstanceSelector.builder().selectorType(SelectorType.PERMANENT).ofType(singletonList(CREATURE)).others(true).build();
}
