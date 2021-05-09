package com.matag.game.turn.action.cast;

import static com.matag.cards.ability.type.AbilityType.TAP_ADD_MANA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.matag.cards.properties.Color;
import com.matag.cards.properties.Cost;
import com.matag.game.message.MessageException;
import com.matag.game.player.Player;

@Component
public class ManaCountService {

  public ArrayList<Cost> verifyManaPaid(Map<Integer, List<String>> mana, Player currentPlayer) {
    var paidCost = new ArrayList<Cost>();
    for (var cardInstanceId : mana.keySet()) {
      var requestedManas = mana.get(cardInstanceId);

      for (var requestedMana : requestedManas) {
        var cardInstanceToTap = currentPlayer.getBattlefield().findCardById(cardInstanceId);
        if (!cardInstanceToTap.hasAbilityType(TAP_ADD_MANA)) {
          throw new MessageException(cardInstanceToTap.getIdAndName() + " cannot be tapped for mana.");

        } else if (cardInstanceToTap.getModifiers().isTapped()) {
          throw new MessageException(cardInstanceToTap.getIdAndName() + " is already tapped.");

        } else if (!requestedMana.equals("COLORLESS") && !cardInstanceToTap.canProduceMana(Color.valueOf(requestedMana))) {
          throw new MessageException(cardInstanceToTap.getIdAndName() + " cannot produce " + requestedMana);

        } else {
          paidCost.add(Cost.valueOf(requestedMana));
        }
      }
    }
    return paidCost;
  }

  public Map<String, Integer> countManaPaid(Map<Integer, List<String>> mana) {
    var map = new HashMap<String, Integer>();

    for (var values : mana.values()) {
      for (var value : values) {
        if (!map.containsKey(value)) {
          map.put(value, 0);
        }
        map.put(value, map.get(value) + 1);
      }
    }

    return map;
  }
}
