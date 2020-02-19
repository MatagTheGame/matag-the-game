package com.aa.mtg.cards.cost;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Cost;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.type.AbilityType.abilityType;
import static com.aa.mtg.cards.properties.Cost.COLORLESS;

@Service
public class CostService {

    public boolean isCastingCostFulfilled(Card card, List<Cost> manaPaid, String ability) {
        ArrayList<Cost> manaPaidCopy = new ArrayList<>(manaPaid);

        for (Cost cost : getCost(card, ability)) {
            boolean removed = false;

            if (cost == COLORLESS) {
                if (manaPaidCopy.size() > 0) {
                    manaPaidCopy.remove(0);
                    removed = true;
                }
            } else {
                removed = manaPaidCopy.remove(cost);
            }


            if (!removed) {
                return false;
            }
        }

        return true;
    }

    private static List<Cost> getCost(Card card, String ability) {
        if (ability == null || getAbilityCost(card, ability) == null) {
            return card.getCost();

        } else {
            return getAbilityCost(card, ability);
        }
    }

    private static List<Cost> getAbilityCost(Card card, String ability) {
        return card.getAbilities().stream()
                .findFirst()
                .filter(c -> c.getAbilityType().equals(abilityType(ability)))
                .orElseThrow(() -> new RuntimeException("ability " + ability +" not found on card " + card.getName()))
                .getTrigger()
                .getCost();
    }

}
