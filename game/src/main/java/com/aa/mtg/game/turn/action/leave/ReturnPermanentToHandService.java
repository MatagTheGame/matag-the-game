package com.aa.mtg.game.turn.action.leave;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReturnPermanentToHandService {
  private final LeaveBattlefieldService leaveBattlefieldService;
  private final PutIntoHandService returnToHand;

  @Autowired
  public ReturnPermanentToHandService(LeaveBattlefieldService leaveBattlefieldService, PutIntoHandService returnToHand) {
    this.leaveBattlefieldService = leaveBattlefieldService;
    this.returnToHand = returnToHand;
  }

  public void returnPermanentToHand(GameStatus gameStatus, int targetId) {
    CardInstance cardInstance = gameStatus.findCardByIdFromAnyBattlefield(targetId);

    if (cardInstance != null) {
      leaveBattlefieldService.leaveTheBattlefield(gameStatus, targetId);
      returnToHand.returnToHand(gameStatus, cardInstance);
    }
  }
}
