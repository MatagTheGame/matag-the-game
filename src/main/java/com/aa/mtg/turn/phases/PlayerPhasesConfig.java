package com.aa.mtg.turn.phases;

import java.util.ArrayList;
import java.util.List;

public class PlayerPhasesConfig {
    private List<PhaseConfig> config;

    public PlayerPhasesConfig() {
        this.config = new ArrayList<>();
    }

    public List<PhaseConfig> getConfig() {
        return config;
    }

    public static PlayerPhasesConfig defaultPlayerPhasesConfig() {
        PlayerPhasesConfig playerPhasesConfig = new PlayerPhasesConfig();

        playerPhasesConfig.config.add(new PhaseConfig(Phase.UP, PhaseStatus.DISABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.DR, PhaseStatus.DISABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.M1, PhaseStatus.ENABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.BC, PhaseStatus.DISABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.DA, PhaseStatus.ENABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.DB, PhaseStatus.ENABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.FS, PhaseStatus.DISABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.CD, PhaseStatus.DISABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.EC, PhaseStatus.DISABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.M2, PhaseStatus.ENABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.ET, PhaseStatus.DISABLED));
        playerPhasesConfig.config.add(new PhaseConfig(Phase.CL, PhaseStatus.DISABLED));

        return playerPhasesConfig;
    }
}
