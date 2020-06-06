package com.matag.game.turn.action.cast;

import com.matag.cards.ability.trigger.TriggerSubtype;
import com.matag.game.turn.action.selection.CardInstanceSelectorService;
import com.matag.game.turn.action.when.WhenTriggerService;
import org.springframework.stereotype.Component;

import static com.matag.cards.ability.trigger.TriggerSubtype.WHEN_CAST;

@Component
public class WhenCastService extends WhenTriggerService {
  public WhenCastService(CardInstanceSelectorService cardInstanceSelectorService) {
    super(cardInstanceSelectorService);
  }

  @Override
  public TriggerSubtype triggerSubtype() {
    return WHEN_CAST;
  }
}
