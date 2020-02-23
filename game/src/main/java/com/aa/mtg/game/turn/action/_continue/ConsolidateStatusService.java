package com.aa.mtg.game.turn.action._continue;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.leave.DestroyPermanentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.cards.properties.Type.CREATURE;

@Component
public class ConsolidateStatusService {
  private final DestroyPermanentService destroyPermanentService;

  @Autowired
  public ConsolidateStatusService(DestroyPermanentService destroyPermanentService) {
    this.destroyPermanentService = destroyPermanentService;
  }

  // Destroy all creatures with toughness - damage > 0. If a creature is destroyed it needs to be reevaluated as that creature
  // might have "other creatures get +1/+1" and keeping them alive.
  public void consolidate(GameStatus gameStatus) {
    boolean repeat;
    do {
      repeat = false;
      for (CardInstance card : gameStatus.getAllBattlefieldCards().getCards()) {
        if (card.isOfType(CREATURE) && card.getToughness() - card.getModifiers().getDamage() <= 0) {
          boolean destroyed = destroyPermanentService.destroy(gameStatus, card.getId());
          if (destroyed) {
            repeat = true;
          }
        }
      }
    } while (repeat);
  }
}
