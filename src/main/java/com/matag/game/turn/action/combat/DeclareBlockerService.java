package com.matag.game.turn.action.combat;

import static com.matag.cards.ability.trigger.TriggerSubtype.WHEN_BLOCK;
import static com.matag.game.turn.action._continue.InputRequiredActions.DECLARE_BLOCKERS;
import static com.matag.game.turn.phases.combat.DeclareBlockersPhase.DB;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.ContinueService;
import com.matag.game.turn.action.trigger.WhenTriggerService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DeclareBlockerService {
  private final ContinueService continueService;
  private final BlockerChecker blockerChecker;
  private final WhenTriggerService whenTriggerService;

  public void declareBlockers(GameStatus gameStatus, Map<Integer, List<Integer>> blockingCreaturesIdsForAttackingCreaturesIds) {
    var turn = gameStatus.getTurn();
    var currentPlayer = gameStatus.getCurrentPlayer();
    var nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

    if (!turn.getCurrentPhase().equals(DB) || !DECLARE_BLOCKERS.equals(turn.getInputRequiredAction())) {
      throw new RuntimeException("Cannot declare blockers. Phase=" + turn.getCurrentPhase());
    }

    blockingCreaturesIdsForAttackingCreaturesIds.forEach((blockingCreatureId, blockedCreaturesIds) -> {
      var attacker = currentPlayer.getBattlefield().findCardById(blockedCreaturesIds.get(0));
      var blockers = findBlockers(blockingCreaturesIdsForAttackingCreaturesIds, nonCurrentPlayer, attacker);
      blockerChecker.checkIfCanBlock(attacker, blockers);
    });

    blockingCreaturesIdsForAttackingCreaturesIds.forEach((blockingCreatureId, blockedCreaturesIds) -> {
      var attackingCreatureId = blockedCreaturesIds.get(0);
      currentPlayer.getBattlefield().findCardById(attackingCreatureId).getModifiers().getModifiersUntilEndOfTurn().setBlocked(true);
      CardInstance blockingCreature = nonCurrentPlayer.getBattlefield().findCardById(blockingCreatureId);
      blockingCreature.declareAsBlocker(attackingCreatureId);
      whenTriggerService.whenTriggered(gameStatus, blockingCreature, WHEN_BLOCK);
    });

    turn.setInputRequiredAction(null);
    turn.setCurrentPhaseActivePlayer(turn.getCurrentTurnPlayer());
    continueService.set(gameStatus);
  }

  private List<CardInstance> findBlockers(Map<Integer, List<Integer>> blockingCreaturesIdsForAttackingCreaturesIds, Player nonCurrentPlayer, CardInstance attacker) {
    return blockingCreaturesIdsForAttackingCreaturesIds.keySet()
        .stream()
        .map(creatureId -> nonCurrentPlayer.getBattlefield().findCardById(creatureId))
        .filter(blocker -> blockingCreaturesIdsForAttackingCreaturesIds.get(blocker.getId()).indexOf(attacker.getId()) == 0)
        .collect(Collectors.toList());
  }
}
