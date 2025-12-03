package com.matag.game.turn.action.cast;

import org.springframework.stereotype.Component;

import com.matag.cards.ability.type.AbilityType;
import com.matag.game.cardinstance.CardInstance;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class InstantSpeedService {
  public boolean isAtInstantSpeed(CardInstance cardToCast, String playedAbility) {
    if (playedAbility != null) {
      var abilities = cardToCast.getAbilitiesByType(AbilityType.valueOf(playedAbility));
      for (var ability : abilities) {
        if (!ability.getAbility().getSorcerySpeed()) {
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
