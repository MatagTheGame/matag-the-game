package com.aa.mtg.game.turn.combat;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.action.DealXDamageToTargetAction;
import com.aa.mtg.game.player.LifeService;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aa.mtg.cards.ability.type.AbilityType.DEATHTOUCH;
import static com.aa.mtg.cards.ability.type.AbilityType.LIFELINK;
import static com.aa.mtg.cards.ability.type.AbilityType.TRAMPLE;

@Service
public class CombatService {
    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final DealXDamageToTargetAction dealXDamageToTargetAction;
    private final LifeService lifeService;

    @Autowired
    public CombatService(GameStatusUpdaterService gameStatusUpdaterService, DealXDamageToTargetAction destroyTargetAction, LifeService lifeService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.dealXDamageToTargetAction = destroyTargetAction;
        this.lifeService = lifeService;
    }

    public void dealCombatDamage(GameStatus gameStatus) {
        Player currentPlayer = gameStatus.getCurrentPlayer();
        Player nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

        List<CardInstance> attackingCreatures = currentPlayer.getBattlefield().getAttackingCreatures();

        int damageFromUnblockedCreatures = 0;
        int lifelink = 0;
        for (CardInstance attackingCreature : attackingCreatures) {
            List<CardInstance> blockingCreaturesFor = nonCurrentPlayer.getBattlefield().getBlockingCreaturesFor(attackingCreature.getId());
            int remainingDamage = dealCombatDamageForOneAttackingCreature(gameStatus, attackingCreature, blockingCreaturesFor);

            if (blockingCreaturesFor.isEmpty() || attackingCreature.hasAbility(TRAMPLE)) {
                damageFromUnblockedCreatures += remainingDamage;
            }

            if (attackingCreature.hasAbility(LIFELINK)) {
                lifelink += attackingCreature.getPower();
            }
        }

        gameStatusUpdaterService.sendUpdateBattlefields(gameStatus);
        gameStatusUpdaterService.sendUpdateGraveyards(gameStatus);

        if (damageFromUnblockedCreatures > 0) {
            lifeService.substract(nonCurrentPlayer, damageFromUnblockedCreatures, gameStatus);
        }

        if (lifelink > 0) {
            lifeService.add(currentPlayer, lifelink, gameStatus);
        }
    }

    private int dealCombatDamageForOneAttackingCreature(GameStatus gameStatus, CardInstance attackingCreature, List<CardInstance> blockingCreatures) {
        int remainingDamageForAttackingCreature = attackingCreature.getPower();
        for (CardInstance blockingCreature : blockingCreatures) {
            int damageToCurrentBlocker = Math.min(remainingDamageForAttackingCreature, blockingCreature.getToughness());

            dealXDamageToTargetAction.dealDamageToCreature(gameStatus, attackingCreature, blockingCreature.getPower(), blockingCreature.hasAbility(DEATHTOUCH));
            dealXDamageToTargetAction.dealDamageToCreature(gameStatus, blockingCreature, damageToCurrentBlocker, attackingCreature.hasAbility(DEATHTOUCH));

            remainingDamageForAttackingCreature -= damageToCurrentBlocker;
        }
        return remainingDamageForAttackingCreature;
    }

}
