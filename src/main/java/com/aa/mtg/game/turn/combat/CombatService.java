package com.aa.mtg.game.turn.combat;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CombatService {

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public CombatService(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    public void dealCombatDamage(GameStatus gameStatus) {
        Player currentPlayer = gameStatus.getCurrentPlayer();
        Player nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

        List<CardInstance> attackingCreatures = currentPlayer.getBattlefield().getAttackingCreatures();

        if (!attackingCreatures.isEmpty()) {
            // TODO block creatures

            int totalDamage = attackingCreatures.stream()
                    .map(CardInstance::getCard)
                    .map(Card::getPower)
                    .mapToInt(Integer::intValue)
                    .sum();

            nonCurrentPlayer.decreaseLife(totalDamage);
            gameStatusUpdaterService.sendUpdatePlayerLife(gameStatus, nonCurrentPlayer);

            if (nonCurrentPlayer.getLife() <= 0) {
                gameStatus.getTurn().setWinner(currentPlayer.getName());
            }
        }
    }

}
