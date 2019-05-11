package com.aa.mtg.cards.ability.type;

import static java.util.Arrays.asList;

public enum AbilityType {
    DEATHTOUCH,
    FLYING,
    HASTE,
    REACH,
    VIGILANCE,
    TRAMPLE,
    DESTROY_TARGET,
    DEALS_X_DAMAGE_TO_TARGET,
    SHUFFLE_GRAVEYARD_INTO_LIBRARY_FOR_TARGET_PLAYER,
    DRAW_X_CARDS;

    public boolean isStatic() {
      return asList(DEATHTOUCH, FLYING, REACH, VIGILANCE, TRAMPLE).contains(this);
    }
}