package com.matag.game.turn.action.combat;

import com.matag.cards.ability.type.AbilityType;
import com.matag.cards.properties.Type;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.message.MessageException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockerChecker {
  public void checkIfCanBlock(CardInstance attacker, List<CardInstance> blockers) {
    for (var blocker : blockers) {
      checkIfCanBlock(attacker, blocker);
    }

    if (attacker.hasAbilityType(AbilityType.MENACE) && blockers.size() == 1) {
      throw new MessageException(blockers.get(0).getIdAndName() + " cannot block " + attacker.getIdAndName() + " alone as it has menace.");
    }
  }

  private void checkIfCanBlock(CardInstance attacker, CardInstance blocker) {
    if (!blocker.isOfType(Type.CREATURE)) {
      throw new MessageException(blocker.getIdAndName() + " is not of type Creature.");
    }

    if (blocker.getModifiers().isTapped()) {
      throw new MessageException(blocker.getIdAndName() + " is tapped and cannot block.");
    }

    if (attacker.hasAbilityType(AbilityType.FLYING)) {
      if (!(blocker.hasAbilityType(AbilityType.FLYING) || blocker.hasAbilityType(AbilityType.REACH))) {
        throw new MessageException(blocker.getIdAndName() + " cannot block " + attacker.getIdAndName() + " as it has flying.");
      }
    }
  }
}
