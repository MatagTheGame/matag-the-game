package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.combat.CombatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.Phase.EC;

@Component
public class CombatDamagePhase {

    private final CombatService combatService;

    @Autowired
    public CombatDamagePhase(CombatService combatService) {
        this.combatService = combatService;
    }

    public void apply(GameStatus gameStatus) {
        combatService.dealCombatDamage(gameStatus);

        if (!gameStatus.getTurn().isEnded()) {
            gameStatus.getTurn().setCurrentPhase(EC);
            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        }
    }
}
