package com.matag.game.turn.action.cast;

import com.matag.cards.ability.type.AbilityType;
import com.matag.game.cardinstance.CardInstance;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InstantSpeedService {
  public boolean isAtInstantSpeed(CardInstance cardToCast, String playedAbility) {
    if (playedAbility != null) {
      var abilities = cardToCast.getAbilitiesByType(AbilityType.valueOf(playedAbility));
      for (var ability : abilities) {
        if (!ability.isSorcerySpeed()) {
          return true;
        }
      }
    }

    if (cardToCast.isInstantSpeed()) {
      return true;
    }

    return false;
  }

}
