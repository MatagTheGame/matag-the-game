package com.matag.game.turn.action.permanent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;

@Component
public class GainControlPermanentService {
  private static final Logger LOGGER = LoggerFactory.getLogger(GainControlPermanentService.class);

  public void gainControlUntilEndOfTurn(CardInstance target, String newControllerName) {
    target.getModifiers().getModifiersUntilEndOfTurn().setNewController(newControllerName);
    LOGGER.info("Changed controller until end of turn of " + target.getIdAndName() + " to " + newControllerName);
  }
}
