package com.aa.mtg.game.turn.action.permanent;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GainControlPermanentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GainControlPermanentService.class);

    public void gainControlUntilEndOfTurn(GameStatus gameStatus, CardInstance target, String newControllerName) {
        target.getModifiers().setControllerUntilEndOfTurn(newControllerName);
        LOGGER.info("Changed controller until end of turn of " + target.getIdAndName() + " to " + newControllerName);
    }
}
