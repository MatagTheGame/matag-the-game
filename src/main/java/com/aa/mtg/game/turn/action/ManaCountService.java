package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.player.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.aa.mtg.cards.ability.type.AbilityType.TAP_ADD_MANA;

@Component
public class ManaCountService {

    ArrayList<Cost> verifyManaPaid(Map<Integer, List<String>> mana, Player currentPlayer) {
        ArrayList<Cost> paidCost = new ArrayList<>();
        for (int cardInstanceId : mana.keySet()) {
            List<String> requestedManas = mana.get(cardInstanceId);

            for (String requestedMana : requestedManas) {
                CardInstance cardInstanceToTap = currentPlayer.getBattlefield().findCardById(cardInstanceId);
                if (!cardInstanceToTap.hasAbility(TAP_ADD_MANA)) {
                    throw new MessageException(cardInstanceToTap.getIdAndName() + " cannot be tapped for mana.");

                } else if (cardInstanceToTap.getModifiers().isTapped()) {
                    throw new MessageException(cardInstanceToTap.getIdAndName() + " is already tapped.");

                } else if (!cardInstanceToTap.canProduceMana(Color.valueOf(requestedMana))) {
                    throw new MessageException(cardInstanceToTap.getIdAndName() + " cannot produce " + requestedMana);

                } else {
                    paidCost.add(Cost.valueOf(requestedMana));
                }
            }
        }
        return paidCost;
    }

}
