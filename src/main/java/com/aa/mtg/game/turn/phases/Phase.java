package com.aa.mtg.game.turn.phases;

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
}
