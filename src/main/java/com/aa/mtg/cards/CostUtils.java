package com.aa.mtg.cards;

import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;

import java.util.ArrayList;
import java.util.List;

public class CostUtils {

    public static boolean isCastingCostFulfilled(Card card, List<Color> manaPaid) {
        ArrayList<Color> manaPaidCopy = new ArrayList<>(manaPaid);

        for (Cost cost : card.getCost()) {
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

}
