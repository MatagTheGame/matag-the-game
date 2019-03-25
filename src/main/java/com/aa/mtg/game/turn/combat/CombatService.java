package com.aa.mtg.game.turn.combat;

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

        int damageFromUnblockedCreatures = 0;
        for (CardInstance attackingCreature : attackingCreatures) {
            List<CardInstance> blockingCreaturesFor = nonCurrentPlayer.getBattlefield().getBlockingCreaturesFor(attackingCreature.getId());
            if (blockingCreaturesFor.isEmpty()) {
                damageFromUnblockedCreatures += attackingCreature.getPower();
            } else {
                dealDamageToCreature(currentPlayer, attackingCreature, blockingCreaturesFor.get(0).getPower());
                dealDamageToCreature(nonCurrentPlayer, blockingCreaturesFor.get(0), attackingCreature.getPower());
                gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
                gameStatusUpdaterService.sendUpdateNonCurrentPlayerBattlefield(gameStatus);
                gameStatusUpdaterService.sendUpdateCurrentPlayerGraveyard(gameStatus);
                gameStatusUpdaterService.sendUpdateNonCurrentPlayerGraveyard(gameStatus);
            }
        }

        if (damageFromUnblockedCreatures > 0) {
            nonCurrentPlayer.decreaseLife(damageFromUnblockedCreatures);
            gameStatusUpdaterService.sendUpdatePlayerLife(gameStatus, nonCurrentPlayer);

            if (nonCurrentPlayer.getLife() <= 0) {
                gameStatus.getTurn().setWinner(currentPlayer.getName());
            }
        }
    }

    private void dealDamageToCreature(Player owner, CardInstance cardInstance, int damage) {
        // TODO owner
        cardInstance.getModifiers().setDamage(damage);
        if (cardInstance.getModifiers().getDamage() >= cardInstance.getToughness()) {
            cardInstance = owner.getBattlefield().extractCardById(cardInstance.getId());
            cardInstance.clearModifiers();
            owner.getGraveyard().addCard(cardInstance);
        }
    }

}
