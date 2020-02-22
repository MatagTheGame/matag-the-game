package com.aa.mtg.game.turn.phases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.AfterDeclareBlockersPhase.AB;
import static com.aa.mtg.game.turn.phases.BeginCombatPhase.BC;
import static com.aa.mtg.game.turn.phases.CleanupPhase.CL;
import static com.aa.mtg.game.turn.phases.CombatDamagePhase.CD;
import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;
import static com.aa.mtg.game.turn.phases.DeclareBlockersPhase.DB;
import static com.aa.mtg.game.turn.phases.DrawPhase.DR;
import static com.aa.mtg.game.turn.phases.EndOfCombatPhase.EC;
import static com.aa.mtg.game.turn.phases.EndTurnPhase.ET;
import static com.aa.mtg.game.turn.phases.FirstStrikePhase.FS;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;
import static com.aa.mtg.game.turn.phases.UntapPhase.UT;
import static com.aa.mtg.game.turn.phases.UpkeepPhase.UP;

@Component
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
  private final CombatDamagePhase combatDamagePhase;
  private final EndOfCombatPhase endOfCombatPhase;
  private final Main2Phase main2Phase;
  private final EndTurnPhase endTurnPhase;
  private final CleanupPhase cleanupPhase;

  @Autowired
  public PhaseFactory(UntapPhase untapPhase, UpkeepPhase upkeepPhase, DrawPhase drawPhase,
                      Main1Phase main1Phase, BeginCombatPhase beginCombatPhase, DeclareAttackersPhase declareAttackersPhase, DeclareBlockersPhase declareBlockersPhase,
                      AfterDeclareBlockersPhase afterDeclareBlockersPhase, FirstStrikePhase firstStrikePhase, CombatDamagePhase combatDamagePhase, EndOfCombatPhase endOfCombatPhase, Main2Phase main2Phase,
                      EndTurnPhase endTurnPhase, CleanupPhase cleanupPhase) {
    this.untapPhase = untapPhase;
    this.upkeepPhase = upkeepPhase;
    this.drawPhase = drawPhase;
    this.main1Phase = main1Phase;
    this.beginCombatPhase = beginCombatPhase;
    this.declareAttackersPhase = declareAttackersPhase;
    this.declareBlockersPhase = declareBlockersPhase;
    this.afterDeclareBlockersPhase = afterDeclareBlockersPhase;
    this.firstStrikePhase = firstStrikePhase;
    this.combatDamagePhase = combatDamagePhase;
    this.endOfCombatPhase = endOfCombatPhase;
    this.main2Phase = main2Phase;
    this.endTurnPhase = endTurnPhase;
    this.cleanupPhase = cleanupPhase;
  }

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
