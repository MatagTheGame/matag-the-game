package com.aa.mtg.game.turn.action;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.phases.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContinueTurnService {

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final UntapPhase untapPhase;
    private final UpkeepPhase upkeepPhase;
    private final DrawPhase drawPhase;
    private final Main1Phase main1Phase;
    private final BeforeCombatPhase beforeCombatPhase;
    private final DeclareAttackersPhase declareAttackersPhase;
    private final DeclareBlockersPhase declareBlockersPhase;
    private final FirstStrikePhase firstStrikePhase;
    private final CombatDamagePhase combatDamagePhase;
    private final EndOfCombatPhase endOfCombatPhase;
    private final Main2Phase main2Phase;
    private final EndTurnPhase endTurnPhase;
    private final CleanupPhase cleanupPhase;

    @Autowired
    public ContinueTurnService(GameStatusUpdaterService gameStatusUpdaterService, UntapPhase untapPhase, UpkeepPhase upkeepPhase, DrawPhase drawPhase,
                               Main1Phase main1Phase, BeforeCombatPhase beforeCombatPhase, DeclareAttackersPhase declareAttackersPhase, DeclareBlockersPhase declareBlockersPhase,
                               FirstStrikePhase firstStrikePhase, CombatDamagePhase combatDamagePhase, EndOfCombatPhase endOfCombatPhase, Main2Phase main2Phase,
                               EndTurnPhase endTurnPhase, CleanupPhase cleanupPhase) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.untapPhase = untapPhase;
        this.upkeepPhase = upkeepPhase;
        this.drawPhase = drawPhase;
        this.main1Phase = main1Phase;
        this.beforeCombatPhase = beforeCombatPhase;
        this.declareAttackersPhase = declareAttackersPhase;
        this.declareBlockersPhase = declareBlockersPhase;
        this.firstStrikePhase = firstStrikePhase;
        this.combatDamagePhase = combatDamagePhase;
        this.endOfCombatPhase = endOfCombatPhase;
        this.main2Phase = main2Phase;
        this.endTurnPhase = endTurnPhase;
        this.cleanupPhase = cleanupPhase;
    }

    public void continueTurn(GameStatus gameStatus) {
        switch (gameStatus.getTurn().getCurrentPhase()) {
            case UT:
                untapPhase.apply(gameStatus);
                break;

            case UP:
                upkeepPhase.apply(gameStatus);
                break;

            case DR:
                drawPhase.apply(gameStatus);
                break;

            case M1:
                main1Phase.apply(gameStatus);
                break;

            case BC:
                beforeCombatPhase.apply(gameStatus);
                break;

            case DA:
                declareAttackersPhase.apply(gameStatus);
                break;

            case DB:
                declareBlockersPhase.apply(gameStatus);
                break;

            case FS:
                firstStrikePhase.apply(gameStatus);
                break;

            case CD:
                combatDamagePhase.apply(gameStatus);
                break;

            case EC:
                endOfCombatPhase.apply(gameStatus);
                break;

            case M2:
                main2Phase.apply(gameStatus);
                break;

            case ET:
                endTurnPhase.apply(gameStatus);
                break;

            case CL:
                cleanupPhase.apply(gameStatus);
                break;
        }

        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }
}
