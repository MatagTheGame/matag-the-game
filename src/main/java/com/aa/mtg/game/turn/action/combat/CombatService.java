package com.aa.mtg.game.turn.action.combat;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.damage.DealDamageToCreatureService;
import com.aa.mtg.game.turn.action.damage.DealDamageToPlayerService;
import com.aa.mtg.game.turn.action.life.LifeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aa.mtg.cards.ability.type.AbilityType.DEATHTOUCH;
import static com.aa.mtg.cards.ability.type.AbilityType.LIFELINK;
import static com.aa.mtg.cards.ability.type.AbilityType.TRAMPLE;

@Component
public class CombatService {
    private final LifeService lifeService;
    private final DealDamageToCreatureService dealDamageToCreatureService;
    private final DealDamageToPlayerService dealDamageToPlayerService;

    @Autowired
    public CombatService(LifeService lifeService, DealDamageToCreatureService dealDamageToCreatureService, DealDamageToPlayerService dealDamageToPlayerService) {
        this.lifeService = lifeService;
        this.dealDamageToCreatureService = dealDamageToCreatureService;
        this.dealDamageToPlayerService = dealDamageToPlayerService;
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

        if (damageFromUnblockedCreatures > 0) {
            dealDamageToPlayerService.dealDamageToPlayer(gameStatus, damageFromUnblockedCreatures, nonCurrentPlayer);
        }

        if (lifelink > 0) {
            lifeService.add(currentPlayer, lifelink, gameStatus);
        }
    }

    private int dealCombatDamageForOneAttackingCreature(GameStatus gameStatus, CardInstance attackingCreature, List<CardInstance> blockingCreatures) {
        int remainingDamageForAttackingCreature = attackingCreature.getPower();
        for (CardInstance blockingCreature : blockingCreatures) {
            int damageToCurrentBlocker = Math.min(remainingDamageForAttackingCreature, blockingCreature.getToughness());

            dealDamageToCreatureService.dealDamageToCreature(gameStatus, attackingCreature, blockingCreature.getPower(), blockingCreature.hasAbility(DEATHTOUCH));
            dealDamageToCreatureService.dealDamageToCreature(gameStatus, blockingCreature, damageToCurrentBlocker, attackingCreature.hasAbility(DEATHTOUCH));

            remainingDamageForAttackingCreature -= damageToCurrentBlocker;
        }
        return remainingDamageForAttackingCreature;
    }

}
