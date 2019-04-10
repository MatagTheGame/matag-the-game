package com.aa.mtg.game.turn.phases;

public enum Phase {
    UT, UP, DR, M1, BC, DA, DB, FS, CD, EC, M2, ET, CL;

    public boolean isMainPhase() {
        return this == M1 || this == M2;
    }
}
