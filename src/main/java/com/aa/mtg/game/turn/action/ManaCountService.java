package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.player.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.aa.mtg.cards.properties.Cost.fromColor;

@Component
public class ManaCountService {

    ArrayList<Cost> verifyManaPaid(Map<Integer, List<String>> mana, Player currentPlayer) {
        ArrayList<Cost> paidCost = new ArrayList<>();
        for (int cardInstanceId : mana.keySet()) {
            CardInstance landToTap = currentPlayer.getBattlefield().findCardById(cardInstanceId);
            if (!landToTap.isOfType(Type.LAND)) {
                throw new MessageException("The card you are trying to tap for mana is not a land.");
            } else if (landToTap.getModifiers().isTapped()) {
                throw new MessageException("The land you are trying to tap is already tapped.");
            }
            // FIXME choose the color passed into mana
            Color firstColor = landToTap.getCard().getColors().iterator().next();
            paidCost.add(fromColor(firstColor));
        }
        return paidCost;
    }

}
