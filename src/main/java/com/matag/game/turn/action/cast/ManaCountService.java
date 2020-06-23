package com.matag.game.turn.action.cast;

import com.matag.game.cardinstance.CardInstance;
import com.matag.cards.properties.Color;
import com.matag.cards.properties.Cost;
import com.matag.game.message.MessageException;
import com.matag.game.player.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.matag.cards.ability.type.AbilityType.TAP_ADD_MANA;

@Component
public class ManaCountService {

  public ArrayList<Cost> verifyManaPaid(Map<Integer, List<String>> mana, Player currentPlayer) {
    ArrayList<Cost> paidCost = new ArrayList<>();
    for (int cardInstanceId : mana.keySet()) {
      List<String> requestedManas = mana.get(cardInstanceId);

      for (String requestedMana : requestedManas) {
        CardInstance cardInstanceToTap = currentPlayer.getBattlefield().findCardById(cardInstanceId);
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
    Map<String, Integer> map = new HashMap<>();

    for (List<String> values : mana.values()) {
      for (String value : values) {
        if (!map.containsKey(value)) {
          map.put(value, 0);
        }
        map.put(value, map.get(value) + 1);
      }
    }

    return map;
  }
}
