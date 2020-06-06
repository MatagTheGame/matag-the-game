package com.matag.game.turn.action.leave;

import com.matag.cards.ability.trigger.TriggerSubtype;
import com.matag.game.turn.action.selection.CardInstanceSelectorService;
import com.matag.game.turn.action.when.WhenTriggerService;
import org.springframework.stereotype.Component;

import static com.matag.cards.ability.trigger.TriggerSubtype.WHEN_DIE;

@Component
public class WhenDieService extends WhenTriggerService {
  public WhenDieService(CardInstanceSelectorService cardInstanceSelectorService) {
    super(cardInstanceSelectorService);
  }

  @Override
  public TriggerSubtype triggerSubtype() {
    return WHEN_DIE;
  }
}
