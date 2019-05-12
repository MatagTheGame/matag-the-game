package com.aa.mtg.cards.ability.type;

import static java.util.Arrays.asList;

public enum AbilityType {
    DEATHTOUCH,
    FLYING,
    HASTE,
    REACH,
    VIGILANCE,
    TRAMPLE,

    DEALS_X_DAMAGE_TO_TARGET,
    DESTROY_TARGET,
    DRAW_X_CARDS,
    SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER,
    THAT_TARGETS_GET_X;

    public boolean isStatic() {
      return asList(DEATHTOUCH, FLYING, REACH, VIGILANCE, TRAMPLE).contains(this);
    }
}