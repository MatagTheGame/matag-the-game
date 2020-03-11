package com.matag.game.turn.action.leave;

import com.matag.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.matag.cards.ability.type.AbilityType.INDESTRUCTIBLE;

@Component
public class DestroyPermanentService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DestroyPermanentService.class);

  private final LeaveBattlefieldService leaveBattlefieldService;
  private final PutIntoGraveyardService putIntoGraveyardService;
  private final WhenDieService whenDieService;

  public DestroyPermanentService(LeaveBattlefieldService leaveBattlefieldService, PutIntoGraveyardService putIntoGraveyardService, WhenDieService whenDieService) {
    this.leaveBattlefieldService = leaveBattlefieldService;
    this.putIntoGraveyardService = putIntoGraveyardService;
    this.whenDieService = whenDieService;
  }

  public void markToBeDestroyed(GameStatus gameStatus, int permanentId) {
    CardInstance cardInstance = gameStatus.findCardByIdFromAnyBattlefield(permanentId);

    if (cardInstance != null) {
      cardInstance.getModifiers().setToBeDestroyed(true);
    }
  }

  public boolean destroy(GameStatus gameStatus, int permanentId) {
    CardInstance cardInstance = gameStatus.findCardByIdFromAnyBattlefield(permanentId);

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
    whenDieService.whenDie(gameStatus, cardInstance);
    leaveBattlefieldService.leaveTheBattlefield(gameStatus, permanentId);
    putIntoGraveyardService.putIntoGraveyard(gameStatus, cardInstance);
    LOGGER.info("{} has been destroyed.", cardInstance.getIdAndName());
    return true;
  }
}
