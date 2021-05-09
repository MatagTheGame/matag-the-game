package com.matag.game.turn.action.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.finish.FinishGameService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class LifeService {
  private static final Logger LOGGER = LoggerFactory.getLogger(LifeService.class);

  private final FinishGameService finishGameService;

  public void add(Player player, int amount, GameStatus gameStatus) {
    if (amount != 0) {
      player.addLife(amount);
      LOGGER.info("Player {} add {} life bringing it to {}", player.getName(), amount, player.getLife());

      if (player.getLife() <= 0) {
        var winner = gameStatus.getOtherPlayer(player);
        LOGGER.info("Player {} lose, Player {} win", player.getName(), winner.getName());
        finishGameService.setWinner(gameStatus, winner);
      }
    }
  }
}
