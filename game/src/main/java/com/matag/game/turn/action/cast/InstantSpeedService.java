package com.matag.game.turn.action.cast;

import com.matag.cards.ability.trigger.TriggerType;
import com.matag.cards.ability.type.AbilityType;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InstantSpeedService {
  public boolean isAtInstantSpeed(CardInstance cardToCast, String playedAbility) {
    if (playedAbility != null) {
      if (!getCardInstanceAbility(cardToCast, playedAbility).isSorcerySpeed()) {
        return true;
      }
    }

    if (cardToCast.isInstantSpeed()) {
      return true;
    }

    return false;
  }

  private CardInstanceAbility getCardInstanceAbility(CardInstance cardToCast, String playedAbility) {
    return cardToCast.getAbilitiesByType(AbilityType.valueOf(playedAbility)).stream()
      .findFirst()
      .orElseThrow(RuntimeException::new);
  }
}
