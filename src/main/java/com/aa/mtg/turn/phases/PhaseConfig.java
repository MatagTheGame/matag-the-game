package com.aa.mtg.turn.phases;

public class PhaseConfig {
    private final Phase name;
    private final PhaseStatus status;

    public PhaseConfig(Phase name, PhaseStatus status) {
        this.name = name;
        this.status = status;
    }

    public Phase getName() {
        return name;
    }

    public PhaseStatus getStatus() {
        return status;
    }
}
