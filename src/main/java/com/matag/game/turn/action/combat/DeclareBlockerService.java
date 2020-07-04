package com.matag.game.turn.action.combat;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.Turn;
import com.matag.game.turn.action._continue.ContinueTurnService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.matag.game.turn.phases.combat.DeclareBlockersPhase.DB;

@Component
@AllArgsConstructor
public class DeclareBlockerService {
  private final ContinueTurnService continueTurnService;
  private final BlockerChecker blockerChecker;

  public void declareBlockers(GameStatus gameStatus, Map<Integer, List<Integer>> blockingCreaturesIdsForAttackingCreaturesIds) {
    Turn turn = gameStatus.getTurn();
    Player currentPlayer = gameStatus.getCurrentPlayer();
    Player nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

    if (!turn.getCurrentPhase().equals(DB)) {
      throw new RuntimeException("Blockers declared during phase: " + turn.getCurrentPhase());
    }

    blockingCreaturesIdsForAttackingCreaturesIds.forEach((blockingCreatureId, blockedCreaturesIds) -> {
      CardInstance attacker = currentPlayer.getBattlefield().findCardById(blockedCreaturesIds.get(0));
      List<CardInstance> blockers = findBlockers(blockingCreaturesIdsForAttackingCreaturesIds, nonCurrentPlayer, attacker);
      blockerChecker.checkIfCanBlock(attacker, blockers);
    });

    blockingCreaturesIdsForAttackingCreaturesIds.forEach((blockingCreatureId, blockedCreaturesIds) -> {
      Integer attackingCreatureId = blockedCreaturesIds.get(0);
      currentPlayer.getBattlefield().findCardById(attackingCreatureId).getModifiers().getModifiersUntilEndOfTurn().setBlocked(true);
      nonCurrentPlayer.getBattlefield().findCardById(blockingCreatureId).declareAsBlocker(attackingCreatureId);
    });

    continueTurnService.continueTurn(gameStatus);
  }

  private List<CardInstance> findBlockers(Map<Integer, List<Integer>> blockingCreaturesIdsForAttackingCreaturesIds, Player nonCurrentPlayer, CardInstance attacker) {
    return blockingCreaturesIdsForAttackingCreaturesIds.keySet()
        .stream()
        .map(creatureId -> nonCurrentPlayer.getBattlefield().findCardById(creatureId))
        .filter(blocker -> blockingCreaturesIdsForAttackingCreaturesIds.get(blocker.getId()).indexOf(attacker.getId()) == 0)
        .collect(Collectors.toList());
  }
}
