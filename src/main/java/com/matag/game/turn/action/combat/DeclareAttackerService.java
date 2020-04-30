package com.matag.game.turn.action.combat;

import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.Turn;
import com.matag.game.turn.action._continue.ContinueTurnService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;

@Component
@AllArgsConstructor
public class DeclareAttackerService {
  private final ContinueTurnService continueTurnService;

  public void declareAttackers(GameStatus gameStatus, List<Integer> cardIds) {
    Turn turn = gameStatus.getTurn();
    Player currentPlayer = gameStatus.getCurrentPlayer();

    if (!turn.getCurrentPhase().equals(DA)) {
      throw new RuntimeException("Attackers declared during phase: " + turn.getCurrentPhase());
    }

    cardIds.forEach(cardId -> currentPlayer.getBattlefield().findCardById(cardId).checkIfCanAttack());
    cardIds.forEach(cardId -> currentPlayer.getBattlefield().findCardById(cardId).declareAsAttacker());

    continueTurnService.continueTurn(gameStatus);
  }
}
