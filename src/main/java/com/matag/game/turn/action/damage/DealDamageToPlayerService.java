package com.matag.game.turn.action.damage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.player.LifeService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DealDamageToPlayerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DealDamageToPlayerService.class);

  private final LifeService lifeService;

  public void dealDamageToPlayer(GameStatus gameStatus, int damage, Player player) {
    if (damage > 0) {
      lifeService.add(player, -damage, gameStatus);
      LOGGER.info("AbilityActionExecuted: deals {} damage to {}", damage, player.getName());
    }
  }
}
