package com.aa.mtg.game.turn.phases;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public enum Phase {
    UP, DR, M1, BC, DA, DB, FS, CD, EC, M2, ET, CL;

    public static Phase nextPhase(Phase phase) {
        switch (phase) {
            case UP: return DR;
            case DR: return M1;
            case M1: return BC;
            case BC: return DA;
            case DA: return DB;
            case DB: return FS;
            case FS: return CD;
            case CD: return EC;
            case EC: return M2;
            case M2: return ET;
            case ET: return CL;
            case CL: return UP;
        }
        throw new RuntimeException("Cannot get next phase of " + phase);
    }

    public static Set<Phase> nonInstantAllowedPhases() {
        return new HashSet<>(asList(DR, CD, EC, CL));
    }

    public static Set<Phase> nonOpponentPhases() {
        return new HashSet<>(asList(DR, DA));
    }

    public static Set<Phase> nonPlayerPhases() {
        return new HashSet<>(singletonList(DB));
    }
}
