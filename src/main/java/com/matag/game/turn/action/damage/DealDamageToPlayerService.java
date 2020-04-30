package com.matag.game.turn.action.damage;

import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.life.LifeService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DealDamageToPlayerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DealDamageToPlayerService.class);

  private final LifeService lifeService;

  public void dealDamageToPlayer(GameStatus gameStatus, int damage, Player player) {
    if (damage > 0) {
      lifeService.subtract(player, damage, gameStatus);
      LOGGER.info("AbilityActionExecuted: deals {} damage to {}", damage, player.getName());
    }
  }
}
