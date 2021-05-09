package com.matag.game.turn.action.leave;

import static com.matag.cards.ability.trigger.TriggerSubtype.WHEN_DIE;
import static com.matag.cards.ability.type.AbilityType.INDESTRUCTIBLE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.trigger.WhenTriggerService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DestroyPermanentService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DestroyPermanentService.class);

  private final LeaveBattlefieldService leaveBattlefieldService;
  private final PutIntoGraveyardService putIntoGraveyardService;
  private final WhenTriggerService whenTriggerService;

  public void markToBeDestroyed(GameStatus gameStatus, int permanentId) {
    var cardInstance = gameStatus.findCardByIdFromAnyBattlefield(permanentId);

    if (cardInstance != null) {
      cardInstance.getModifiers().getModifiersUntilEndOfTurn().setToBeDestroyed(true);
    }
  }

  public boolean destroy(GameStatus gameStatus, int permanentId) {
    var cardInstance = gameStatus.findCardByIdFromAnyBattlefield(permanentId);

    if (cardInstance == null) {
      return false;
    }

    if (cardInstance.getToughness() <= 0) {
      return destroyAction(gameStatus, permanentId, cardInstance);
    }

    if (cardInstance.hasAbilityType(INDESTRUCTIBLE)) {
      LOGGER.info("{} has indestructible and cannot be destroyed.", cardInstance.getIdAndName());
      return false;
    }

    return destroyAction(gameStatus, permanentId, cardInstance);
  }

  private boolean destroyAction(GameStatus gameStatus, int permanentId, CardInstance cardInstance) {
    whenTriggerService.whenTriggered(gameStatus, cardInstance, WHEN_DIE);
    leaveBattlefieldService.leaveTheBattlefield(gameStatus, permanentId);
    putIntoGraveyardService.putIntoGraveyard(gameStatus, cardInstance);
    LOGGER.info("{} has been destroyed.", cardInstance.getIdAndName());
    return true;
  }
}
