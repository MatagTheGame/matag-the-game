package com.matag.game.turn.action.attach;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AttachService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AttachService.class);

  public void attach(GameStatus gameStatus, CardInstance cardInstance, int attachedToId) {
    CardInstance attachTo = gameStatus.findCardByIdFromAnyBattlefield(attachedToId);
    cardInstance.getModifiers().setAttachedToId(attachedToId);
    LOGGER.info("attached {} to {}", cardInstance.getIdAndName(), attachTo.getIdAndName());
  }

  public void detach(CardInstance cardToDestroy, CardInstance attachedCard) {
    attachedCard.getModifiers().unsetAttachedId();
    LOGGER.info("detached {} from {}", attachedCard.getIdAndName(), cardToDestroy.getIdAndName());
  }
}
