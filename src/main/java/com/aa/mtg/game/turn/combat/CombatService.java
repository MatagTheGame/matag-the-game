package com.aa.mtg.game.turn.combat;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.action.DealXDamageToTargetAction;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.TurnController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aa.mtg.cards.ability.type.AbilityType.DEATHTOUCH;

@Service
public class CombatService {
    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final DealXDamageToTargetAction dealXDamageToTargetAction;

    @Autowired
    public CombatService(GameStatusUpdaterService gameStatusUpdaterService, DealXDamageToTargetAction destroyTargetAction) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.dealXDamageToTargetAction = destroyTargetAction;
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
                dealCombatDamageForOneAttackingCreature(gameStatus, attackingCreature, blockingCreaturesFor);
            }
        }

        gameStatusUpdaterService.sendUpdateBattlefields(gameStatus);
        gameStatusUpdaterService.sendUpdateGraveyards(gameStatus);

        if (damageFromUnblockedCreatures > 0) {
            nonCurrentPlayer.decreaseLife(damageFromUnblockedCreatures);
            gameStatusUpdaterService.sendUpdatePlayerLife(gameStatus, nonCurrentPlayer);

            if (nonCurrentPlayer.getLife() <= 0) {
                gameStatus.getTurn().setWinner(currentPlayer.getName());
            }
        }
    }

    private void dealCombatDamageForOneAttackingCreature(GameStatus gameStatus, CardInstance attackingCreature, List<CardInstance> blockingCreatures) {
        int remainingDamageForAttackingCreature = attackingCreature.getPower();
        for (CardInstance blockingCreature : blockingCreatures) {
            int damageToCurrentBlocker = remainingDamageForAttackingCreature;
            if (damageToCurrentBlocker > blockingCreature.getToughness()) {
                remainingDamageForAttackingCreature = blockingCreature.getToughness();
            }

            boolean attackingCreatureDestroyed = dealXDamageToTargetAction.dealDamageToCreature(gameStatus, attackingCreature, blockingCreature.getPower(), blockingCreature.hasAbility(DEATHTOUCH));
            dealXDamageToTargetAction.dealDamageToCreature(gameStatus, blockingCreature, damageToCurrentBlocker, attackingCreature.hasAbility(DEATHTOUCH));

            if (attackingCreatureDestroyed) {
                break;
            }

            remainingDamageForAttackingCreature -= damageToCurrentBlocker;
        }
    }

}
