package com.matag.game.turn.action._continue;

import static com.matag.cards.properties.Type.CREATURE;

import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.leave.DestroyPermanentService;
import com.matag.game.turn.action.leave.ReturnPermanentToHandService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ConsolidateStatusService {
  private final DestroyPermanentService destroyPermanentService;
  private final ReturnPermanentToHandService returnPermanentToHandService;

  // Destroy all creatures with toughness - damage > 0.
  // If a creature is destroyed the entire GameStatus needs to be reevaluated as that creature
  // might have "other creatures get +1/+1" and keeping them alive.
  public void consolidate(GameStatus gameStatus) {
    boolean repeat;
    do {
      repeat = false;
      for (var card : gameStatus.getAllBattlefieldCards()) {
        if (isToBeDestroyed(card)) {
          var destroyed = destroyPermanentService.destroy(gameStatus, card.getId());
          if (destroyed) {
            repeat = true;
          }
        }

        if (card.getModifiers().getModifiersUntilEndOfTurn().isToBeReturnedToHand()) {
          var returnedToHand = returnPermanentToHandService.returnPermanentToHand(gameStatus, card.getId());
          if (returnedToHand) {
            repeat = true;
          }
        }
      }
    } while (repeat);
  }

  private boolean isToBeDestroyed(CardInstance card) {
    return card.isPermanent() && card.getModifiers().getModifiersUntilEndOfTurn().isToBeDestroyed() ||
      card.isOfType(CREATURE) && card.getToughness() - card.getModifiers().getDamage() <= 0;
  }
}
