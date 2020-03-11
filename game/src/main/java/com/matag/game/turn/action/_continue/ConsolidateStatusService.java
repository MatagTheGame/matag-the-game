package com.matag.game.turn.action._continue;

import com.matag.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.leave.DestroyPermanentService;
import com.matag.game.turn.action.leave.ReturnPermanentToHandService;
import org.springframework.stereotype.Component;

import static com.matag.cards.properties.Type.CREATURE;

@Component
public class ConsolidateStatusService {
  private final DestroyPermanentService destroyPermanentService;
  private final ReturnPermanentToHandService returnPermanentToHandService;

  public ConsolidateStatusService(DestroyPermanentService destroyPermanentService, ReturnPermanentToHandService returnPermanentToHandService) {
    this.destroyPermanentService = destroyPermanentService;
    this.returnPermanentToHandService = returnPermanentToHandService;
  }

  // Destroy all creatures with toughness - damage > 0. If a creature is destroyed it needs to be reevaluated as that creature
  // might have "other creatures get +1/+1" and keeping them alive.
  public void consolidate(GameStatus gameStatus) {
    boolean repeat;
    do {
      repeat = false;
      for (CardInstance card : gameStatus.getAllBattlefieldCards().getCards()) {
        if (isToBeDestroyed(card)) {
          boolean destroyed = destroyPermanentService.destroy(gameStatus, card.getId());
          if (destroyed) {
            repeat = true;
          }
        }

        if (card.getModifiers().isToBeReturnedToHand()) {
          boolean returnedToHand = returnPermanentToHandService.returnPermanentToHand(gameStatus, card.getId());
          if (returnedToHand) {
            repeat = true;
          }
        }
      }
    } while (repeat);
  }

  private boolean isToBeDestroyed(CardInstance card) {
    return card.isPermanent() && card.getModifiers().isToBeDestroyed() ||
      card.isOfType(CREATURE) && card.getToughness() - card.getModifiers().getDamage() <= 0;
  }
}
