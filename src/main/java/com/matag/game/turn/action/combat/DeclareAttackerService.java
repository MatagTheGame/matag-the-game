package com.matag.game.turn.action.combat;

import static com.matag.cards.ability.trigger.TriggerSubtype.WHEN_ATTACK;
import static com.matag.game.turn.action._continue.InputRequiredActions.DECLARE_ATTACKERS;
import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;

import java.util.List;

import org.springframework.stereotype.Component;

import com.matag.cards.ability.type.AbilityType;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.ContinueService;
import com.matag.game.turn.action.tap.TapPermanentService;
import com.matag.game.turn.action.trigger.WhenTriggerService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DeclareAttackerService {
  private final ContinueService continueService;
  private final TapPermanentService tapPermanentService;
  private final WhenTriggerService whenTriggerService;

  public void declareAttackers(GameStatus gameStatus, List<Integer> cardIds) {
    var turn = gameStatus.getTurn();
    var currentPlayer = gameStatus.getCurrentPlayer();

    if (!turn.getCurrentPhase().equals(DA) || !DECLARE_ATTACKERS.equals(turn.getInputRequiredAction())) {
      throw new RuntimeException("Cannot declare attackers. Phase=" + turn.getCurrentPhase());
    }

    cardIds.forEach(cardId -> checkIfCanAttack(currentPlayer, cardId));
    cardIds.forEach(cardId -> declareAsAttacker(gameStatus, currentPlayer, cardId));

    turn.setInputRequiredAction(null);
    continueService.set(gameStatus);
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
