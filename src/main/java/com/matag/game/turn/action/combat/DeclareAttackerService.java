package com.matag.game.turn.action.combat;

import com.matag.cards.ability.type.AbilityType;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.ContinueService;
import com.matag.game.turn.action.tap.TapPermanentService;
import com.matag.game.turn.action.trigger.WhenTriggerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matag.cards.ability.trigger.TriggerSubtype.WHEN_ATTACK;
import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;

@Component
@AllArgsConstructor
public class DeclareAttackerService {
  private final ContinueService continueService;
  private final TapPermanentService tapPermanentService;
  private final WhenTriggerService whenTriggerService;

  public void declareAttackers(GameStatus gameStatus, List<Integer> cardIds) {
    var turn = gameStatus.getTurn();
    var currentPlayer = gameStatus.getCurrentPlayer();

    if (!turn.getCurrentPhase().equals(DA)) {
      throw new RuntimeException("Attackers declared during phase: " + turn.getCurrentPhase());
    }

    cardIds.forEach(cardId -> checkIfCanAttack(currentPlayer, cardId));
    cardIds.forEach(cardId -> declareAsAttacker(gameStatus, currentPlayer, cardId));

    continueService.next(gameStatus);
  }

  private void checkIfCanAttack(Player currentPlayer, Integer cardId) {
    currentPlayer.getBattlefield().findCardById(cardId).checkIfCanAttack();
  }

  private void declareAsAttacker(GameStatus gameStatus, Player currentPlayer, Integer cardId) {
    var cardInstance = currentPlayer.getBattlefield().findCardById(cardId);
    if (!cardInstance.hasAbilityType(AbilityType.VIGILANCE)) {
      tapPermanentService.tap(gameStatus, cardId);
    }
    cardInstance.declareAsAttacker();
    whenTriggerService.whenTriggered(gameStatus, cardInstance, WHEN_ATTACK);
  }
}
