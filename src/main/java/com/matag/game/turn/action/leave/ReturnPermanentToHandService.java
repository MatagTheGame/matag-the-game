package com.matag.game.turn.action.leave;

import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReturnPermanentToHandService {
  private final LeaveBattlefieldService leaveBattlefieldService;
  private final PutIntoHandService putIntoHandService;

  public void markAsToBeReturnedToHand(GameStatus gameStatus, int targetId) {
    var cardInstance = gameStatus.findCardByIdFromAnyBattlefield(targetId);

    if (cardInstance != null) {
      cardInstance.getModifiers().getModifiersUntilEndOfTurn().setToBeReturnedToHand(true);
    }
  }

  public boolean returnPermanentToHand(GameStatus gameStatus, int targetId) {
    var cardInstance = gameStatus.findCardByIdFromAnyBattlefield(targetId);

    if (cardInstance != null) {
      leaveBattlefieldService.leaveTheBattlefield(gameStatus, targetId);
      putIntoHandService.returnToHand(gameStatus, cardInstance);
      return true;
    }

    return false;
  }
}
