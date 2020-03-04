package com.aa.mtg.game.turn.action.damage;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DealDamageToCreatureService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DealDamageToCreatureService.class);

  public void dealDamageToCreature(GameStatus gameStatus, CardInstance cardInstance, int damage, boolean deathtouch) {
    if (damage > 0) {
      LOGGER.info("{} is getting {} damage.", cardInstance.getIdAndName(), damage);
      cardInstance.getModifiers().dealDamage(damage);
      if (cardInstance.getToughness() - cardInstance.getModifiers().getDamage() <= 0 || deathtouch) {
        LOGGER.info("{} marked to be destroyed.", cardInstance.getIdAndName());
        cardInstance.getModifiers().setToBeDestroyed(true);
      }
    }
  }
}
