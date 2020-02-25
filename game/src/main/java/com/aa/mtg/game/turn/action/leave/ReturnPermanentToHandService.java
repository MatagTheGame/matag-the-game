package com.aa.mtg.game.turn.action.leave;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReturnPermanentToHandService {
  private final LeaveBattlefieldService leaveBattlefieldService;
  private final PutIntoHandService putIntoHandService;

  @Autowired
  public ReturnPermanentToHandService(LeaveBattlefieldService leaveBattlefieldService, PutIntoHandService putIntoHandService) {
    this.leaveBattlefieldService = leaveBattlefieldService;
    this.putIntoHandService = putIntoHandService;
  }

  public void markAsToBeReturnedToHand(GameStatus gameStatus, int targetId) {
    CardInstance cardInstance = gameStatus.findCardByIdFromAnyBattlefield(targetId);

    if (cardInstance != null) {
      cardInstance.setToBeReturnedToHand(true);
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
