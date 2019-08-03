package com.aa.mtg.game.player;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LifeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LifeService.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public LifeService(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    public void add(Player player, int amount, GameStatus gameStatus) {
        perform(player, "ADD", amount, gameStatus);
    }

    public void subtract(Player player, int amount, GameStatus gameStatus) {
        perform(player, "SUBTRACT", amount, gameStatus);
    }

    private void perform(Player player, String lifeServiceType, int amount, GameStatus gameStatus) {
        if (amount > 0) {
            if (lifeServiceType.equals("ADD")) {
                player.increaseLife(amount);
            } else {
                player.decreaseLife(amount);
            }

            LOGGER.info("Player {} {} {} life bringing it to {}", player.getName(), lifeServiceType, amount, player.getLife());
            gameStatusUpdaterService.sendUpdatePlayerLife(gameStatus, player);

            if (player.getLife() <= 0) {
                gameStatus.getTurn().setWinner(player.getName());
            }
        }
    }
}
