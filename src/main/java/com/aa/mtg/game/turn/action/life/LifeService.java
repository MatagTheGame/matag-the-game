package com.aa.mtg.game.turn.action.life;

import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LifeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LifeService.class);

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
                gameStatus.getTurn().setWinner(gameStatus.getOtherPlayer(player).getName());
            }
        }
    }
}
