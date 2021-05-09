package com.matag.game.turn.action.target;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.AbilityAction;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.permanent.PermanentGetService;
import com.matag.game.turn.action.player.PlayerGetService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ThatTargetsGetAction implements AbilityAction {
  private static final Logger LOGGER = LoggerFactory.getLogger(ThatTargetsGetAction.class);

  private final TargetCheckerService targetCheckerService;
  private final PermanentGetService permanentGetService;
  private final PlayerGetService playerGetService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    for (var i = 0; i < ability.getTargets().size(); i++) {
      var target = ability.getTargets().get(i);
      var targetId = targetCheckerService.getTargetIdAtIndex(cardInstance, ability, i);

      if (targetId != null) {
        switch (target.getMagicInstanceSelector().getSelectorType()) {
          case PERMANENT -> thatPermanentGets(gameStatus, cardInstance, ability, (int)targetId);
          case PLAYER -> thatPlayerGets(gameStatus, cardInstance, ability, (String)targetId);
          case SPELL -> thatSpellGets(gameStatus, cardInstance, ability, (int)targetId);
          case ANY -> thatAnyTargetGets(cardInstance, gameStatus, ability, targetId);
        }
      }
    }
  }

  private void thatPlayerGets(GameStatus gameStatus, CardInstance cardInstance, CardInstanceAbility ability, String targetPlayer) {
    var player = gameStatus.getPlayerByName(targetPlayer);
    playerGetService.thatPlayerGets(cardInstance, gameStatus, ability.getParameters(), player);
  }

  private void thatPermanentGets(GameStatus gameStatus, CardInstance cardInstance, CardInstanceAbility ability, int targetId) {
    var targetOptional = gameStatus.getAllBattlefieldCardsSearch().withId(targetId);
    thatTargetGets(gameStatus, cardInstance, ability, targetId, targetOptional);
  }

  private void thatSpellGets(GameStatus gameStatus, CardInstance cardInstance, CardInstanceAbility ability, int targetId) {
    var targetOptional = gameStatus.getStack().search().withId(targetId);
    thatTargetGets(gameStatus, cardInstance, ability, targetId, targetOptional);
  }

  private void thatAnyTargetGets(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability, Object targetId) {
    if (targetId instanceof String) {
      thatPlayerGets(gameStatus, cardInstance, ability, (String) targetId);
    } else if (targetId instanceof Integer) {
      thatPermanentGets(gameStatus, cardInstance, ability, (int) targetId);
    }
  }

  private void thatTargetGets(GameStatus gameStatus, CardInstance cardInstance, CardInstanceAbility ability, int targetCardId, java.util.Optional<CardInstance> targetOptional) {
    if (targetOptional.isPresent()) {
      var target = targetOptional.get();
      permanentGetService.thatPermanentGets(cardInstance, gameStatus, ability.getParameters(), target);

    } else {
      LOGGER.info("target {} is not anymore valid.", targetCardId);
    }
  }
}
