package com.aa.mtg.game.init;

import com.aa.mtg.game.turn.phases.PhaseConfig;

import java.util.List;

class InitPlayerPhasesConfigEvent {
    private List<PhaseConfig> phasesConfig;

    public InitPlayerPhasesConfigEvent(List<PhaseConfig> phasesConfig) {
        this.phasesConfig = phasesConfig;
    }

    public List<PhaseConfig> getPhasesConfig() {
        return phasesConfig;
    }
}
