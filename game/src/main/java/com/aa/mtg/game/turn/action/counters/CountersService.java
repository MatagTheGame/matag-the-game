package com.aa.mtg.game.turn.action.counters;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CountersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountersService.class);

    public void addPlus1Counters(GameStatus gameStatus, CardInstance target, int counters) {
        if (counters > 0) {
            target.getModifiers().getCounters().addPlus1Counters(1);
            LOGGER.info(target.getIdAndName() + " got " + counters + " +1/+1 counters");
        }
    }
}
