package com.aa.mtg.game.turn.combat;

import com.aa.mtg.cards.CardInstance;
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
    private Logger LOGGER = LoggerFactory.getLogger(TurnController.class);

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
                dealCombatDamageForOneAttackingCreature(gameStatus, attackingCreature, blockingCreaturesFor);
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

    private void dealCombatDamageForOneAttackingCreature(GameStatus gameStatus, CardInstance attackingCreature, List<CardInstance> blockingCreatures) {
        int remainingDamageForAttackingCreature = attackingCreature.getPower();
        for (CardInstance blockingCreature : blockingCreatures) {
            int damageToCurrentBlocker = remainingDamageForAttackingCreature;
            if (damageToCurrentBlocker > blockingCreature.getToughness()) {
                remainingDamageForAttackingCreature = blockingCreature.getToughness();
            }

            boolean attackingCreatureDestroyed = dealDamageToCreature(gameStatus, attackingCreature, blockingCreature.getPower(), blockingCreature.hasAbility(DEATHTOUCH));
            dealDamageToCreature(gameStatus, blockingCreature, damageToCurrentBlocker, attackingCreature.hasAbility(DEATHTOUCH));

            if (attackingCreatureDestroyed) {
                break;
            }

            remainingDamageForAttackingCreature -= damageToCurrentBlocker;
        }
    }

    private boolean dealDamageToCreature(GameStatus gameStatus, CardInstance cardInstance, int damage, boolean deathtouch) {
        LOGGER.info("{} is getting {} damage", cardInstance.getIdAndName(), damage);
        cardInstance.getModifiers().setDamage(damage);
        if (cardInstance.getModifiers().getDamage() >= cardInstance.getToughness()) {
            destroy(gameStatus, cardInstance);
            return true;
        } else if (damage > 0 && deathtouch) {
            destroy(gameStatus, cardInstance);
            return true;
        }
        return false;
    }

    private void destroy(GameStatus gameStatus, CardInstance cardInstance) {
        LOGGER.info("{} is being destroyed", cardInstance.getIdAndName());
        Player owner = gameStatus.getPlayerByName(cardInstance.getOwner());
        cardInstance = owner.getBattlefield().extractCardById(cardInstance.getId());
        cardInstance.clearModifiers();
        owner.getGraveyard().addCard(cardInstance);
    }

}
