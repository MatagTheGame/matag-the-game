package com.matag.game.turn.action.target;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.AbilityAction;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.permanent.PermanentGetService;
import com.matag.game.turn.action.player.PlayerGetService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ThatTargetsGetAction implements AbilityAction {
  private static final Logger LOGGER = LoggerFactory.getLogger(ThatTargetsGetAction.class);

  private final TargetCheckerService targetCheckerService;
  private final PermanentGetService permanentGetService;
  private final PlayerGetService playerGetService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    for (var i = 0; i < ability.getTargets().size(); i++) {
      var targetId = targetCheckerService.getTargetIdAtIndex(cardInstance, ability, i);

      if (targetId != null) {
        if (targetId instanceof String targetPlayer) {
          var player = gameStatus.getPlayerByName(targetPlayer);
          playerGetService.thatPlayerGets(cardInstance, gameStatus, ability.getParameters(), player);

        } else if (targetId instanceof Integer targetCardId) {
          var targetOptional = gameStatus.getAllBattlefieldCardsSearch().withId(targetCardId);

          if (targetOptional.isPresent()) {
            var target = targetOptional.get();
            permanentGetService.thatPermanentGets(cardInstance, gameStatus, ability.getParameters(), target);

          } else {
            LOGGER.info("target {} is not anymore valid.", targetCardId);
          }
        }
      }
    }
  }
}
