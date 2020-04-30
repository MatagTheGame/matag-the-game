package com.matag.game.turn.action.tap;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TapPermanentService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TapPermanentService.class);

  public void tap(GameStatus gameStatus, int targetId) {
    CardInstance cardToTap = gameStatus.findCardByIdFromAnyBattlefield(targetId);
    if (cardToTap != null) {
      cardToTap.getModifiers().tap();
      LOGGER.info("{} tapped.", cardToTap.getIdAndName());

    } else {
      LOGGER.info("target {} is not anymore valid.", targetId);
    }
  }

  public void tapDoesNotUntapNextTurn(GameStatus gameStatus, int targetId) {
    tap(gameStatus, targetId);
    CardInstance cardToTap = gameStatus.findCardByIdFromAnyBattlefield(targetId);
    if (cardToTap != null) {
      cardToTap.getModifiers().doesNotUntapNextTurn();
      LOGGER.info("{} does not untap next turn.", cardToTap.getIdAndName());

    } else {
      LOGGER.info("target {} is not anymore valid.", targetId);
    }
  }

  public void untap(GameStatus gameStatus, int targetId) {
    CardInstance cardToTap = gameStatus.findCardByIdFromAnyBattlefield(targetId);
    if (cardToTap != null) {
      cardToTap.getModifiers().untap();
      LOGGER.info("{} tapped.", cardToTap.getIdAndName());

    } else {
      LOGGER.info("target {} is not anymore valid.", targetId);
    }
  }
}
