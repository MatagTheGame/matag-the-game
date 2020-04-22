package com.matag.game.turn.phases;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.AfterDeclareBlockersPhase.AB;
import static com.matag.game.turn.phases.AfterFirstStrikePhase.AF;
import static com.matag.game.turn.phases.BeginCombatPhase.BC;
import static com.matag.game.turn.phases.CleanupPhase.CL;
import static com.matag.game.turn.phases.CombatDamagePhase.CD;
import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.DrawPhase.DR;
import static com.matag.game.turn.phases.EndOfCombatPhase.EC;
import static com.matag.game.turn.phases.EndTurnPhase.ET;
import static com.matag.game.turn.phases.FirstStrikePhase.FS;
import static com.matag.game.turn.phases.Main1Phase.M1;
import static com.matag.game.turn.phases.Main2Phase.M2;
import static com.matag.game.turn.phases.UntapPhase.UT;
import static com.matag.game.turn.phases.UpkeepPhase.UP;

@Component
@AllArgsConstructor
public class PhaseFactory {
  private final UntapPhase untapPhase;
  private final UpkeepPhase upkeepPhase;
  private final DrawPhase drawPhase;
  private final Main1Phase main1Phase;
  private final BeginCombatPhase beginCombatPhase;
  private final DeclareAttackersPhase declareAttackersPhase;
  private final DeclareBlockersPhase declareBlockersPhase;
  private final AfterDeclareBlockersPhase afterDeclareBlockersPhase;
  private final FirstStrikePhase firstStrikePhase;
  private final AfterFirstStrikePhase afterFirstStrikePhase;
  private final CombatDamagePhase combatDamagePhase;
  private final EndOfCombatPhase endOfCombatPhase;
  private final Main2Phase main2Phase;
  private final EndTurnPhase endTurnPhase;
  private final CleanupPhase cleanupPhase;

  public Phase get(String phase) {
    switch (phase) {
      case UT:
        return untapPhase;

      case UP:
        return upkeepPhase;

      case DR:
        return drawPhase;

      case M1:
        return main1Phase;

      case BC:
        return beginCombatPhase;

      case DA:
        return declareAttackersPhase;

      case DB:
        return declareBlockersPhase;

      case AB:
        return afterDeclareBlockersPhase;

      case FS:
        return firstStrikePhase;

      case AF:
        return afterFirstStrikePhase;

      case CD:
        return combatDamagePhase;

      case EC:
        return endOfCombatPhase;

      case M2:
        return main2Phase;

      case ET:
        return endTurnPhase;

      case CL:
        return cleanupPhase;

      default:
        throw new UnsupportedOperationException("Phase not valid");
    }
  }
}
