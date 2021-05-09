package com.matag.game.turn.action.damage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;

@Component
public class DealDamageToCreatureService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DealDamageToCreatureService.class);

  public void dealDamageToCreature(GameStatus gameStatus, CardInstance cardInstance, int damage, boolean deathtouch, CardInstance damageDealer) {
    if (damage > 0) {
      LOGGER.info("{} is getting {} damage from {}.", cardInstance.getIdAndName(), damage, damageDealer.getIdAndName());
      cardInstance.getModifiers().dealDamage(damage);
      if (cardInstance.getToughness() - cardInstance.getModifiers().getDamage() <= 0 || deathtouch) {
        LOGGER.info("{} marked to be destroyed.", cardInstance.getIdAndName());
        cardInstance.getModifiers().getModifiersUntilEndOfTurn().setToBeDestroyed(true);
      }
    }
  }
}
