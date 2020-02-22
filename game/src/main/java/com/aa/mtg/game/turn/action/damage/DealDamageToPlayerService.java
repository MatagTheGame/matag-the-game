package com.aa.mtg.game.turn.action.damage;

import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.life.LifeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DealDamageToPlayerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DealDamageToPlayerService.class);

  private final LifeService lifeService;

  @Autowired
  public DealDamageToPlayerService(LifeService lifeService) {
    this.lifeService = lifeService;
  }

  public void dealDamageToPlayer(GameStatus gameStatus, int damage, Player player) {
    if (damage > 0) {
      lifeService.subtract(player, damage, gameStatus);
      LOGGER.info("AbilityActionExecuted: deals {} damage to {}", damage, player.getName());
    }
  }
}
