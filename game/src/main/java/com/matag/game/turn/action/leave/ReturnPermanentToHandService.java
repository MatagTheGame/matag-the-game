package com.matag.game.turn.action.leave;

import com.matag.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class ReturnPermanentToHandService {
  private final LeaveBattlefieldService leaveBattlefieldService;
  private final PutIntoHandService putIntoHandService;

  public ReturnPermanentToHandService(LeaveBattlefieldService leaveBattlefieldService, PutIntoHandService putIntoHandService) {
    this.leaveBattlefieldService = leaveBattlefieldService;
    this.putIntoHandService = putIntoHandService;
  }

  public void markAsToBeReturnedToHand(GameStatus gameStatus, int targetId) {
    CardInstance cardInstance = gameStatus.findCardByIdFromAnyBattlefield(targetId);

    if (cardInstance != null) {
      cardInstance.getModifiers().setToBeReturnedToHand(true);
    }
  }

  public boolean returnPermanentToHand(GameStatus gameStatus, int targetId) {
    CardInstance cardInstance = gameStatus.findCardByIdFromAnyBattlefield(targetId);

    if (cardInstance != null) {
      leaveBattlefieldService.leaveTheBattlefield(gameStatus, targetId);
      putIntoHandService.returnToHand(gameStatus, cardInstance);
      return true;
    }

    return false;
  }
}
