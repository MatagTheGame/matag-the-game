package com.matag.game.turn.action.life;

import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.finish.FinishGameService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LifeService {
  private static final Logger LOGGER = LoggerFactory.getLogger(LifeService.class);

  private final FinishGameService finishGameService;

  public void add(Player player, int amount, GameStatus gameStatus) {
    perform(player, "ADD", amount, gameStatus);
  }

  public void subtract(Player player, int amount, GameStatus gameStatus) {
    perform(player, "SUBTRACT", amount, gameStatus);
  }

  private void perform(Player player, String lifeServiceType, int amount, GameStatus gameStatus) {
    if (amount != 0) {
      if (lifeServiceType.equals("ADD")) {
        player.increaseLife(amount);
      } else {
        player.decreaseLife(amount);
      }

      LOGGER.info("Player {} {} {} life bringing it to {}", player.getName(), lifeServiceType, amount, player.getLife());

      if (player.getLife() <= 0) {
        finishGameService.setWinner(gameStatus, gameStatus.getOtherPlayer(player));
      }
    }
  }
}
