package com.matag.game.turn.action.combat;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.damage.DealDamageToCreatureService;
import com.matag.game.turn.action.damage.DealDamageToPlayerService;
import com.matag.game.turn.action.life.LifeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matag.cards.ability.type.AbilityType.*;
import static com.matag.game.turn.phases.FirstStrikePhase.FS;

@Component
@AllArgsConstructor
public class CombatService {
  private final LifeService lifeService;
  private final DealDamageToCreatureService dealDamageToCreatureService;
  private final DealDamageToPlayerService dealDamageToPlayerService;

  public void dealCombatDamage(GameStatus gameStatus) {
    Player currentPlayer = gameStatus.getCurrentPlayer();
    Player nonCurrentPlayer = gameStatus.getNonCurrentPlayer();

    List<CardInstance> attackingCreatures = currentPlayer.getBattlefield().getAttackingCreatures();

    int damageFromUnblockedCreatures = 0;
    int lifelink = 0;
    for (CardInstance attackingCreature : attackingCreatures) {
      List<CardInstance> blockingCreaturesFor = nonCurrentPlayer.getBattlefield().getBlockingCreaturesFor(attackingCreature.getId());
      int remainingDamage = dealCombatDamageForOneAttackingCreature(gameStatus, attackingCreature, blockingCreaturesFor);

      boolean isBlocked = attackingCreature.getModifiers().getModifiersUntilEndOfTurn().isBlocked();
      if (!isBlocked || attackingCreature.hasAbilityType(TRAMPLE)) {
        if (shouldDealDamage(gameStatus, attackingCreature)) {
          damageFromUnblockedCreatures += remainingDamage;
        }
      }

      if (attackingCreature.hasAbilityType(LIFELINK)) {
        if (!isBlocked || !blockingCreaturesFor.isEmpty()) {
          lifelink += attackingCreature.getPower();
        }
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

      if (shouldDealDamage(gameStatus, blockingCreature)) {
        dealDamageToCreatureService.dealDamageToCreature(gameStatus, attackingCreature, blockingCreature.getPower(), blockingCreature.hasAbilityType(DEATHTOUCH), blockingCreature);
        remainingDamageForAttackingCreature -= damageToCurrentBlocker;
      } else {
        remainingDamageForAttackingCreature = 0;
      }

      if (shouldDealDamage(gameStatus, attackingCreature)) {
        dealDamageToCreatureService.dealDamageToCreature(gameStatus, blockingCreature, damageToCurrentBlocker, attackingCreature.hasAbilityType(DEATHTOUCH), attackingCreature);
      }
    }
    return remainingDamageForAttackingCreature;
  }

  private boolean shouldDealDamage(GameStatus gameStatus, CardInstance cardInstance) {
    if (gameStatus.getTurn().getCurrentPhase().equals(FS)) {
      return cardInstance.hasAnyFixedAbility(List.of(FIRST_STRIKE, DOUBLE_STRIKE));

    } else {
      return !cardInstance.hasFixedAbility(FIRST_STRIKE);
    }
  }
}
