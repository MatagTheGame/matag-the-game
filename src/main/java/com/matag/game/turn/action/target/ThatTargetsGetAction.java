package com.matag.game.turn.action.target;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.AbilityAction;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.cards.ability.AbilityService;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.damage.DealDamageToPlayerService;
import com.matag.game.turn.action.draw.DrawXCardsService;
import com.matag.game.turn.action.life.LifeService;
import com.matag.game.turn.action.permanent.PermanentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ThatTargetsGetAction implements AbilityAction {
  private static final Logger LOGGER = LoggerFactory.getLogger(ThatTargetsGetAction.class);

  private final LifeService lifeService;
  private final DrawXCardsService drawXCardsService;
  private final PermanentService permanentService;
  private final DealDamageToPlayerService dealDamageToPlayerService;
  private final AbilityService abilityService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    for (Object targetId : cardInstance.getModifiers().getTargets()) {
      if (targetId instanceof String) {
        String targetPlayerName = (String) targetId;
        thatTargetPlayerGet(cardInstance, gameStatus, ability.getParameters(), targetPlayerName);

      } else {
        int targetCardId = (int) targetId;

        Optional<CardInstance> targetOptional = gameStatus.getAllBattlefieldCards().withId(targetCardId);

        if (targetOptional.isPresent()) {
          CardInstance target = targetOptional.get();
          permanentService.thatPermanentGets(cardInstance, gameStatus, ability.getParameters(), target);

        } else {
          LOGGER.info("target {} is not anymore valid.", targetCardId);
        }
      }
    }
  }

  private void thatTargetPlayerGet(CardInstance cardInstance, GameStatus gameStatus, List<String> parameters, String targetPlayerName) {
    for (String parameter : parameters) {
      thatTargetPlayerGet(cardInstance, gameStatus, parameter, targetPlayerName);
    }
  }

  private void thatTargetPlayerGet(CardInstance cardInstance, GameStatus gameStatus, String parameter, String targetPlayerName) {
    Player player = gameStatus.getPlayerByName(targetPlayerName);
    int damage = abilityService.damageFromParameter(parameter);
    dealDamageToPlayerService.dealDamageToPlayer(gameStatus, damage, player);

    int life = abilityService.lifeFromParameter(parameter);
    if (life != 0) {
      lifeService.add(player, damage, gameStatus);
      LOGGER.info("AbilityActionExecuted: {} add {} life to {}", cardInstance.getIdAndName(), damage, player.getName());
    }

    int cardsToDraw = abilityService.drawFromParameter(parameter);
    if (cardsToDraw > 0) {
      drawXCardsService.drawXCards(player, cardsToDraw);
    }
  }
}
