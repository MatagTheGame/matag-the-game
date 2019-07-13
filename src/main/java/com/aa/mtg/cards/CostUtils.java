package com.aa.mtg.cards;

import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.type.AbilityType.abilityType;

public class CostUtils {

    public static boolean isCastingCostFulfilled(Card card, List<Color> manaPaid, String ability) {
        ArrayList<Color> manaPaidCopy = new ArrayList<>(manaPaid);

        for (Cost cost : getCost(card, ability)) {
            boolean removed = false;
            switch (cost) {
                case WHITE:
                    removed = manaPaidCopy.remove(Color.WHITE);
                    break;
                case BLUE:
                    removed = manaPaidCopy.remove(Color.BLUE);
                    break;
                case BLACK:
                    removed = manaPaidCopy.remove(Color.BLACK);
                    break;
                case RED:
                    removed = manaPaidCopy.remove(Color.RED);
                    break;
                case GREEN:
                    removed = manaPaidCopy.remove(Color.GREEN);
                    break;
                case COLORLESS:
                    if (manaPaidCopy.size() > 0) {
                        manaPaidCopy.remove(0);
                        removed = true;
                    }
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
                .filter(a -> a.getAbilityTypes().contains(abilityType(ability)))
                .orElseThrow(() -> new RuntimeException("ability " + ability +" not found on card " + card.getName()))
                .getTrigger()
                .getCost();
    }

}
