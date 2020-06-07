package com.matag.game.turn.action.counters;

import com.matag.cards.ability.type.AbilityType;
import com.matag.game.cardinstance.CardInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CountersService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CountersService.class);

  public void addPlus1Counters(CardInstance target, int counters) {
    if (counters > 0) {
      target.getModifiers().getCounters().addPlus1Counters(1);
      LOGGER.info(target.getIdAndName() + " got " + counters + " +1/+1 counters");
    }
  }

  public void addKeywordCounter(CardInstance target, AbilityType keywordCounter) {
    target.getModifiers().getCounters().addKeywordCounter(keywordCounter);
    LOGGER.info(target.getIdAndName() + " got " + keywordCounter + " counters");
  }
}
