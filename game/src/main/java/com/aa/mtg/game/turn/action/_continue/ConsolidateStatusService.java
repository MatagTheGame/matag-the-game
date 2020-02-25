package com.aa.mtg.game.turn.action._continue;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.leave.DestroyPermanentService;
import com.aa.mtg.game.turn.action.leave.ReturnPermanentToHandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.cards.properties.Type.CREATURE;

@Component
public class ConsolidateStatusService {
  private final DestroyPermanentService destroyPermanentService;
  private final ReturnPermanentToHandService returnPermanentToHandService;

  @Autowired
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

        if (card.isToBeReturnedToHand()) {
          boolean returnedToHand = returnPermanentToHandService.returnPermanentToHand(gameStatus, card.getId());
          if (returnedToHand) {
            repeat = true;
          }
        }
      }
    } while (repeat);
  }

  private boolean isToBeDestroyed(CardInstance card) {
    return card.isPermanent() && card.isToBeDestroyed() ||
      card.isOfType(CREATURE) && card.getToughness() - card.getModifiers().getDamage() <= 0;
  }
}
