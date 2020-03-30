package com.matag.game.turn.action.combat;

import com.matag.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.Turn;
import com.matag.game.turn.action._continue.ContinueTurnService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.matag.game.turn.phases.DeclareBlockersPhase.DB;

@Component
@AllArgsConstructor
public class DeclareBlockerService {
  private final ContinueTurnService continueTurnService;

  public void declareBlockers(GameStatus gameStatus, Map<Integer, List<Integer>> blockingCreaturesIdsForAttackingCreaturesIds) {
    Turn turn = gameStatus.getTurn();
    Player currentPlayer = gameStatus.getCurrentPlayer();
    Player nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

    if (!turn.getCurrentPhase().equals(DB)) {
      throw new RuntimeException("Blockers declared during phase: " + turn.getCurrentPhase());
    }

    blockingCreaturesIdsForAttackingCreaturesIds.forEach((blockingCreatureId, blockedCreaturesIds) -> {
      CardInstance blockedCreature = currentPlayer.getBattlefield().findCardById(blockedCreaturesIds.get(0));
      nonCurrentPlayer.getBattlefield().findCardById(blockingCreatureId).checkIfCanBlock(blockedCreature);
    });

    blockingCreaturesIdsForAttackingCreaturesIds.forEach((blockingCreatureId, blockedCreaturesIds) ->
      nonCurrentPlayer.getBattlefield().findCardById(blockingCreatureId).declareAsBlocker(blockedCreaturesIds.get(0))
    );

    continueTurnService.continueTurn(gameStatus);
  }
}
